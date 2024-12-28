import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Box, Typography, Card, CardContent, Pagination, CircularProgress } from "@mui/material";
import ApiService from "../../service/ApiService";

const PsychologistReviewsPage = () => {
  const { psychologistId: paramPsychologistId } = useParams(); 
  const [psychologistId, setPsychologistId] = useState(paramPsychologistId || null);
  const [reviews, setReviews] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchPsychologistIdIfNeeded = async () => {
      if (!psychologistId) {
        try {
          const profile = await ApiService.fetchUserProfile();
          if (profile.user?.role === "PSYCHOLOGIST") {
            setPsychologistId(profile.psychologist?.id);
          } else {
            setError("Invalid psychologist ID. Please log in as a psychologist.");
          }
        } catch (error) {
          console.error("Error fetching profile:", error);
          setError("Failed to fetch psychologist profile.");
        }
      }
    };

    fetchPsychologistIdIfNeeded();
  }, [psychologistId]);

  useEffect(() => {
    const fetchReviews = async () => {
      if (!psychologistId) return;

      try {
        setLoading(true);
        const response = await ApiService.getPsychologistReviews(psychologistId, page, 5);
        setReviews(response.page.content || []);
        setTotalPages(response.page.totalPages || 0);
        setLoading(false);
      } catch (error) {
        console.error("Error fetching reviews:", error);
        setError("Failed to fetch reviews. Please try again.");
        setLoading(false);
      }
    };

    fetchReviews();
  }, [psychologistId, page]);

  const handlePageChange = (event, value) => {
    setPage(value - 1); // Pagination 
  };

  if (error) {
    return (
      <Box sx={{ p: 3 }}>
        <Typography variant="h6" color="error">
          {error}
        </Typography>
      </Box>
    );
  }

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h4" gutterBottom>
        Reviews for Psychologist {psychologistId}
      </Typography>
      {loading ? (
        <CircularProgress />
      ) : reviews.length > 0 ? (
        <Box>
          {reviews.map((review) => (
            <Card key={review.id} sx={{ mb: 2 }}>
              <CardContent>
                <Typography variant="h6">{review.content}</Typography>
                <Typography variant="body2" color="text.secondary">
                  Rating: {review.rating}/5
                </Typography>
                <Typography variant="caption" color="text.secondary">
                  User ID: {review.userId}
                </Typography>
              </CardContent>
            </Card>
          ))}
          <Pagination
            count={totalPages}
            page={page + 1}
            onChange={handlePageChange}
            sx={{ mt: 2 }}
          />
        </Box>
      ) : (
        <Typography>No reviews available.</Typography>
      )}
    </Box>
  );
};

export default PsychologistReviewsPage;
