import React, { useEffect } from "react";
import { Button, Container, Typography, Box, Paper, Avatar } from "@mui/material";
import { useAppDispatch, useAppSelector } from "../hooks/redux-hooks";
import { getUser, logout } from "../slices/authSlice";
import { useNavigate } from "react-router-dom";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';

const Home = () => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const basicUserInfo = useAppSelector((state) => state.auth.basicUserInfo);

  useEffect(() => {
    if (basicUserInfo && basicUserInfo.id !== undefined) {
      console.log(basicUserInfo);
    }
  }, [basicUserInfo]);

  const handleLogout = async () => {
    try {
      await dispatch(logout()).unwrap();
      navigate("/login");
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <Container 
      sx={{ 
        width: '100%', 
        height: '88vh', 
        display: 'flex', 
        alignItems: 'center', 
        justifyContent: 'center'
      }}
    >
      <Paper elevation={3} sx={{ p: 4, width: '60%', height: '70%' }}>
        <Box display="flex" justifyContent="center" mb={3}>
          <Avatar sx={{ bgcolor: 'primary.main', width: 56, height: 56 }}>
            <AccountCircleIcon sx={{ fontSize: 40 }} />
          </Avatar>
        </Box>
        <Typography variant="h4" component="h1" align="center" gutterBottom>
          Home
        </Typography>
        <Box my={3}>
          <Typography variant="h6" component="h4" align="center">
            Id: {basicUserInfo?.id}
          </Typography>
          <Typography variant="h6" component="h4" align="center">
            Email: {basicUserInfo?.email}
          </Typography>
        </Box>
        <Box display="flex" justifyContent="center">
          <Button variant="contained" color="primary" sx={{ mt: 3 }} onClick={handleLogout}>
            Logout
          </Button>
        </Box>
      </Paper>
    </Container>
  );
};

export default Home;
