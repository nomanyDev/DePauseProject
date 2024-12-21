import React, { useState, useEffect } from "react";
import {
  Container,
  TextField,
  Button,
  Box,
  Typography,
  Alert,
  Avatar,
} from "@mui/material";
import ApiService from "../../service/ApiService";

const EditProfilePage = () => {
  const [profile, setProfile] = useState({
    id: "",
    firstName: "",
    lastName: "",
    phoneNumber: "",
    email: "",
    birthDate: "",
    role: "",
    profilePhotoUrl: "",
  });
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [profilePhoto, setProfilePhoto] = useState(null); // Для загрузки файла

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const data = await ApiService.getUserProfile();
        console.log("Fetched profile data:", data);
        if (data?.user) {
          setProfile({
            id: data.user.id,
            firstName: data.user.firstName || "",
            lastName: data.user.lastName || "",
            phoneNumber: data.user.phoneNumber || "",
            email: data.user.email || "",
            birthDate: data.user.birthDate || "",
            role: data.user.role || "",
            profilePhotoUrl: data.user.profilePhotoUrl || "",
          });
        } else {
          throw new Error("Invalid profile data format");
        }
      } catch (err) {
        console.error("Error fetching profile:", err.message);
        setError("Failed to load profile. Please try again.");
      }
    };

    fetchProfile();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setProfile({ ...profile, [name]: value });
  };

  const handleFileChange = (e) => {
    setProfilePhoto(e.target.files[0]); // Сохраняем файл в состоянии
  };

  const handleSubmit = async () => {
    if (!profile.firstName || !profile.lastName || !profile.phoneNumber || !profile.email) {
      setError("Please fill in all required fields.");
      return;
    }

    try {
      await ApiService.editUser(profile.id, profile);
      setSuccess("Profile updated successfully!");
      setError("");
    } catch (err) {
      console.error("Error updating profile:", err.message);
      setError(err.response?.data?.message || "Failed to update profile. Please try again.");
      setSuccess("");
    }

    // Если фото выбрано, загружаем его
    if (profilePhoto) {
      try {
        const photoResponse = await ApiService.uploadProfilePhoto(profile.id, profilePhoto);
        setProfile({ ...profile, profilePhotoUrl: photoResponse.photoUrl }); // Обновляем URL фото в профиле
        setSuccess("Profile photo uploaded successfully!");
        setError("");
      } catch (err) {
        console.error("Error uploading profile photo:", err.message);
        setError("Failed to upload profile photo. Please try again.");
        setSuccess("");
      }
    }
  };

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
        <Typography variant="h4" gutterBottom>
          Edit Profile
        </Typography>
        {error && <Alert severity="error">{error}</Alert>}
        {success && <Alert severity="success">{success}</Alert>}

        <Box sx={{ textAlign: "center", mb: 4 }}>
          <Avatar
            src={profile.profilePhotoUrl || ""}
            alt="Profile Photo"
            sx={{ width: 100, height: 100, mx: "auto" }}
          />
          <input
            type="file"
            accept="image/*"
            onChange={handleFileChange}
            style={{ marginTop: "1rem" }}
          />
        </Box>

        <TextField
          fullWidth
          margin="normal"
          label="First Name"
          name="firstName"
          value={profile.firstName}
          onChange={handleChange}
          required
        />
        <TextField
          fullWidth
          margin="normal"
          label="Last Name"
          name="lastName"
          value={profile.lastName}
          onChange={handleChange}
          required
        />
        <TextField
          fullWidth
          margin="normal"
          label="Phone Number"
          name="phoneNumber"
          value={profile.phoneNumber}
          onChange={handleChange}
          required
        />
        <TextField
          fullWidth
          margin="normal"
          label="Email"
          name="email"
          value={profile.email}
          onChange={handleChange}
          required
        />
        <TextField
          fullWidth
          margin="normal"
          label="Birth Date"
          name="birthDate"
          type="date"
          InputLabelProps={{
            shrink: true,
          }}
          value={profile.birthDate}
          onChange={handleChange}
        />
        <TextField
          fullWidth
          margin="normal"
          label="Role"
          name="role"
          value={profile.role}
          onChange={handleChange}
          disabled
        />
        <Button
          variant="contained"
          color="primary"
          sx={{ mt: 3 }}
          onClick={handleSubmit}
        >
          Save Changes
        </Button>
      </Box>
    </Container>
  );
};

export default EditProfilePage;
