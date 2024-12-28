import React from "react";
import { Pagination, Box } from "@mui/material";

const PsychologistPagination = ({ totalPages, currentPage, onPageChange }) => {
  const handlePageChange = (event, value) => {
    onPageChange(value - 1); 
  };

  return (
    <Box sx={{ display: "flex", justifyContent: "center", mt: 2 }}>
      <Pagination
        count={totalPages}
        page={currentPage + 1} 
        onChange={handlePageChange}
        color="primary"
      />
    </Box>
  );
};

export default PsychologistPagination;
