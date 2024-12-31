import React, { useState, useEffect } from "react";
import {
  Container,
  TextField,
  Button,
  Box,
  Typography,
  Alert,
  MenuItem,
  CircularProgress,
} from "@mui/material";
import ApiService from "../../service/ApiService";

const BookAppointmentPage = ({ psychologistId, onClose }) => {
  const [selectedDate, setSelectedDate] = useState("");
  const [availableSlots, setAvailableSlots] = useState([]);
  const [selectedSlot, setSelectedSlot] = useState("");
  const [therapyType, setTherapyType] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loadingSlots, setLoadingSlots] = useState(false);
  const [bookingLoading, setBookingLoading] = useState(false);
  const [userId, setUserId] = useState(null);

  const therapyTypes = [
    { value: "INDIVIDUAL", label: "Individual Therapy" },
    { value: "FAMILY", label: "Family Therapy" },
    { value: "COUPLE", label: "Couple Therapy" },
    { value: "CHILD", label: "Child Therapy" },
  ];

  useEffect(() => {
    const fetchUserProfile = async () => {
      try {
        const profile = await ApiService.fetchUserProfile();
        const id = profile.user?.id;
        if (id) {
          setUserId(id);
        } else {
          setError("Unable to fetch user ID. Please log in again.");
        }
      } catch (err) {
        console.error("Error fetching user profile:", err.message);
        setError("Failed to fetch user profile.");
      }
    };

    fetchUserProfile();
  }, []);

  const handleDateChange = async (event) => {
    const date = event.target.value;
    setSelectedDate(date);
    setSelectedSlot("");
    setError("");
    setSuccess("");

    if (!date) return;

    setLoadingSlots(true);
    try {
      console.log("Fetching available slots for date:", date, "Psychologist ID:", psychologistId);
      const response = await ApiService.getAvailableTimeSlots(psychologistId, date);
      console.log("Available slots response:", response);

      const baseSlots = generateDefaultSlots();

      // Убираем слоты с статусами BLOCKED и BOOKED
      const unavailableSlots = (response.data || [])
        .filter((slot) => slot.status === "BLOCKED" || slot.status === "BOOKED")
        .map((slot) => `${slot.startTime} - ${slot.endTime}`);

      const availableSlots = baseSlots.filter((slot) => !unavailableSlots.includes(slot));

      setAvailableSlots(availableSlots);
    } catch (err) {
      console.error("Error fetching available slots:", err.message);
      setError("Failed to fetch available time slots. Please try again.");
    } finally {
      setLoadingSlots(false);
    }
  };

  const generateDefaultSlots = () => {
    const startHour = 9;
    const endHour = 20;
    const slots = [];
    for (let hour = startHour; hour < endHour; hour++) {
      const time = `${hour.toString().padStart(2, '0')}:00`;
      const nextTime = `${(hour + 1).toString().padStart(2, '0')}:00`;
      slots.push(`${time} - ${nextTime}`);
    }
    return slots;
  };

  const handleSubmit = async () => {
    if (!selectedDate || !selectedSlot || !therapyType) {
      setError("Please fill in all required fields.");
      return;
    }

    setError("");
    setSuccess("");
    setBookingLoading(true);

    try {
      const appointmentTime = `${selectedDate}T${selectedSlot.split(" - ")[0]}`;

      await ApiService.bookAppointment({
        userId,
        psychologistId,
        appointmentTime,
        therapyType,
      });

      setSuccess("Appointment booked successfully!");
      setTimeout(() => {
        onClose();
      }, 2000);
    } catch (err) {
      console.error("Error booking appointment:", err.response?.data || err.message);
      setError(err.response?.data?.message || "Failed to book appointment. Please try again.");
    } finally {
      setBookingLoading(false);
    }
  };

  return (
    <Container>
      <Box sx={{ mt: 2, p: 2 }}>
        <Typography variant="h5" gutterBottom>
          Book an Appointment
        </Typography>
        {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
        {success && <Alert severity="success" sx={{ mb: 2 }}>{success}</Alert>}

        <TextField
          fullWidth
          margin="normal"
          label="Select Date"
          type="date"
          InputLabelProps={{
            shrink: true,
          }}
          value={selectedDate}
          onChange={handleDateChange}
          required
        />

        {loadingSlots ? (
          <Box sx={{ display: "flex", justifyContent: "center", my: 2 }}>
            <CircularProgress />
          </Box>
        ) : (
          <TextField
            fullWidth
            select
            margin="normal"
            label="Select Time Slot"
            value={selectedSlot}
            onChange={(e) => setSelectedSlot(e.target.value)}
            disabled={!availableSlots.length}
            required
          >
            {availableSlots.length > 0 ? (
              availableSlots.map((slot) => (
                <MenuItem key={slot} value={slot}>
                  {slot}
                </MenuItem>
              ))
            ) : (
              <MenuItem disabled>No available slots</MenuItem>
            )}
          </TextField>
        )}

        <TextField
          fullWidth
          select
          margin="normal"
          label="Select Therapy Type"
          value={therapyType}
          onChange={(e) => setTherapyType(e.target.value)}
          required
        >
          {therapyTypes.map((type) => (
            <MenuItem key={type.value} value={type.value}>
              {type.label}
            </MenuItem>
          ))}
        </TextField>

        <Box sx={{ display: "flex", gap: 2, mt: 3 }}>
          <Button
            variant="contained"
            color="primary"
            onClick={handleSubmit}
            disabled={bookingLoading || !userId}
          >
            {bookingLoading ? <CircularProgress size={24} /> : "Book Appointment"}
          </Button>
          <Button variant="outlined" color="info" onClick={onClose}>
            Cancel
          </Button>
        </Box>
      </Box>
    </Container>
  );
};

export default BookAppointmentPage;
