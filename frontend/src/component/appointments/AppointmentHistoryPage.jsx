import React, { useState, useEffect } from "react";
import { Container, Box, Typography, Card, CardContent, Pagination, Button } from "@mui/material";
import { useNavigate } from "react-router-dom";
import ApiService from "../../service/ApiService";

const AppointmentHistoryPage = () => {
  const [appointments, setAppointments] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [error, setError] = useState("");
  const [role, setRole] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const fetchProfileAndAppointments = async () => {
      try {
        const profile = await ApiService.fetchUserProfile();
        console.log("Fetched profile:", profile);

        const role = profile.user?.role; 
        if (!role) {
          setError("Invalid role. Please log in.");
          return;
        }

        setRole(role);

        if (role === "USER") {
          const userId = profile.user?.id;
          if (!userId) {
            throw new Error("User ID is missing in profile data");
          }
          fetchAppointmentsForUser(userId, currentPage);
        } else if (role === "PSYCHOLOGIST") {
          const psychologistId = profile.psychologist?.id; 
          if (!psychologistId) {
            throw new Error("Psychologist ID is missing in profile data");
          }
          fetchAppointmentsForPsychologist(psychologistId, currentPage);
        } else {
          setError("Invalid role. Please log in.");
        }
      } catch (err) {
        console.error("Error fetching profile:", err.message);
        setError("Failed to fetch profile. Please log in again.");
      }
    };

    fetchProfileAndAppointments();
  }, [currentPage]);

  const fetchAppointmentsForUser = async (userId, page) => {
    try {
      console.log("Fetching appointments for user:", userId, "Page:", page);
      const response = await ApiService.getUserAppointments(userId, page, 10);

      // Переворачиваем массив, чтобы последние элементы были первыми
      const reversedAppointments = [...(response.page?.content || [])].reverse();

      setAppointments(reversedAppointments);
      setTotalPages(response.page?.totalPages || 0);
    } catch (err) {
      console.error("Error fetching appointments for user:", err.message);
      setError("Failed to fetch user appointments. Please try again.");
    }
  };

  const fetchAppointmentsForPsychologist = async (psychologistId, page) => {
    try {
      console.log("Fetching appointments for psychologist:", psychologistId, "Page:", page);
      const response = await ApiService.getPsychologistAppointments(psychologistId, page, 10);

      // reverse data from appointemnt to show from last added
      const reversedAppointments = [...(response.page?.content || [])].reverse();

      setAppointments(reversedAppointments);
      setTotalPages(response.page?.totalPages || 0);
    } catch (err) {
      console.error("Error fetching appointments for psychologist:", err.message);
      setError("Failed to fetch psychologist appointments. Please try again.");
    }
  };

  const handlePageChange = (event, value) => {
    setCurrentPage(value - 1);
  };

  return (
    <Container>
      <Box sx={{ mt: 4 }}>
        <Typography variant="h4" gutterBottom>
          Appointment History
        </Typography>
        {error && (
          <Typography variant="body1" color="error" gutterBottom>
            {error}
          </Typography>
        )}
        <Box>
          {appointments.map((appointment) => (
            <Card key={appointment.id} sx={{ mb: 2 }}>
              <CardContent>
                <Typography variant="h6">Appointment ID: {appointment.id}</Typography>
                <Typography>
                  Date and Time: {new Date(appointment.appointmentTime).toLocaleString()}
                </Typography>
                <Typography>Status: {appointment.status}</Typography>
                <Typography>
                  Psychologist: {appointment.psychologist?.description || "N/A"}
                </Typography>
                <Typography>
                  Client: {appointment.user?.firstName} {appointment.user?.lastName || "N/A"}
                </Typography>
                {role === "USER" && (
                  <Box sx={{ mt: 2 }}>
                    {appointment.hasReview ? (
                      <Button
                        variant="outlined"
                        color="secondary"
                        onClick={() =>
                          navigate(`/psychologists/${appointment.psychologist.id}/reviews`)
                        }
                      >
                        View Review
                      </Button>
                    ) : (
                      <Button
                        variant="contained"
                        color="primary"
                        onClick={() =>
                          navigate("/reviews/create", { state: { appointment } })
                        }
                      >
                        Create Review
                      </Button>
                    )}
                  </Box>
                )}
              </CardContent>
            </Card>
          ))}
        </Box>
        <Box sx={{ display: "flex", justifyContent: "center", mt: 2 }}>
          <Pagination
            count={totalPages}
            page={currentPage + 1}
            onChange={handlePageChange}
            color="primary"
          />
        </Box>
      </Box>
    </Container>
  );
};

export default AppointmentHistoryPage;
