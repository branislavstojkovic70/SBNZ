import React, { useState, useEffect } from "react";
import { Outlet, Link as RouterLink } from "react-router-dom";
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import { styled } from '@mui/material/styles';
import Link from '@mui/material/Link';
import AlarmPopup from '../components/AlarmPopup';
import { connect } from "../services/websocket";
import { useAppSelector } from "../hooks/redux-hooks";

const StyledButton = styled(Button)(({ theme }) => ({
  color: '#fff',
  marginLeft: theme.spacing(2),
  '&:hover': {
    backgroundColor: '#0056b3',
  },
}));

const PatientLayout = () => {
  const [alarmDescription, setAlarmDescription] = useState<string>('');
  const [open, setOpen] = useState<boolean>(false);
  const basicUserInfo = useAppSelector((state) => state.auth.basicUserInfo); 

  useEffect(() => {
    if (basicUserInfo?.id) {
      connect(basicUserInfo.id, (message) => {
        setAlarmDescription(message.description);
        setOpen(true);
      });
    }
  }, [basicUserInfo]);

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <div>
      <AppBar position="static" sx={{ backgroundColor: '#007bff' }}>
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1 }}>
            Patient Portal
          </Typography>
          <Link component={RouterLink} to="/patient" color="inherit" underline="none">
            <StyledButton>Home</StyledButton>
          </Link>
          <Link component={RouterLink} to="/patient/scheduledExaminations" color="inherit" underline="none">
            <StyledButton>Scheduled examinations</StyledButton>
          </Link>
          <Link component={RouterLink} to="/patient/doneExaminations" color="inherit" underline="none">
            <StyledButton>Done examinations</StyledButton>
          </Link>
          <Link component={RouterLink} to="/patient/diagnosis" color="inherit" underline="none">
            <StyledButton>Diagnosis</StyledButton>
          </Link>
          <Link component={RouterLink} to="/patient/therapies" color="inherit" underline="none">
            <StyledButton>Therapies</StyledButton>
          </Link>
          <Link component={RouterLink} to="/patient/alarms" color="inherit" underline="none">
            <StyledButton>Alarms</StyledButton>
          </Link>
          <Link component={RouterLink} to="/patient/simulations" color="inherit" underline="none">
            <StyledButton>Simulations</StyledButton>
          </Link>
        </Toolbar>
      </AppBar>
      <Outlet />
      <AlarmPopup open={open} onClose={handleClose} description={alarmDescription} />
    </div>
  );
};

export default PatientLayout;
