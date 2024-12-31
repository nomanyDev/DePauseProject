import React, { useEffect, useState } from "react";
import { Container, Typography, Box, Button, Card, CardContent, Avatar } from "@mui/material";
import { useNavigate } from "react-router-dom";
import ApiService from "../../service/ApiService";
import Slider from "react-slick"; // react-slick
import "slick-carousel/slick/slick.css"; 
import "slick-carousel/slick/slick-theme.css";

const Home = () => {
  const navigate = useNavigate();
  const [psychologists, setPsychologists] = useState([]);

  const imagesWithCaptions = [
    {
      src: "https://depause-user-images.s3.us-east-1.amazonaws.com/pexels-marcus-aurelius-6787202.jpg",
      caption: "Find peace in the chaos.",
    },
    {
      src: "https://depause-user-images.s3.us-east-1.amazonaws.com/pexels-puwadon-sang-ngern-2168173-5340280.jpg",
      caption: "Rediscover yourself.",
    },
    {
      src: "https://depause-user-images.s3.us-east-1.amazonaws.com/pexels-yaroslav-shuraev-8692129.jpg",
      caption: "Every step counts towards healing.",
    },
  ];

  useEffect(() => {
    const fetchPsychologists = async () => {
      try {
        const response = await ApiService.getPsychologistsList(0, 6); // Fetch up to 6 psychologists
        const psychologists = response.page?.content || [];
        // Reverse the order to show the newest psychologists first
        setPsychologists(psychologists.reverse());
      } catch (error) {
        console.error("Error fetching psychologists:", error);
      }
    };
    fetchPsychologists();
  }, []);

  const sliderSettings = {
    dots: true,
    infinite: true,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
    autoplay: true,
    autoplaySpeed: 15000, // 15 seconds
    arrows: false,
  };

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

      {/* Carousel Section */}
      <Box sx={{ mt: 4 }}>
        <Slider {...sliderSettings}>
          {imagesWithCaptions.map((item, index) => (
            <Box key={index} sx={{ position: "relative" }}>
              <img
                src={item.src}
                alt={`Slide ${index + 1}`}
                style={{ width: "100%", height: "auto", borderRadius: "8px" }}
              />
              <Typography
                variant="h5"
                sx={{
                  position: "absolute",
                  bottom: "20px",
                  left: "20px",
                  color: "white",
                  backgroundColor: "rgba(0, 0, 0, 0.5)",
                  padding: "10px",
                  borderRadius: "4px",
                }}
              >
                {item.caption}
              </Typography>
            </Box>
          ))}
        </Slider>
      </Box>

      {/* Scrollable Psychologist Cards */}
      <Box sx={{ mt: 4 }}>
        <Typography variant="h5" gutterBottom sx={{ textAlign: "center", mb: 2 }}>
          Meet Our New Psychologists
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
                    alt={psychologist.firstName || "Psychologist"}
                    sx={{ width: 80, height: 80, mx: "auto", mb: 1 }}
                  />
                  <Typography variant="h6" sx={{ fontWeight: "bold" }}>
                    {psychologist.firstName && psychologist.lastName
                      ? `${psychologist.firstName} ${psychologist.lastName}`
                      : "Name not specified"}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {psychologist.therapyTypes?.join(", ") || "No Therapy Types Specified"}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Experience: {psychologist.experience || "N/A"} years
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Price: ${psychologist.price || "N/A"}
                  </Typography>
                  <Button
                    size="small"
                    variant="contained"
                    sx={{ mt: 2 }}
                    onClick={() => navigate(`/psychologists/${psychologist.id}`)}
                  >
                    View Profile
                  </Button>
                  <Button
                    size="small"
                    variant="outlined"
                    sx={{ mt: 1 }}
                    onClick={() => navigate(`/psychologists/${psychologist.id}/reviews`)}
                  >
                    View Reviews
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
