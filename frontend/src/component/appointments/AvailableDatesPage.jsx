import React, { useState, useEffect } from "react";
import {
  Container,
  Button,
  Box,
  Typography,
  TextField,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
} from "@mui/material";
import ApiService from "../../service/ApiService";

const AvailableDatesPage = ({ psychologistId }) => {
  const [dates, setDates] = useState([]);
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const fetchAvailableDates = async () => {
    setLoading(true);
    try {
      const response = await ApiService.getAvailableDates(psychologistId, startDate, endDate);
      setDates(response.availableDates || []);
      setError("");
    } catch (err) {
      setError("Failed to fetch available dates.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container>
      <Box sx={{ mt: 2, p: 2 }}>
        <Typography variant="h5" gutterBottom>
          Manage Availability
        </Typography>
        <Box sx={{ display: "flex", gap: 2, my: 2 }}>
          <TextField
            type="date"
            label="Start Date"
            value={startDate}
            onChange={(e) => setStartDate(e.target.value)}
            InputLabelProps={{ shrink: true }}
          />
          <TextField
            type="date"
            label="End Date"
            value={endDate}
            onChange={(e) => setEndDate(e.target.value)}
            InputLabelProps={{ shrink: true }}
          />
          <Button variant="contained" onClick={fetchAvailableDates}>
            Fetch
          </Button>
        </Box>

        {error && <Typography color="error">{error}</Typography>}

        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Date</TableCell>
              <TableCell>Start Time</TableCell>
              <TableCell>End Time</TableCell>
              <TableCell>Status</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {dates.map((date) => (
              <TableRow key={date}>
                <TableCell>{date.date}</TableCell>
                <TableCell>{date.startTime}</TableCell>
                <TableCell>{date.endTime}</TableCell>
                <TableCell>{date.status}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Box>
    </Container>
  );
};

export default AvailableDatesPage;
