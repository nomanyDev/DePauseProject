import React from "react";
import { Box, Container, Typography, Link, IconButton } from "@mui/material";
import LinkedInIcon from "@mui/icons-material/LinkedIn";
import SchoolIcon from "@mui/icons-material/School";
import GitHubIcon from "@mui/icons-material/GitHub";
import ApiIcon from "@mui/icons-material/Api"; 

function Footer() {
  // URL Swagger
  const swaggerUrl = "http://ec2-44-207-6-108.compute-1.amazonaws.com:8080/swagger-ui/index.html";

  
  const isServer =
    window.location.hostname === "ec2-44-207-6-108.compute-1.amazonaws.com" ||
    window.location.hostname === "44.207.6.108";

  return (
    <Box
      sx={{
        backgroundImage: "linear-gradient(to top, rgba(0, 153, 153, 0.7), rgba(204, 245, 255, 0.7))",
        backdropFilter: "blur(10px)",
        color: "white",
        py: 2,
        mt: 4,
      }}
    >
      <Container maxWidth="lg">
        <Box
          sx={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
          }}
        >
          <Typography variant="body2" sx={{ textAlign: "left" }}>
            DePause Appointment System | All Rights Reserved &copy;{" "}
            {new Date().getFullYear()}
          </Typography>

          <Box sx={{ display: "flex", gap: 2 }}>
            <Link
              href="https://www.linkedin.com/in/nomany/"
              target="_blank"
              rel="noopener noreferrer"
              sx={{ color: "white" }}
            >
              <IconButton sx={{ color: "white" }}>
                <LinkedInIcon fontSize="large" />
              </IconButton>
            </Link>
            <Link
              href="https://www.ncirl.ie/"
              target="_blank"
              rel="noopener noreferrer"
              sx={{ color: "white" }}
            >
              <IconButton sx={{ color: "white" }}>
                <SchoolIcon fontSize="large" />
              </IconButton>
            </Link>
            <Link
              href="https://github.com/nomanyDev/DePauseProject"
              target="_blank"
              rel="noopener noreferrer"
              sx={{ color: "white" }}
            >
              <IconButton sx={{ color: "white" }}>
                <GitHubIcon fontSize="large" />
              </IconButton>
            </Link>
            {isServer && (
              <Link
                href={swaggerUrl}
                target="_blank"
                rel="noopener noreferrer"
                sx={{ color: "white" }}
              >
                <IconButton sx={{ color: "white" }}>
                  <ApiIcon fontSize="large" />
                </IconButton>
              </Link>
            )}
          </Box>
        </Box>
      </Container>
    </Box>
  );
}

export default Footer;
