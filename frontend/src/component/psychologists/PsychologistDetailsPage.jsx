import React, { useState, useEffect } from "react";
import { Container, Typography, Box, Pagination } from "@mui/material";
import ApiService from "../../service/ApiService";
import PsychologistList from "./PsychologistList";

const PsychologistsPage = () => {
  const [psychologists, setPsychologists] = useState([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);

  useEffect(() => {
    const fetchPsychologists = async () => {
      try {
        const response = await ApiService.getPsychologistsList(page - 1, 10); // page - 1, так как API использует нумерацию с 0
        setPsychologists(response.psychologists);
        setTotalPages(response.totalPages);
      } catch (err) {
        console.error("Error fetching psychologists:", err.message);
      }
    };

    fetchPsychologists();
  }, [page]);

  const handlePageChange = (event, value) => {
    setPage(value);
  };

  return (
    <Container>
      <Box sx={{ mt: 4 }}>
        <Typography variant="h4" gutterBottom>
          Psychologists
        </Typography>
        <PsychologistList psychologists={psychologists} />
        <Box sx={{ mt: 3, display: "flex", justifyContent: "center" }}>
          <Pagination
            count={totalPages}
            page={page}
            onChange={handlePageChange}
            color="primary"
          />
        </Box>
      </Box>
    </Container>
  );
};

export default PsychologistsPage;
