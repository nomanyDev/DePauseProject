import React, { useState, useEffect } from "react";
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

const EditPsychologistPage = ({ psychologistId }) => {
  const [psychologistData, setPsychologistData] = useState({
    education: "",
    experience: "",
    therapyTypes: [],
    price: "",
    rating: "",
    description: "",
    certificateUrls: [],
  });

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  // Fetch psychologist data on mount
  useEffect(() => {
    const fetchPsychologistData = async () => {
      try {
        const data = await ApiService.getPsychologistById(psychologistId);
        setPsychologistData({
          education: data.education || "",
          experience: data.experience || "",
          therapyTypes: (data.therapyTypes || []).map(
            (type) => reverseTherapyTypeMapping[type] // Convert ENUM to display string
          ),
          price: data.price || "",
          rating: data.rating || "",
          description: data.description || "",
          certificateUrls: data.certificateUrls || [],
        });
      } catch (err) {
        console.error("Error fetching psychologist data:", err.message);
        
      }
    };

    fetchPsychologistData();
  }, [psychologistId]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setPsychologistData({ ...psychologistData, [name]: value });
  };

  const handleTherapyTypeToggle = (therapyType) => {
    const selectedType = therapyTypeMapping[therapyType]; // Convert display string to ENUM
    setPsychologistData((prevData) => {
      const isAlreadySelected = prevData.therapyTypes.includes(therapyType);
      const updatedTypes = isAlreadySelected
        ? prevData.therapyTypes.filter((type) => type !== therapyType)
        : [...prevData.therapyTypes, therapyType];

      return {
        ...prevData,
        therapyTypes: updatedTypes,
      };
    });
  };

  const handleSubmit = async () => {
    try {
        // Преобразуем therapyTypes в значения ENUM
        const updatedData = {
            ...psychologistData,
            therapyTypes: psychologistData.therapyTypes.map(
                (type) => therapyTypeMapping[type] // Конвертируем строки в формат ENUM
            ),
        };

        console.log("Data sent to API:", updatedData); // Лог для проверки
        await ApiService.editPsychologistProfile(updatedData);
        setSuccess("Psychologist profile updated successfully!");
        setError("");
    } catch (err) {
        console.error("Error updating psychologist profile:", err.message);
        setError(
            err.response?.data?.message || "Failed to update profile. Please try again."
        );
        setSuccess("");
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
          Edit Psychologist Profile
        </Typography>
        {error && <Alert severity="error">{error}</Alert>}
        {success && <Alert severity="success">{success}</Alert>}
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
        {/*<Box sx={{ mt: 2 }}>
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
        </Box> */}
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

