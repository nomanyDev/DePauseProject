import React, { useState, useEffect } from "react";
import { Container, Box, Typography, Card, CardContent, Pagination } from "@mui/material";
import ApiService from "../../service/ApiService";

const AppointmentHistoryPage = () => {
  const [appointments, setAppointments] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [error, setError] = useState("");

  useEffect(() => {
    const role = localStorage.getItem("role");
    const userId = localStorage.getItem("userId");
    const psychologistId = localStorage.getItem("psychologistId");

    console.log("Role:", role, "User ID:", userId, "Psychologist ID:", psychologistId);

    if (role === "USER" && userId) {
      fetchAppointmentsForUser(userId, currentPage);
    } else if (role === "PSYCHOLOGIST" && psychologistId) {
      fetchAppointmentsForPsychologist(psychologistId, currentPage);
    } else {
      setError("Invalid role or missing ID. Please log in.");
    }
  }, [currentPage]);

  const fetchAppointmentsForUser = async (userId, page) => {
    try {
      const response = await ApiService.getUserAppointments(userId, page, 10);
      setAppointments(response.page?.content || []);
      setTotalPages(response.page?.totalPages || 0);
    } catch (err) {
      console.error("Error fetching appointments for user:", err.message);
      setError("Failed to fetch user appointments. Please try again.");
    }
  };

  const fetchAppointmentsForPsychologist = async (psychologistId, page) => {
    try {
      const response = await ApiService.getPsychologistAppointments(psychologistId, page, 10);
      setAppointments(response.page?.content || []);
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
                <Typography variant="h6">
                  Appointment ID: {appointment.id}
                </Typography>
                <Typography>
                  Date and Time:{" "}
                  {new Date(appointment.appointmentTime).toLocaleString()}
                </Typography>
                <Typography>Status: {appointment.status}</Typography>
                <Typography>
                  Psychologist:{" "}
                  {appointment.psychologist
                    ? appointment.psychologist.description
                    : "N/A"}
                </Typography>
                <Typography>
                  Client:{" "}
                  {appointment.user
                    ? `${appointment.user.firstName} ${appointment.user.lastName}`
                    : "N/A"}
                </Typography>
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
