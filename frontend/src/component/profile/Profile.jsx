import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  Container,
  Box,
  Typography,
  Avatar,
  Grid,
  CircularProgress,
  Button,
  Rating,
  Card,
  CardContent,
  Chip,
} from "@mui/material";
import SchoolIcon from "@mui/icons-material/School";
import WorkIcon from "@mui/icons-material/Work";
import MonetizationOnIcon from "@mui/icons-material/MonetizationOn";
import StarIcon from "@mui/icons-material/Star";
import EmailIcon from "@mui/icons-material/Email";
import PhoneIcon from "@mui/icons-material/Phone";
import TelegramIcon from "@mui/icons-material/Telegram";
import CakeIcon from "@mui/icons-material/Cake";
import WcIcon from "@mui/icons-material/Wc";
import CalendarMonthIcon from "@mui/icons-material/CalendarMonth";
import EditIcon from "@mui/icons-material/Edit";
import GroupIcon from "@mui/icons-material/Group";
import ChildCareIcon from "@mui/icons-material/ChildCare";
import PeopleIcon from "@mui/icons-material/People";
import PersonIcon from "@mui/icons-material/Person";
import ApiService from "../../service/ApiService";
import PsychologistPagination from "../common/Pagination";

const therapyIcons = {
  INDIVIDUAL: <PersonIcon />,
  FAMILY: <GroupIcon />,
  COUPLE: <PeopleIcon />,
  CHILD: <ChildCareIcon />,
};

