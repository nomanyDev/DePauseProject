import React, { useState, useEffect } from "react";
import {
  Container,
  Box,
  Typography,
  Button,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Select,
  MenuItem,
  Alert,
  CircularProgress,
} from "@mui/material";
import { useSearchParams } from "react-router-dom";
import ApiService from "../../service/ApiService";

const AvailableDatesPage = () => {
  const [searchParams] = useSearchParams();
  const psychologistId = searchParams.get("psychologistId");

  const [existingSlots, setExistingSlots] = useState([]);
  const [selectedDays, setSelectedDays] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [currentWeekForSlots, setCurrentWeekForSlots] = useState(new Date());
  const [currentWeekForAvailability, setCurrentWeekForAvailability] = useState(new Date());

  const daysOfWeek = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];
  const timeSlots = Array.from({ length: 12 }, (_, i) => `${(i + 9).toString().padStart(2, "0")}:00`);

  const calculateWeekRange = (date) => {
    const startOfWeek = new Date(date);
    startOfWeek.setDate(date.getDate() - date.getDay());
    const endOfWeek = new Date(startOfWeek);
    endOfWeek.setDate(startOfWeek.getDate() + 6);
    return { start: startOfWeek, end: endOfWeek };
  };

  const fetchExistingSlots = async () => {
    if (!psychologistId) {
      setError("Psychologist ID is missing.");
      return;
    }

    const { start, end } = calculateWeekRange(currentWeekForSlots);
    const startDate = start.toISOString().split("T")[0];
    const endDate = end.toISOString().split("T")[0];

    try {
      setLoading(true);
      const response = await ApiService.getAvailableDates(psychologistId, startDate, endDate);
      const formattedSlots = response.availableDates.map((date) => ({
        date,
        startTime: "",
        endTime: "",
        status: "AVAILABLE",
      }));
      setExistingSlots(formattedSlots);
      setError("");
    } catch (err) {
      console.error("Error fetching existing slots:", err.message);
      setError("Failed to load existing availability.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchExistingSlots();
  }, [psychologistId, currentWeekForSlots]);

  const handleWeeklyAvailabilitySubmit = async () => {
    if (!psychologistId) {
      setError("Psychologist ID is missing.");
      return;
    }

    try {
      const { start } = calculateWeekRange(currentWeekForAvailability);
      const selectedDates = selectedDays.map((day) => {
        const dayIndex = daysOfWeek.indexOf(day);
        const targetDate = new Date(start);
        targetDate.setDate(start.getDate() + dayIndex);
        return targetDate.toISOString().split("T")[0];
      });

      await ApiService.updateAvailability(psychologistId, selectedDates);
      setSuccess("Weekly availability updated successfully!");
      setError("");
    } catch (err) {
      console.error("Error updating weekly availability:", err.message);
      setError("Failed to update weekly availability.");
      setSuccess("");
    }
  };

  const goToPreviousWeekForSlots = () => {
    const newDate = new Date(currentWeekForSlots);
    newDate.setDate(currentWeekForSlots.getDate() - 7);
    setCurrentWeekForSlots(newDate);
  };

  const goToNextWeekForSlots = () => {
    const newDate = new Date(currentWeekForSlots);
    newDate.setDate(currentWeekForSlots.getDate() + 7);
    setCurrentWeekForSlots(newDate);
  };

  const goToPreviousWeekForAvailability = () => {
    const newDate = new Date(currentWeekForAvailability);
    newDate.setDate(currentWeekForAvailability.getDate() - 7);
    setCurrentWeekForAvailability(newDate);
  };

  const goToNextWeekForAvailability = () => {
    const newDate = new Date(currentWeekForAvailability);
    newDate.setDate(currentWeekForAvailability.getDate() + 7);
    setCurrentWeekForAvailability(newDate);
  };

  const handleSaveSlot = async (index) => {
    const slot = existingSlots[index];
    if (!slot.date || !slot.startTime || !slot.endTime) {
      setError("Please fill in all slot fields.");
      return;
    }

    try {
      await ApiService.updateAvailabilitySlots(psychologistId, [slot]);
      setSuccess("Slot updated successfully!");
      setError("");
    } catch (err) {
      console.error("Error updating slot:", err.message);
      setError("Failed to update slot. Please try again.");
      setSuccess("");
    }
  };

  return (
    <Container>
      <Box sx={{ mt: 4 }}>
        <Typography variant="h4" gutterBottom>
          Manage Availability
        </Typography>
        {error && <Alert severity="error">{error}</Alert>}
        {success && <Alert severity="success">{success}</Alert>}

        {/* Weekly Availability Section */}
        <Typography variant="h5" sx={{ mt: 3 }}>
          Set Weekly Availability
        </Typography>
        <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mt: 2 }}>
          <Button onClick={goToPreviousWeekForAvailability}>Previous Week</Button>
          <Typography>
            Week of {currentWeekForAvailability.toLocaleDateString("en-US", { month: "long", day: "numeric" })}
          </Typography>
          <Button onClick={goToNextWeekForAvailability}>Next Week</Button>
        </Box>
        <Box sx={{ display: "flex", gap: 2, mt: 2 }}>
          {daysOfWeek.map((day) => (
            <Button
              key={day}
              variant={selectedDays.includes(day) ? "contained" : "outlined"}
              onClick={() =>
                setSelectedDays((prevDays) =>
                  prevDays.includes(day)
                    ? prevDays.filter((d) => d !== day)
                    : [...prevDays, day]
                )
              }
            >
              {day}
            </Button>
          ))}
        </Box>
        <Button
          variant="contained"
          color="primary"
          sx={{ mt: 2 }}
          onClick={handleWeeklyAvailabilitySubmit}
        >
          Save Weekly Availability
        </Button>

        {/* Week Navigation for Existing Slots */}
        <Typography variant="h5" sx={{ mt: 3 }}>
          Existing Slots
        </Typography>
        <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mt: 2 }}>
          <Button onClick={goToPreviousWeekForSlots}>Previous Week</Button>
          <Typography>
            Week of {currentWeekForSlots.toLocaleDateString("en-US", { month: "long", day: "numeric" })}
          </Typography>
          <Button onClick={goToNextWeekForSlots}>Next Week</Button>
        </Box>
        {/* Existing Slots Table */}
        {loading ? (
          <CircularProgress sx={{ mt: 2 }} />
        ) : (
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Date</TableCell>
                <TableCell>Start Time</TableCell>
                <TableCell>End Time</TableCell>
                <TableCell>Status</TableCell>
                <TableCell>Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {existingSlots.map((slot, index) => (
                <TableRow key={index}>
                  <TableCell>{slot.date}</TableCell>
                  <TableCell>
                    <Select
                      value={slot.startTime}
                      onChange={(e) =>
                        setExistingSlots((prev) =>
                          prev.map((s, i) => (i === index ? { ...s, startTime: e.target.value } : s))
                        )
                      }
                    >
                      {timeSlots.map((time) => (
                        <MenuItem key={time} value={time}>
                          {time}
                        </MenuItem>
                      ))}
                    </Select>
                  </TableCell>
                  <TableCell>
                    <Select
                      value={slot.endTime}
                      onChange={(e) =>
                        setExistingSlots((prev) =>
                          prev.map((s, i) => (i === index ? { ...s, endTime: e.target.value } : s))
                        )
                      }
                    >
                      {timeSlots.map((time) => (
                        <MenuItem key={time} value={time}>
                          {time}
                        </MenuItem>
                      ))}
                    </Select>
                  </TableCell>
                  <TableCell>
                    <Select
                      value={slot.status}
                      onChange={(e) =>
                        setExistingSlots((prev) =>
                          prev.map((s, i) => (i === index ? { ...s, status: e.target.value } : s))
                        )
                      }
                    >
                      <MenuItem value="AVAILABLE">Available</MenuItem>
                      <MenuItem value="BLOCKED">Blocked</MenuItem>
                    </Select>
                  </TableCell>
                  <TableCell>
                    <Button
                      variant="contained"
                      onClick={() => handleSaveSlot(index)}
                    >
                      Save
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        )}
      </Box>
    </Container>
  );
};

export default AvailableDatesPage;
