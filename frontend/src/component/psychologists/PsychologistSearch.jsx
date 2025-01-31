

import React, { useState, useEffect } from "react";
import { TextField, MenuItem, Button, Box } from "@mui/material";
import { DatePicker } from "@mui/x-date-pickers";
import { useNavigate } from "react-router-dom";
import ApiService from "../../service/ApiService";

const PsychologistSearch = () => {
  const [therapyType, setTherapyType] = useState("");
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);
  const [therapyTypes, setTherapyTypes] = useState([]);
  const [error, setError] = useState("");
  const navigate = useNavigate(); 

  useEffect(() => {
    setTherapyTypes([
      "Individual Therapy",
      "Family Therapy",
      "Couple Therapy",
      "Child Therapy",
    ]); 
  }, []);

  const handleSearch = async () => {
    if (!therapyType || !startDate || !endDate) {
      setError("Please fill in all fields");
      setTimeout(() => setError(""), 5000);
      return;
    }

    try {
      const results = await ApiService.searchPsychologists({
        therapyType,
        startDate: startDate.toISOString().split("T")[0],
        endDate: endDate.toISOString().split("T")[0],
      });
      
      
      navigate("/psychologists", { state: { searchResults: results.psychologistList } });
    } catch (err) {
      setError(err.response?.data?.message || "Error during search");
    }
  };

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        gap: 2,
        maxWidth: 600,
        mx: "auto",
        mt: 4,
      }}
    >
      <TextField
        select
        label="Therapy Type"
        value={therapyType}
        onChange={(e) => setTherapyType(e.target.value)}
        fullWidth
      >
        {therapyTypes.map((type) => (
          <MenuItem key={type} value={type}>
            {type}
          </MenuItem>
        ))}
      </TextField>

      <DatePicker
        label="Start Date"
        value={startDate}
        onChange={(date) => setStartDate(date)}
        renderInput={(params) => <TextField {...params} />}
      />

      <DatePicker
        label="End Date"
        value={endDate}
        onChange={(date) => setEndDate(date)}
        renderInput={(params) => <TextField {...params} />}
      />

      {error && <p style={{ color: "red" }}>{error}</p>}

      <Button variant="contained" color="primary" onClick={handleSearch}>
        Search
      </Button>
    </Box>
  );
};

export default PsychologistSearch;

