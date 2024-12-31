import React, { useState, useEffect } from "react";
import { Container, Box, Typography, Card, CardContent, Button } from "@mui/material";
import PsychologistPagination from "../common/Pagination";
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
        const userRole = profile.user?.role;

        if (!userRole) {
          setError("Invalid role. Please log in.");
          return;
        }

        setRole(userRole);

        if (userRole === "USER") {
          fetchAppointmentsForUser(profile.user.id, currentPage);
        } else if (userRole === "PSYCHOLOGIST") {
          fetchAppointmentsForPsychologist(profile.psychologist.id, currentPage);
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
      const response = await ApiService.getUserAppointments(userId, page, 10);
      setAppointments(response.page?.content || []);
      setTotalPages(response.page?.totalPages || 0);
    } catch (err) {
      console.error("Error fetching user appointments:", err.message);
      setError("Failed to fetch user appointments. Please try again.");
    }
  };

  const fetchAppointmentsForPsychologist = async (psychologistId, page) => {
    try {
      const response = await ApiService.getPsychologistAppointments(psychologistId, page, 10);
      setAppointments(response.page?.content || []);
      setTotalPages(response.page?.totalPages || 0);
    } catch (err) {
      console.error("Error fetching psychologist appointments:", err.message);
      setError("Failed to fetch psychologist appointments. Please try again.");
    }
  };

  const handleCancelAppointment = async (appointmentId) => {
    try {
      const confirmed = window.confirm("Are you sure you want to cancel this appointment?");
      if (!confirmed) return;

      const response = await ApiService.cancelAppointment(appointmentId);
      setAppointments((prev) =>
        prev.map((appointment) =>
          appointment.id === appointmentId
            ? { ...appointment, status: response.appointment.status }
            : appointment
        )
      );
    } catch (err) {
      console.error("Error cancelling appointment:", err.message);
      setError("Failed to cancel appointment.");
    }
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  return (
    <Container>
      <Box sx={{ mt: 4 }}>
        <Typography variant="h4" gutterBottom>
          Appointment History
        </Typography>
        {error && <Typography color="error">{error}</Typography>}
        {appointments.map((appointment) => (
          <Card key={appointment.id} sx={{ mb: 2 }}>
            <CardContent>
              <Typography variant="h6">Appointment ID: {appointment.id}</Typography>
              <Typography>
                Date and Time: {new Date(appointment.appointmentTime).toLocaleString()}
              </Typography>
              <Typography>Status: {appointment.status}</Typography>
              <Typography>
                Psychologist: {appointment.psychologist?.firstName}{" "}
                {appointment.psychologist?.lastName}
              </Typography>
              <Typography>
                Client: {appointment.user?.firstName} {appointment.user?.lastName}
              </Typography>
              {role === "USER" && (
                <Box sx={{ mt: 2 }}>
                  <Button
                    variant="contained"
                    color="error"
                    disabled={appointment.status === "Cancelled"}
                    onClick={() => handleCancelAppointment(appointment.id)}
                  >
                    {appointment.status === "Cancelled" ? "Cancelled" : "Cancel Appointment"}
                  </Button>
                  <Box sx={{ mt: 1 }}>
                    {appointment.hasReview ? (
                      <Button
                        variant="contained"
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
                </Box>
              )}
            </CardContent>
          </Card>
        ))}
        <PsychologistPagination
          totalPages={totalPages}
          currentPage={currentPage}
          onPageChange={handlePageChange}
        />
      </Box>
    </Container>
  );
};

export default AppointmentHistoryPage;
