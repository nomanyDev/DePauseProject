import React, { useEffect, useState } from "react";
import { Box, Typography, Card, CardContent, CardMedia, Grid, Button, Dialog } from "@mui/material";
import { useLocation } from "react-router-dom";
import ApiService from "../../service/ApiService";
import BookAppointmentPage from "../appointments/BookAppointmentPage";

function PsychologistsPage() {
  const location = useLocation();
  const [psychologists, setPsychologists] = useState(location.state?.searchResults || []);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [selectedPsychologistId, setSelectedPsychologistId] = useState(null); // Для модального окна
  const [openBooking, setOpenBooking] = useState(false);

  useEffect(() => {
    if (!location.state?.searchResults) {
      fetchPsychologists(currentPage);
    }
  }, [currentPage]);

  const fetchPsychologists = async (page) => {
    try {
      const response = await ApiService.getPsychologistsList(page, 9); // 9 карточек на странице
      setPsychologists(response.page?.content || []);
      setTotalPages(response.page?.totalPages || 0);
    } catch (error) {
      console.error("Error fetching psychologists:", error.message);
    }
  };

  const handlePageChange = (event, value) => {
    setCurrentPage(value - 1);
  };

  const handleOpenBooking = (psychologistId) => {
    setSelectedPsychologistId(psychologistId);
    setOpenBooking(true);
  };

  const handleCloseBooking = () => {
    setOpenBooking(false);
    setSelectedPsychologistId(null);
  };

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h4" gutterBottom textAlign="center" color="primary">
        Our Psychologists
      </Typography>
      <Grid container spacing={3}>
        {psychologists.map((psychologist) => (
          <Grid item xs={12} sm={6} md={4} key={psychologist.id}>
            <Card
              sx={{
                boxShadow: 3,
                borderRadius: 2,
                height: "100%",
                display: "flex",
                flexDirection: "column",
              }}
            >
              {/* Фото психолога */}
              <CardMedia
                component="img"
                height="200"
                image={psychologist.photoUrl || "/default-avatar.png"}
                alt={psychologist.description || "Psychologist"}
              />
              <CardContent sx={{ flexGrow: 1 }}>
                <Typography variant="h6" component="div" textAlign="center" gutterBottom>
                  {psychologist.description || "No Name Provided"}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  <strong>Education:</strong> {psychologist.education || "Not specified"}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  <strong>Experience:</strong> {psychologist.experience || "Not specified"}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  <strong>Therapy Types:</strong>{" "}
                  {psychologist.therapyTypes?.join(", ") || "Not specified"}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  <strong>Price:</strong> ${psychologist.price || "N/A"}
                </Typography>
                <Button
                  variant="contained"
                  color="primary"
                  sx={{ mt: 2 }}
                  onClick={() => handleOpenBooking(psychologist.id)}
                >
                  Book Appointment
                </Button>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      {/* Модальное окно с компонентом BookAppointmentPage */}
      <Dialog open={openBooking} onClose={handleCloseBooking} fullWidth maxWidth="sm">
        <BookAppointmentPage psychologistId={selectedPsychologistId} onClose={handleCloseBooking} />
      </Dialog>
    </Box>
  );
}

export default PsychologistsPage;
