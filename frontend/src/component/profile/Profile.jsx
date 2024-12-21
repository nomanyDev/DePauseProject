import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Container, Box, Typography, Avatar, Alert, Button } from "@mui/material";
import ApiService from "../../service/ApiService";

const Profile = () => {
  const [profile, setProfile] = useState(null);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const data = await ApiService.fetchUserProfile();
        console.log("API Response:", data);
    
        if (data && data.user) {
          setProfile(data.user); // Fetch user data
        } else {
          console.error("Unexpected API response format:", data);
          throw new Error("Invalid response format");
        }
      } catch (err) {
        console.error("Failed to fetch profile data:", err.message);
        setError("Failed to load profile. Please try again.");
      }
    };
  
    fetchProfile();
  }, []);

  const handleEditPsychologistProfile = () => {
    // Redirect to edit psychologist profile page
    navigate("/profile/edit-psychologist");
  };

  if (error) {
    return (
      <Container>
        <Alert severity="error">{error}</Alert>
      </Container>
    );
  }

  if (!profile) {
    return (
      <Container>
        <Typography variant="h5" align="center" sx={{ mt: 4 }}>
          Loading profile...
        </Typography>
      </Container>
    );
  }

  return (
    <Container>
      <Box
        sx={{
          mt: 4,
          p: 3,
          boxShadow: 3,
          borderRadius: 2,
          backgroundColor: "white",
        }}
      >
        <Box sx={{ textAlign: "center", mb: 4 }}>
          <Avatar
            src={profile.profilePhotoUrl || ""}
            alt={profile.firstName}
            sx={{ width: 100, height: 100, mx: "auto" }}
          />
          <Typography variant="h5" sx={{ mt: 2 }}>
            {profile.firstName} {profile.lastName}
          </Typography>
          <Typography variant="body1" color="text.secondary">
            Role: {profile.role}
          </Typography>
        </Box>
        <Typography variant="body1">
          <strong>Email:</strong> {profile.email}
        </Typography>
        <Typography variant="body1">
          <strong>Phone Number:</strong> {profile.phoneNumber}
        </Typography>
        <Typography variant="body1">
          <strong>Telegram Nickname:</strong> {profile.telegramNickname || "N/A"}
        </Typography>
        <Typography variant="body1">
          <strong>Birth Date:</strong> {profile.birthDate}
        </Typography>
        <Typography variant="body1">
          <strong>Gender:</strong> {profile.gender}
        </Typography>
        <Typography variant="body1">
          <strong>Age:</strong> {profile.age}
        </Typography>

        {profile.role === "PSYCHOLOGIST" && (
          <Button
            variant="contained"
            color="primary"
            sx={{ mt: 3 }}
            onClick={handleEditPsychologistProfile}
          >
            Edit Psychologist Profile
          </Button>
        )}
      </Box>
    </Container>
  );
};

export default Profile;