const Profile = () => {
  const [profile, setProfile] = useState(null);
  const [psychologist, setPsychologist] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [reviewLoading, setReviewLoading] = useState(true);
  const [error, setError] = useState("");
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const data = await ApiService.fetchUserProfile();
        if (data && data.user) {
          setProfile(data.user);
          if (data.user.role === "PSYCHOLOGIST" && data.psychologist) {
            setPsychologist(data.psychologist);
            fetchReviews(data.psychologist.id);
          }
        } else {
          throw new Error("Invalid response format");
        }
        setLoading(false);
      } catch (err) {
        setError("Failed to load profile. Please try again.");
        setLoading(false);
      }
    };

    const fetchReviews = async (psychologistId) => {
      try {
        setReviewLoading(true);
        const response = await ApiService.getPsychologistReviews(psychologistId, page, 5);
        setReviews(response.page.content || []);
        setTotalPages(response.page.totalPages || 0);
        setReviewLoading(false);
      } catch (error) {
        setReviewLoading(false);
        console.error("Error fetching reviews:", error.message);
      }
    };

    fetchProfile();
  }, [page]);

  const handlePageChange = (page) => {
    setPage(page);
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

  return (
    <Container>
      {profile ? (
        <Card sx={{ display: "flex", flexDirection: { xs: "column", md: "row" }, p: 3, boxShadow: 3, mt: 4 }}>
          {/* profile photo */}
          <Box
            sx={{
              flexShrink: 0,
              maxWidth: { xs: "100%", md: "40%" },
              mb: { xs: 2, md: 0 },
              position: "relative",
            }}
          >
            <Avatar
              src={profile.profilePhotoUrl || ""}
              alt={`${profile.firstName} ${profile.lastName}`}
              variant="square"
              sx={{
                width: "100%",
                height: "auto",
                maxHeight: "300px",
                borderRadius: 2,
                boxShadow: 2,
                objectFit: "cover",
              }}
            />
          </Box>

          {/* user info */}
          <Box sx={{ flexGrow: 1, ml: { md: 3 }, position: "relative" }}>
            {psychologist && (
              <>
                <Button
                  variant="contained"
                  startIcon={<EditIcon />}
                  color="secondary"
                  sx={{
                    position: "absolute",
                    top: 0,
                    right: 0,
                    fontWeight: "bold",
                    backgroundColor: "#3dcfdf",
                    color: "white",
                    "&:hover": {
                      backgroundColor: "#3dbedf",
                    },
                  }}
                  onClick={() => navigate(`/profile/edit-psychologist?psychologistId=${psychologist.id}`)} 
                >
                  Edit Details
                </Button>
                <Button
                  variant="contained"
                  color="primary"
                  sx={{
                    position: "absolute",
                    top: 50,
                    right: 0,
                    fontWeight: "bold",
                  }}
                  onClick={() => navigate(`/profile/manage-availability?psychologistId=${psychologist.id}`)}
                >
                  Manage Availability
                </Button>
              </>
            )}
            <Typography variant="h4" gutterBottom>
              {profile.firstName} {profile.lastName}
            </Typography>
            <Typography variant="body1" color="text.secondary" sx={{ mb: 2 }}>
              Role: {profile.role}
            </Typography>

            <Grid container spacing={2}>
              <Grid item xs={12} sm={6}>
                <Box sx={{ display: "flex", alignItems: "center", mb: 1 }}>
                  <EmailIcon color="primary" sx={{ mr: 1 }} />
                  <Typography variant="body1">
                    <strong>Email:</strong> {profile.email}
                  </Typography>
                </Box>
              </Grid>
              <Grid item xs={12} sm={6}>
                <Box sx={{ display: "flex", alignItems: "center", mb: 1 }}>
                  <PhoneIcon color="primary" sx={{ mr: 1 }} />
                  <Typography variant="body1">
                    <strong>Phone Number:</strong> {profile.phoneNumber}
                  </Typography>
                </Box>
              </Grid>
              <Grid item xs={12} sm={6}>
                <Box sx={{ display: "flex", alignItems: "center", mb: 1 }}>
                  <TelegramIcon color="primary" sx={{ mr: 1 }} />
                  <Typography variant="body1">
                    <strong>Telegram Nickname:</strong> {profile.telegramNickname || "N/A"}
                  </Typography>
                </Box>
              </Grid>
              <Grid item xs={12} sm={6}>
                <Box sx={{ display: "flex", alignItems: "center", mb: 1 }}>
                  <CakeIcon color="primary" sx={{ mr: 1 }} />
                  <Typography variant="body1">
                    <strong>Birth Date:</strong> {profile.birthDate}
                  </Typography>
                </Box>
              </Grid>
              <Grid item xs={12} sm={6}>
                <Box sx={{ display: "flex", alignItems: "center", mb: 1 }}>
                  <WcIcon color="primary" sx={{ mr: 1 }} />
                  <Typography variant="body1">
                    <strong>Gender:</strong> {profile.gender}
                  </Typography>
                </Box>
              </Grid>
              <Grid item xs={12} sm={6}>
                <Box sx={{ display: "flex", alignItems: "center", mb: 1 }}>
                  <CalendarMonthIcon color="primary" sx={{ mr: 1 }} />
                  <Typography variant="body1">
                    <strong>Age:</strong> {profile.age}
                  </Typography>
                </Box>
              </Grid>
            </Grid>

            {psychologist && (
              <Grid container spacing={2}>
                <Grid item xs={12}>
                  <Box sx={{ mt: 4 }}>
                    <Typography variant="h5" gutterBottom>
                      Psychologist Details
                    </Typography>
                    <Grid container spacing={2}>
                      <Grid item xs={12} sm={6}>
                        <Box sx={{ display: "flex", alignItems: "center", mb: 1 }}>
                          <SchoolIcon color="primary" sx={{ mr: 1 }} />
                          <Typography variant="body1">
                            <strong>Education:</strong> {psychologist.education || "Not specified"}
                          </Typography>
                        </Box>
                      </Grid>
                      <Grid item xs={12} sm={6}>
                        <Box sx={{ display: "flex", alignItems: "center", mb: 1 }}>
                          <WorkIcon color="primary" sx={{ mr: 1 }} />
                          <Typography variant="body1">
                            <strong>Experience:</strong> {psychologist.experience || "Not specified"} years
                          </Typography>
                        </Box>
                      </Grid>
                      <Grid item xs={12} sm={6}>
                        <Box sx={{ display: "flex", alignItems: "center", mb: 1 }}>
                          <MonetizationOnIcon color="primary" sx={{ mr: 1 }} />
                          <Typography variant="body1">
                            <strong>Price:</strong> ${psychologist.price || "N/A"}
                          </Typography>
                        </Box>
                      </Grid>
                      <Grid item xs={12} sm={6}>
                        <Box sx={{ display: "flex", alignItems: "center", mb: 1 }}>
                          <StarIcon color="primary" sx={{ mr: 1 }} />
                          <Typography variant="body1">
                            <strong>Rating:</strong> {psychologist.rating || "No rating available"} / 5
                          </Typography>
                        </Box>
                      </Grid>
                    </Grid>
                    {/* Therapy Types */}
                    <Box sx={{ mt: 3 }}>
                      <Typography variant="h6" gutterBottom>
                        Therapy Types
                      </Typography>
                      {psychologist.therapyTypes?.map((type) => (
                        <Chip
                          key={type}
                          icon={therapyIcons[type]}
                          label={type}
                          color="primary"
                          sx={{ mr: 1, mb: 1 }}
                        />
                      ))}
                    </Box>
                  </Box>
                </Grid>
                {/* Description */}
                <Grid item xs={12}>
                  <Box sx={{ mt: 4 }}>
                    <Typography variant="h5" gutterBottom>
                      Description
                    </Typography>
                    <Typography variant="body1">
                      {psychologist.description || "Not specified"}
                    </Typography>
                  </Box>
                </Grid>
              </Grid>
            )}
          </Box>
        </Card>
      ) : (
        <Typography variant="h6" color="error" sx={{ textAlign: "center" }}>
          Profile not found.
        </Typography>
      )}

      {/* reviews */}
      {psychologist && (
        <Box sx={{ mt: 5 }}>
          <Typography variant="h5" gutterBottom>
            Reviews
          </Typography>
          {reviewLoading ? (
            <CircularProgress />
          ) : reviews.length > 0 ? (
            reviews.map((review) => (
              <Card key={review.id} sx={{ mb: 2 }}>
                <CardContent>
                  <Typography variant="h6">{review.content}</Typography>
                  <Typography variant="body2" color="text.secondary">
                    Rating: {review.rating}/5
                  </Typography>
                  <Typography variant="caption" color="text.secondary">
                    User ID: {review.userId}
                  </Typography>
                </CardContent>
              </Card>
            ))
          ) : (
            <Typography>No reviews available.</Typography>
          )}
          <Box sx={{ mt: 2 }}>
            <PsychologistPagination
              totalPages={totalPages}
              currentPage={page}
              onPageChange={handlePageChange}
            />
          </Box>
        </Box>
      )}
    </Container>
  );
};

export default Profile;
