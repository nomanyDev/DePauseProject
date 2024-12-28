import React, { useEffect, useState } from "react";
import { Box, Typography, Card, CardContent, Avatar, Grid, Button, Dialog } from "@mui/material";
import { useLocation, useNavigate } from "react-router-dom";
import ApiService from "../../service/ApiService";
import BookAppointmentPage from "../appointments/BookAppointmentPage";

function PsychologistsPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const [psychologists, setPsychologists] = useState(location.state?.searchResults || []);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [selectedPsychologistId, setSelectedPsychologistId] = useState(null);
  const [openBooking, setOpenBooking] = useState(false);

  useEffect(() => {
    if (!location.state?.searchResults) {
      fetchPsychologists(currentPage);
    }
  }, [currentPage]);

  const fetchPsychologists = async (page) => {
    try {
      const response = await ApiService.getPsychologistsList(page, 9);
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

  const getAvatar = (psychologist) => {
    if (psychologist.profilePhotoUrl) {
      return (
        <Avatar
          src={psychologist.profilePhotoUrl}
          sx={{
            width: "100%",
            height: 250,
            borderRadius: 2, 
          }}
          variant="square"
        />
      );
    }
    const initials =
      psychologist.description?.split(" ").map((name) => name[0]).join("").toUpperCase() || "P";
    return (
      <Avatar
        sx={{
          bgcolor: "primary.main",
          width: "100%",
          height: 250,
          borderRadius: 2,
        }}
        variant="square"
      >
        {initials}
      </Avatar>
    );
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
                display: "flex",
                flexDirection: "column",
                height: "100%",
                p: 2,
              }}
            >
              {/* Фото психолога */}
              {getAvatar(psychologist)}
              <CardContent sx={{ flexGrow: 1, textAlign: "center" }}>
                <Typography variant="h6" component="div" gutterBottom>
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
                <Box
                  sx={{
                    mt: 3,
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center",
                    gap: 1,
                  }}
                >
                  <Button
                    variant="contained"
                    color="primary"
                    fullWidth
                    onClick={() => handleOpenBooking(psychologist.id)}
                    sx={{ fontWeight: "bold", mb: 1 }}
                  >
                    Book Appointment
                  </Button>
                  <Button
                    size="small"
                    variant="outlined"
                    fullWidth
                    onClick={() => navigate(`/psychologists/${psychologist.id}`)}
                  >
                    View Profile
                  </Button>
                  {/* hidden 
                  <Button
                    size="small"
                    variant="outlined"
                    fullWidth
                    onClick={() => navigate(`/psychologists/${psychologist.id}/reviews`)}
                  >
                    View Reviews
                  </Button>
                  */}
                </Box>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      {/* Диалоговое окно записи на прием */}
      <Dialog open={openBooking} onClose={handleCloseBooking} fullWidth maxWidth="sm">
        <BookAppointmentPage psychologistId={selectedPsychologistId} onClose={handleCloseBooking} />
      </Dialog>
    </Box>
  );
}

export default PsychologistsPage;
