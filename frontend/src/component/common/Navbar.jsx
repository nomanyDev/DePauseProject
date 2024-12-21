import React, { useState } from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import IconButton from "@mui/material/IconButton";
import Typography from "@mui/material/Typography";
import Menu from "@mui/material/Menu";
import Container from "@mui/material/Container";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import Tooltip from "@mui/material/Tooltip";
import MenuItem from "@mui/material/MenuItem";
import TextField from "@mui/material/TextField";
import WbTwilightTwoToneIcon from "@mui/icons-material/WbTwilightTwoTone";
import { useNavigate } from "react-router-dom";
import ApiService from "../../service/ApiService";
import ChangePasswordDialog from "../auth/ChangePasswordDialog";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
} from "@mui/material";


const pages = [
  { name: "Home", path: "/" },
  { name: "Psychologists", path: "/psychologists" },
  { name: "Appointments", path: "/appointments" },
  { name: "Profile", path: "/profile" },
  { name: "Support", path: "/support" },
];
const settings = ["Edit Profile", "Change Password", "Logout"];

function Navbar() {
  const [anchorElUser, setAnchorElUser] = useState(null);
  const [searchQuery, setSearchQuery] = useState("");
  const [loginDialogOpen, setLoginDialogOpen] = useState(false);
  const [passwordDialogOpen, setPasswordDialogOpen] = useState(false); 
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isAuthenticated, setIsAuthenticated] = useState(ApiService.isAuthenticated());
  const navigate = useNavigate();

  const handleOpenUserMenu = (event) => setAnchorElUser(event.currentTarget);
  const handleCloseUserMenu = () => setAnchorElUser(null);

  const handleSearch = async () => {
    try {
      const response = await ApiService.searchPsychologistsByName(searchQuery);
      if (response.psychologistList) {
        navigate("/psychologists", { state: { searchResults: response.psychologistList } });
      }
    } catch (err) {
      console.error("Error searching psychologists:", err.message);
    }
  };

  const handlePageNavigation = (path) => navigate(path);

  const handleLogout = () => {
    ApiService.logout();
    setIsAuthenticated(false);
    navigate("/");
  };

  const handleOpenChangePasswordDialog = () => {
    setPasswordDialogOpen(true);
    setAnchorElUser(null); // Закрыть меню
  };

  const handleOpenLoginDialog = () => setLoginDialogOpen(true);
  const handleCloseLoginDialog = () => setLoginDialogOpen(false);

  const handleLogin = async () => {
    try {
      const response = await ApiService.loginUser({ email, password });
      if (response.statusCode === 200) {
        localStorage.setItem("token", response.token);
        localStorage.setItem("role", response.role);
        setIsAuthenticated(true);
        setLoginDialogOpen(false);
        navigate("/profile");
      }
    } catch (error) {
      console.error("Error logging in:", error.message);
      alert("Login failed. Please check your credentials.");
    }
  };

  return (
    <AppBar
      position="static"
      sx={{
        backgroundImage:
          "linear-gradient(to bottom, rgba(0, 153, 153, 0.7), rgba(204, 245, 255, 0.7))",
        backdropFilter: "blur(10px)",
        color: "white",
      }}
    >
      <Container maxWidth="xl">
        <Toolbar disableGutters>
          {/* Logo */}
          <WbTwilightTwoToneIcon sx={{ mr: 1, fontSize: 40 }} />
          <Typography
            variant="h6"
            noWrap
            sx={{ mr: 2, cursor: "pointer" }}
            onClick={() => navigate("/")}
          >
            Depause
          </Typography>

          {/* Search Bar */}
          <Box sx={{ flexGrow: 1, mx: 2, display: { xs: "none", md: "flex" } }}>
            <TextField
              variant="outlined"
              placeholder="Search psychologists..."
              size="small"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              sx={{ backgroundColor: "white", borderRadius: 1 }}
            />
            <Button
              variant="contained"
              color="secondary"
              onClick={handleSearch}
              sx={{ ml: 1 }}
            >
              Search
            </Button>
          </Box>

          {/* Main Navigation */}
          <Box sx={{ flexGrow: 1, display: { xs: "none", md: "flex" } }}>
            {pages.map((page) => (
              <Button
                key={page.name}
                onClick={() => handlePageNavigation(page.path)}
                sx={{ my: 2, color: "white", display: "block" }}
              >
                {page.name}
              </Button>
            ))}
          </Box>

          {/* User Profile */}
          <Box sx={{ flexGrow: 0 }}>
            {isAuthenticated ? (
              <>
                <Tooltip title="Open settings">
                  <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }}>
                    <Avatar alt="User" src="/static/images/avatar/1.jpg" />
                  </IconButton>
                </Tooltip>
                <Menu
                  sx={{ mt: "45px" }}
                  anchorEl={anchorElUser}
                  open={Boolean(anchorElUser)}
                  onClose={handleCloseUserMenu}
                >
                  <MenuItem onClick={() => handlePageNavigation("/profile/editprofile")}>
                    Edit Profile
                  </MenuItem>
                  <MenuItem onClick={handleOpenChangePasswordDialog}>
                    Change Password
                  </MenuItem>
                  <MenuItem onClick={handleLogout}>Logout</MenuItem>
                </Menu>
              </>
            ) : (
              <Button onClick={handleOpenLoginDialog} sx={{ color: "white" }}>
                Sign In
              </Button>
            )}
          </Box>
        </Toolbar>
      </Container>

      {/* Change Password Dialog */}
      <ChangePasswordDialog
        open={passwordDialogOpen}
        onClose={() => setPasswordDialogOpen(false)}
      />

      {/* Login Dialog */}
      <Dialog open={loginDialogOpen} onClose={handleCloseLoginDialog}>
        <DialogTitle>Sign In</DialogTitle>
        <DialogContent>
          <DialogContentText>Please enter your credentials to log in.</DialogContentText>
          <TextField
            autoFocus
            margin="dense"
            label="Email Address"
            type="email"
            fullWidth
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <TextField
            margin="dense"
            label="Password"
            type="password"
            fullWidth
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseLoginDialog}>Cancel</Button>
          <Button onClick={handleLogin}>Sign In</Button>
          <Button onClick={() => navigate("/register")} color="primary">
            Register
          </Button>
        </DialogActions>
      </Dialog>
    </AppBar>
  );
}

export default Navbar;
