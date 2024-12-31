import React, { useState } from "react";
import { Box, TextField, Button, MenuItem, Typography, Alert } from "@mui/material";
import ApiService from "../../service/ApiService";

const SupportPage = () => {
  const [formData, setFormData] = useState({
    email: "",
    problemType: "",
    description: "",
  });
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const problemTypes = [
    "Technical Issue",
    "Appointment Problem",
    "Register/Login Problem",
    "Other",
  ];

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!formData.email || !formData.problemType || !formData.description) {
        setErrorMessage("Please fill in all fields.");
        setTimeout(() => setErrorMessage(""), 5000);
        return;
    }

    try {
        console.log("Sending support request:", formData); // Добавьте логирование
        await ApiService.sendSupportRequest(formData);
        setSuccessMessage("Your support request has been sent successfully.");
        setErrorMessage("");
        setFormData({ email: "", problemType: "", description: "" });
    } catch (err) {
        console.error("Error sending support request:", err.response || err.message);
        setErrorMessage("Failed to send support request. Please try again.");
    }
};

  return (
    <Box
      sx={{
        maxWidth: 600,
        mx: "auto",
        mt: 4,
        p: 3,
        boxShadow: 3,
        borderRadius: 2,
        backgroundColor: "white",
      }}
    >
      <Typography variant="h4" gutterBottom align="center">
        Contact Support
      </Typography>

      {successMessage && <Alert severity="success">{successMessage}</Alert>}
      {errorMessage && <Alert severity="error">{errorMessage}</Alert>}

      <form onSubmit={handleSubmit}>
        <TextField
          fullWidth
          margin="normal"
          label="Email Address"
          name="email"
          type="email"
          value={formData.email}
          onChange={handleInputChange}
          required
        />

        <TextField
          fullWidth
          select
          margin="normal"
          label="Problem Type"
          name="problemType"
          value={formData.problemType}
          onChange={handleInputChange}
          required
        >
          {problemTypes.map((type) => (
            <MenuItem key={type} value={type}>
              {type}
            </MenuItem>
          ))}
        </TextField>

        <TextField
          fullWidth
          margin="normal"
          label="Description"
          name="description"
          multiline
          rows={4}
          value={formData.description}
          onChange={handleInputChange}
          required
        />

        <Button
          type="submit"
          fullWidth
          variant="contained"
          color="primary"
          sx={{ mt: 2 }}
        >
          Submit
        </Button>
      </form>
    </Box>
  );
};

export default SupportPage;
