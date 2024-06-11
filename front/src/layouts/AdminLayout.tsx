import React from "react";
import { Outlet, Link as RouterLink } from "react-router-dom";
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import { styled } from '@mui/material/styles';
import Link from '@mui/material/Link'; 

const StyledButton = styled(Button)(({ theme }) => ({
    color: '#fff',
    marginLeft: theme.spacing(2),
    '&:hover': {
      backgroundColor: '#0056b3',
    },
  }));

const AdminLayout = () => {
  return (
    <div>
      <AppBar position="static" sx={{ backgroundColor: '#007bff' }}>
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1 }}>
            Admin Portal
          </Typography>
          <Link component={RouterLink} to="/admin" color="inherit" underline="none">
            <StyledButton>Home</StyledButton>
          </Link>
          <Link component={RouterLink} to="/admin/addDoctor" color="inherit" underline="none">
            <StyledButton>Add doctor</StyledButton>
          </Link>
          <Link component={RouterLink} to="/admin/doctors" color="inherit" underline="none">
            <StyledButton>Doctors</StyledButton>
          </Link>
          <Link component={RouterLink} to="/admin/highRiskPatients" color="inherit" underline="none">
            <StyledButton>High risk patients</StyledButton>
          </Link>
        </Toolbar>
      </AppBar>
      <Outlet />
    </div>
  )
}

export default AdminLayout