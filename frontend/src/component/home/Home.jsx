import React, { useEffect, useState } from "react";
import { Container, Typography, Box, Button, Card, CardContent, Avatar } from "@mui/material";
import { useNavigate } from "react-router-dom";
import ApiService from "../../service/ApiService";

const Home = () => {
  const navigate = useNavigate();
  const [psychologists, setPsychologists] = useState([]);

  useEffect(() => {
    const fetchPsychologists = async () => {
      try {
        const response = await ApiService.getPsychologistsList(0, 6); // Fetch up to 6 psychologists
        setPsychologists(response.page?.content || []);
      } catch (error) {
        console.error("Error fetching psychologists:", error);
      }
    };
    fetchPsychologists();
  }, []);

  return (
    <Container>
      {/* Hero Section */}
      <Box
        sx={{
          textAlign: "center",
          mt: 4,
          p: 4,
          boxShadow: 3,
          borderRadius: 2,
          backgroundColor: "#f0f4f8",
        }}
      >
        <Typography variant="h3" gutterBottom sx={{ fontWeight: "bold" }}>
          Welcome to DePause
        </Typography>
        <Typography variant="h6" gutterBottom>
          Find peace of mind with the right psychologist. DePause will help you take your life off pause.
        </Typography>
        <Box sx={{ mt: 3 }}>
          <Button
            variant="contained"
            color="primary"
            sx={{ mx: 2 }}
            onClick={() => navigate("/psychologists")}
          >
            View Psychologists
          </Button>
        </Box>
      </Box>

      {/* Importance Section */}
      <Box sx={{ mt: 5, textAlign: "center", p: 2 }}>
        <Typography variant="h4" gutterBottom>
          Why Mental Health Matters
        </Typography>
        <Typography variant="body1" sx={{ maxWidth: 800, mx: "auto" }}>
          Mental health is essential for a fulfilling life. Finding the right help at the right time can reduce stress,
          improve relationships, and enhance personal growth. DePause will help you take your life off pause.
        </Typography>
      </Box>

      {/* Scrollable Psychologist Cards */}
      <Box sx={{ mt: 4 }}>
        <Typography variant="h5" gutterBottom sx={{ textAlign: "center", mb: 2 }}>
          Meet Our Top Psychologists
        </Typography>
        <Box
          sx={{
            display: "flex",
            overflowX: "auto",
            gap: 2,
            p: 2,
          }}
        >
          {psychologists.length > 0 ? (
            psychologists.map((psychologist) => (
              <Card key={psychologist.id} sx={{ minWidth: 220, boxShadow: 3 }}>
                <CardContent sx={{ textAlign: "center" }}>
                  <Avatar
                    src={psychologist.profilePhotoUrl || "/static/images/avatar/default.png"}
                    alt={psychologist.description || "Psychologist"}
                    sx={{ width: 80, height: 80, mx: "auto", mb: 1 }}
                  />
                  <Typography variant="h6" sx={{ fontWeight: "bold" }}>
                    {psychologist.description || "Unknown Psychologist"}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {psychologist.therapyTypes?.join(", ") || "No Therapy Types Specified"}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Experience: {psychologist.experience || "N/A"} years
                  </Typography>
                  <Button
                    size="small"
                    variant="contained"
                    sx={{ mt: 2 }}
                    onClick={() => navigate(`/psychologists/${psychologist.id}`)}
                  >
                    View Profile
                  </Button>
                </CardContent>
              </Card>
            ))
          ) : (
            <Typography variant="body1" sx={{ textAlign: "center" }}>
              No psychologists available right now.
            </Typography>
          )}
        </Box>
      </Box>

      {/* Call-to-Action Section */}
      <Box sx={{ mt: 5, p: 4, textAlign: "center", backgroundColor: "#e8f5e9", borderRadius: 2, boxShadow: 2 }}>
        <Typography variant="h5" gutterBottom>
          Ready to Start Your Journey?
        </Typography>
        <Typography variant="body1" sx={{ mb: 2 }}>
          Take the first step today. Browse top professionals or book your first session now.
        </Typography>
        <Button variant="contained" color="primary" onClick={() => navigate("/register")}>
          Get Started
        </Button>
      </Box>
    </Container>
  );
};

export default Home;
