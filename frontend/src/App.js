import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { ThemeProvider, createTheme } from "@mui/material/styles";
import { CssBaseline, Box } from "@mui/material";
import { LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs"; 
import Navbar from "./component/common/Navbar";
import Home from "./component/home/Home";
import LoginPage from "./component/auth/LoginPage";
import RegisterPage from "./component/auth/RegisterPage";
import Footer from "./component/common/Footer";
import PsychologistsPage from "./component/psychologists/PsychologistsPage";
import SupportPage from "./component/common/SupportPage";
import EditProfilePage from "./component/profile/EditProfilePage";
import Profile from "./component/profile/Profile";
import EditPsychologistProfilePage from "./component/psychologists/EditPsychologistProfilePage";
import AppointmentHistoryPage from "./component/appointments/AppointmentHistoryPage.jsx";
import BookAppointmentPage from "./component/appointments/BookAppointmentPage.jsx";
import PsychologistReviewsPage from "./component/reviews/PsychologistReviewsPage.jsx";
import CreateReviewPage from "./component/reviews/CreateReviewPage.jsx";
import PsychologistProfile from './component/psychologists/PsychologistProfile';

const theme = createTheme({
    palette: {
        mode: "light",
        primary: {
            main: "rgb(0, 153, 153)",
        },
        secondary: {
            main: "rgb(198, 236, 216)",
        },
        background: {
            default: "rgb(255, 255, 255)",
            paper: "rgb(244, 232, 251)",
        },
    },
    typography: {
        fontFamily: "Roboto, Arial, sans-serif",
    },
});

function App() {
    return (
        <ThemeProvider theme={theme}>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
                <CssBaseline />
                <Box
                    sx={{
                        display: "flex",
                        flexDirection: "column",
                        minHeight: "100vh",
                    }}
                >
                    <Router>
                        <Navbar />
                        <Box sx={{ flex: 1 }}>
                            <Routes>
                                <Route path="/" element={<Home />} />
                                <Route path="/login" element={<LoginPage />} />
                                <Route path="/register" element={<RegisterPage />} />
                                <Route path="/psychologists" element={<PsychologistsPage />} />
                                <Route path="/support" element={<SupportPage />} />
                                <Route path="/profile" element={<Profile />} />
                                <Route path="/profile/editprofile" element={<EditProfilePage />} />
                                <Route path="/profile/edit-psychologist" element={<EditPsychologistProfilePage />} />
                                <Route path="/appointments" element={<AppointmentHistoryPage />} />
                                <Route path="/appointments/book" element={<BookAppointmentPage />} />
                                <Route path="/psychologists/:psychologistId/reviews" element={<PsychologistReviewsPage />} />
                                <Route path="/reviews/create" element={<CreateReviewPage />} />
                                <Route path="/psychologists/:id" element={<PsychologistProfile />} />

                            </Routes>
                        </Box>
                        <Footer />
                    </Router>
                </Box>
            </LocalizationProvider>
        </ThemeProvider>
    );
}

export default App;
