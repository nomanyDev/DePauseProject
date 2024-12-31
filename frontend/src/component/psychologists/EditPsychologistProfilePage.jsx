import React, { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import {
  Container,
  TextField,
  Button,
  Box,
  Typography,
  Alert,
  FormGroup,
  FormControlLabel,
  Checkbox,
  CircularProgress,
} from "@mui/material";
import ApiService from "../../service/ApiService";

const therapyTypeMapping = {
  "Individual Therapy": "INDIVIDUAL",
  "Family Therapy": "FAMILY",
  "Couple Therapy": "COUPLE",
  "Child Therapy": "CHILD",
};

const reverseTherapyTypeMapping = Object.fromEntries(
  Object.entries(therapyTypeMapping).map(([key, value]) => [value, key])
);

const EditPsychologistPage = () => {
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const psychologistId = queryParams.get("psychologistId");

  const [psychologistData, setPsychologistData] = useState(null);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchPsychologistData = async () => {
      if (!psychologistId) {
        setError("Psychologist ID is missing.");
        setLoading(false);
        return;
      }

      try {
        const data = await ApiService.getPsychologistById(psychologistId);
        setPsychologistData({
          education: data.psychologist.education || "",
          experience: data.psychologist.experience || "",
          therapyTypes: (data.psychologist.therapyTypes || []).map(
            (type) => reverseTherapyTypeMapping[type] || type
          ),
          price: data.psychologist.price || "",
          description: data.psychologist.description || "",
        });
        setLoading(false);
      } catch (err) {
        console.error("Error fetching psychologist data:", err.message);
        setError("Failed to load profile data.");
        setLoading(false);
      }
    };

    fetchPsychologistData();
  }, [psychologistId]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setPsychologistData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleTherapyTypeToggle = (therapyType) => {
    setPsychologistData((prevData) => {
      const isSelected = prevData.therapyTypes.includes(therapyType);
      const updatedTherapyTypes = isSelected
        ? prevData.therapyTypes.filter((type) => type !== therapyType)
        : [...prevData.therapyTypes, therapyType];
      const validTherapyTypes = updatedTherapyTypes.filter(
        (type) => therapyTypeMapping[type]
      );
      return {
        ...prevData,
        therapyTypes: validTherapyTypes,
      };
    });
  };

  const handleSubmit = async () => {
    if (!psychologistData) {
      setError("No data to submit.");
      return;
    }

    try {
      const updatedData = {
        education: psychologistData.education,
        experience: psychologistData.experience,
        therapyTypes: psychologistData.therapyTypes.map(
          (type) => therapyTypeMapping[type]
        ),
        price: psychologistData.price,
        description: psychologistData.description,
      };

      await ApiService.editPsychologistProfile(updatedData);
      setSuccess(true);
      setError("");
    } catch (err) {
      console.error("Error updating psychologist profile:", err.message);
      setError(
        err.response?.data?.message || "Failed to update profile. Please try again."
      );
      setSuccess(false);
    }
  };

  if (loading) {
    return (
      <Container>
        <Box sx={{ textAlign: "center", mt: 5 }}>
          <CircularProgress />
        </Box>
      </Container>
    );
  }

  if (!psychologistData) {
    return (
      <Container>
        <Typography variant="h6" color="error" sx={{ textAlign: "center", mt: 5 }}>
          {error || "No data available."}
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
        <Typography variant="h4" gutterBottom>
          Edit Psychologist Profile
        </Typography>
        {error && <Alert severity="error">{error}</Alert>}
        {success && <Alert severity="success">Profile updated successfully!</Alert>}
        <TextField
          fullWidth
          margin="normal"
          label="Education"
          name="education"
          value={psychologistData.education}
          onChange={handleChange}
        />
        <TextField
          fullWidth
          margin="normal"
          label="Experience"
          name="experience"
          value={psychologistData.experience}
          onChange={handleChange}
        />
        <TextField
          fullWidth
          margin="normal"
          label="Price"
          name="price"
          type="number"
          value={psychologistData.price}
          onChange={handleChange}
        />
        <TextField
          fullWidth
          margin="normal"
          label="Description"
          name="description"
          multiline
          rows={4}
          value={psychologistData.description}
          onChange={handleChange}
        />
        <Box sx={{ mt: 2 }}>
          <Typography variant="h6">Therapy Types</Typography>
          <FormGroup row>
            {Object.keys(therapyTypeMapping).map((therapyType) => (
              <FormControlLabel
                key={therapyType}
                control={
                  <Checkbox
                    checked={psychologistData.therapyTypes.includes(therapyType)}
                    onChange={() => handleTherapyTypeToggle(therapyType)}
                  />
                }
                label={therapyType}
              />
            ))}
          </FormGroup>
        </Box>
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

export default EditPsychologistPage;

