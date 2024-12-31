import React from "react";
import { Box, Card, CardContent, Typography, Button, Grid, Avatar } from "@mui/material";
import { useNavigate } from "react-router-dom";

const PsychologistList = ({ psychologists }) => {
  const navigate = useNavigate();

  return (
    <Grid container spacing={3}>
      {psychologists.map((psychologist) => (
        <Grid item xs={12} sm={6} md={4} key={psychologist.id}>
          <Card sx={{ boxShadow: 3, borderRadius: 2, overflow: "hidden" }}>
            {/* photo */}
            <Box sx={{ display: "flex", justifyContent: "center", mt: 2 }}>
              <Avatar
                src={psychologist.profilePhotoUrl || "/default-avatar.png"}
                alt={`${psychologist.firstName || "Name not specified"} ${psychologist.lastName || ""}`}
                sx={{ width: 100, height: 100 }}
              />
            </Box>
            <CardContent sx={{ textAlign: "center" }}>
              {/* name */}
              <Typography variant="h6">
                {psychologist.firstName && psychologist.lastName
                  ? `${psychologist.firstName} ${psychologist.lastName}`
                  : "Name not specified"}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Education: {psychologist.education || "Not specified"}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Experience: {psychologist.experience || "Not specified"}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Therapy Types: {psychologist.therapyTypes?.join(", ") || "Not specified"}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Price: ${psychologist.price || "N/A"}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Rating: {psychologist.rating || "No Ratings Yet"}
              </Typography>
              <Button
                variant="contained"
                color="primary"
                sx={{ mt: 2 }}
                onClick={() => navigate(`/psychologists/${psychologist.id}`)}
              >
                View Details
              </Button>
            </CardContent>
          </Card>
        </Grid>
      ))}
    </Grid>
  );
};

export default PsychologistList;
