import React, { useState } from "react";
import {
  Container,
  TextField,
  Button,
  Checkbox,
  FormControlLabel,
  Typography,
  Alert,
  Box,
  FormControl,
  FormLabel,
  RadioGroup,
  Radio,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import ApiService from "../../service/ApiService";

function RegisterPage() {
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    gender: "",
    birthDate: "",
    phoneNumber: "",
    telegramNickname: "",
    role: "USER",
  });

  const [isPsychologist, setIsPsychologist] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const navigate = useNavigate();

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleCheckboxChange = () => {
    const newRole = isPsychologist ? "USER" : "PSYCHOLOGIST";
    setIsPsychologist(!isPsychologist);
    setFormData({ ...formData, role: newRole });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (
      !formData.firstName ||
      !formData.lastName ||
      !formData.email ||
      !formData.password ||
      !formData.phoneNumber ||
      !formData.gender ||
      !formData.birthDate
    ) {
      setError("Please fill in all required fields.");
      setTimeout(() => setError(""), 5000);
      return;
    }

    console.log("Submitting data:", formData);

    try {
      const endpoint = isPsychologist
        ? ApiService.registerPsychologist
        : ApiService.registerUser;

      const response = await endpoint(formData);

      if (response.statusCode === 200) {
        setSuccess(
          `Registration successful! ${
            isPsychologist
              ? "Redirecting to psychologist profile setup..."
              : "Redirecting to login..."
          }`
        );
        setTimeout(() => {
          if (isPsychologist) {
            navigate("/profile/edit-psychologist");
          } else {
            navigate("/login");
          }
        }, 2000);
      } else {
        setError("Registration failed. Please try again.");
      }
    } catch (err) {
      console.error("Error during registration:", err);
      setError(err.response?.data?.message || "Something went wrong.");
    }
  };

  return (
    <Container maxWidth="sm">
      <Box
        sx={{
          mt: 4,
          p: 3,
          boxShadow: 3,
          borderRadius: 2,
          backgroundColor: "white",
        }}
      >
        <Typography variant="h4" gutterBottom align="center">
          Register
        </Typography>
        {error && <Alert severity="error">{error}</Alert>}
        {success && <Alert severity="success">{success}</Alert>}
        <form onSubmit={handleSubmit}>
          <TextField
            fullWidth
            margin="normal"
            label="First Name"
            name="firstName"
            value={formData.firstName}
            onChange={handleInputChange}
            required
          />
          <TextField
            fullWidth
            margin="normal"
            label="Last Name"
            name="lastName"
            value={formData.lastName}
            onChange={handleInputChange}
            required
          />
          <TextField
            fullWidth
            margin="normal"
            label="Email"
            name="email"
            type="email"
            value={formData.email}
            onChange={handleInputChange}
            required
          />
          <TextField
            fullWidth
            margin="normal"
            label="Password"
            name="password"
            type="password"
            value={formData.password}
            onChange={handleInputChange}
            required
          />
          <TextField
            fullWidth
            margin="normal"
            label="Phone Number"
            name="phoneNumber"
            value={formData.phoneNumber}
            onChange={handleInputChange}
            required
          />
          <TextField
            fullWidth
            margin="normal"
            label="Telegram Nickname (optional)"
            name="telegramNickname"
            value={formData.telegramNickname}
            onChange={handleInputChange}
          />
          <FormControl component="fieldset" margin="normal">
            <FormLabel component="legend">Gender</FormLabel>
            <RadioGroup
              row
              name="gender"
              value={formData.gender}
              onChange={handleInputChange}
            >
              <FormControlLabel
                value="MALE"
                control={<Radio color="primary" />}
                label="Male"
              />
              <FormControlLabel
                value="FEMALE"
                control={<Radio color="primary" />}
                label="Female"
              />
            </RadioGroup>
          </FormControl>
          <TextField
            fullWidth
            margin="normal"
            label="Birth Date"
            name="birthDate"
            type="date"
            InputLabelProps={{
              shrink: true,
            }}
            value={formData.birthDate || ""}
            onChange={handleInputChange}
            required
          />
          <FormControlLabel
            control={
              <Checkbox
                checked={isPsychologist}
                onChange={handleCheckboxChange}
                color="primary"
              />
            }
            label="I am a psychologist"
          />
          <Button
            type="submit"
            fullWidth
            variant="contained"
            color="primary"
            sx={{ mt: 2 }}
          >
            Sign Up
          </Button>
        </form>
        <Typography
          variant="body2"
          align="center"
          sx={{ mt: 2, color: "text.secondary" }}
        >
          Already have an account?{" "}
          <Button
            onClick={() => navigate("/login")}
            variant="text"
            color="primary"
          >
            Login
          </Button>
        </Typography>
      </Box>
    </Container>
  );
}

export default RegisterPage;
