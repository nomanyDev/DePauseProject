import React, { useState } from "react";
import {
  Container,
  TextField,
  Button,
  Box,
  Typography,
  Alert,
} from "@mui/material";
import ApiService from "../../service/ApiService";

const BookAppointmentPage = ({ psychologistId, onClose }) => {
  const [appointmentTime, setAppointmentTime] = useState(""); // Дата и время записи
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleSubmit = async () => {
    if (!appointmentTime) {
      setError("Please specify the appointment time.");
      return;
    }

    try {
      const userId = localStorage.getItem("userId"); // Получаем userId из localStorage

      console.log("Booking Appointment:", { userId, psychologistId, appointmentTime });

      // Отправляем запрос без therapyType
      await ApiService.bookAppointment({
        userId,
        psychologistId,
        appointmentTime,
      });

      setSuccess("Appointment booked successfully!");
      setError("");
      setTimeout(() => onClose(), 2000); // Закрыть модальное окно после успешного бронирования
    } catch (err) {
      console.error("Error booking appointment:", err.response?.data || err.message);
      setError(err.response?.data?.message || "Failed to book appointment. Please try again.");
      setSuccess("");
    }
  };

  return (
    <Container>
      <Box sx={{ mt: 2, p: 2 }}>
        <Typography variant="h5" gutterBottom>
          Book an Appointment
        </Typography>
        {error && <Alert severity="error">{error}</Alert>}
        {success && <Alert severity="success">{success}</Alert>}

        {/* Поле для ввода времени записи */}
        <TextField
          fullWidth
          margin="normal"
          label="Appointment Time"
          type="datetime-local"
          InputLabelProps={{
            shrink: true,
          }}
          value={appointmentTime}
          onChange={(e) => setAppointmentTime(e.target.value)}
          required
        />

        {/* Кнопки */}
        <Button
          variant="contained"
          color="primary"
          sx={{ mt: 2 }}
          onClick={handleSubmit}
        >
          Book
        </Button>
        <Button sx={{ mt: 1, ml: 1 }} onClick={onClose}>
          Cancel
        </Button>
      </Box>
    </Container>
  );
};

export default BookAppointmentPage;
