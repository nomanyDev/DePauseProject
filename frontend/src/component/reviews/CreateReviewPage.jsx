import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { Box, Typography, TextField, Button, CircularProgress, Alert, Rating } from "@mui/material";
import ApiService from "../../service/ApiService";

const CreateReviewPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { appointment } = location.state || {}; // Переданный объект записи

  const [content, setContent] = useState("");
  const [rating, setRating] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);

  const handleSubmit = async () => {
    if (!content || !rating) {
      setError("Please fill in all fields.");
      return;
    }

    if (rating < 1 || rating > 5) {
      setError("Rating must be between 1 and 5.");
      return;
    }

    setError("");
    setLoading(true);

    try {
      const reviewData = {
        content,
        rating: parseInt(rating, 10),
        appointmentId: appointment.id,
        userId: appointment.user.id,
        psychologistId: appointment.psychologist.id,
      };

      await ApiService.createReview(reviewData);
      setSuccess(true);
      setTimeout(() => {
        navigate("/appointments");
      }, 2000); // Redirect after success
    } catch (err) {
      console.error("Error creating review:", err.message);
      setError("Failed to create review. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h4" gutterBottom>
        Create Review
      </Typography>
      {appointment && (
        <Typography variant="body1" gutterBottom>
          Reviewing session with psychologist: <strong>{appointment.psychologist.description}</strong>
        </Typography>
      )}
      {error && <Alert severity="error">{error}</Alert>}
      {success && <Alert severity="success">Review created successfully! Redirecting...</Alert>}
      <Box sx={{ mt: 3 }}>
        <TextField
          fullWidth
          multiline
          rows={4}
          label="Your Review"
          variant="outlined"
          value={content}
          onChange={(e) => setContent(e.target.value)}
          sx={{ mb: 2 }}
        />
        <Box sx={{ display: "flex", alignItems: "center", gap: 2, mt: 2 }}>
            <Typography variant="body1">Rating:</Typography>
            <Rating
                name="rating"
                value={rating}
                onChange={(event, newValue) => {
                    setRating(newValue);
                }}
            />
        </Box>
        <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mt: 2 }}>
          <Button variant="contained" color="primary" onClick={handleSubmit} disabled={loading}>
            {loading ? <CircularProgress size={24} /> : "Submit Review"}
          </Button>
          <Button variant="outlined" color="secondary" onClick={() => navigate("/appointments")}>
            Cancel
          </Button>
        </Box>
      </Box>
    </Box>
  );
};

export default CreateReviewPage;
