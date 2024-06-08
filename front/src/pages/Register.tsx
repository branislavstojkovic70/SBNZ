import {
  Avatar,
  Box,
  Button,
  Container,
  CssBaseline,
  Grid,
  TextField,
  Typography,
} from "@mui/material";
import { LockOutlined } from "@mui/icons-material";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAppDispatch } from "../hooks/redux-hooks";
import { register } from "../slices/authSlice";
import { Role } from "../model/users/Role";

const Register = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const [ime, setIme] = useState("");
  const [prezime, setPrezime] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [dateOfBirth, setDateOfBirth] = useState("");
  const [bloodPressure, setBloodPressure] = useState("");
  const [puls, setPuls] = useState("");
  const [saturationO2, setSaturationO2] = useState("");
  const [bodyTemperature, setBodyTemperature] = useState("");

  const handleRegister = async () => {
    if (
      ime &&
      prezime &&
      email &&
      password &&
      dateOfBirth &&
      bloodPressure &&
      puls &&
      saturationO2 &&
      bodyTemperature
    ) {
      try {
        const formattedDateOfBirth = new Date(dateOfBirth).toISOString();
        await dispatch(
          register({
            id: null,
            ime,
            prezime,
            email,
            password,
            dateOfBirth: formattedDateOfBirth,
            role: Role.Patient,
            bloodPressure: parseFloat(bloodPressure),
            puls: parseInt(puls),
            saturationO2: parseFloat(saturationO2),
            bodyTemperature: parseFloat(bodyTemperature),
            examinations: [],
          })
        ).unwrap();
        navigate("/login");
      } catch (e) {
        console.error(e);
        alert("Registration failed! Try again later.");
      }
    } else {
      alert("Please fill all the fields.");
    }
  };

  return (
    <>
      <Container maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            mt: 20,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: "primary.light" }}>
            <LockOutlined />
          </Avatar>
          <Typography variant="h5">Register</Typography>
          <Box sx={{ mt: 3 }}>
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <TextField
                  name="ime"
                  required
                  fullWidth
                  id="ime"
                  label="First Name"
                  autoFocus
                  value={ime}
                  onChange={(e) => setIme(e.target.value)}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  name="prezime"
                  required
                  fullWidth
                  id="prezime"
                  label="Last Name"
                  value={prezime}
                  onChange={(e) => setPrezime(e.target.value)}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="email"
                  label="Email Address"
                  name="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="password"
                  label="Password"
                  type="password"
                  id="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="dateOfBirth"
                  label="Date of Birth"
                  type="date"
                  InputLabelProps={{ shrink: true }}
                  id="dateOfBirth"
                  value={dateOfBirth}
                  onChange={(e) => setDateOfBirth(e.target.value)}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="bloodPressure"
                  label="Blood Pressure"
                  type="number"
                  id="bloodPressure"
                  value={bloodPressure}
                  onChange={(e) => setBloodPressure(e.target.value)}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="puls"
                  label="Puls"
                  type="number"
                  id="puls"
                  value={puls}
                  onChange={(e) => setPuls(e.target.value)}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="saturationO2"
                  label="Saturation O2"
                  type="number"
                  id="saturationO2"
                  value={saturationO2}
                  onChange={(e) => setSaturationO2(e.target.value)}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="bodyTemperature"
                  label="Body Temperature"
                  type="number"
                  id="bodyTemperature"
                  value={bodyTemperature}
                  onChange={(e) => setBodyTemperature(e.target.value)}
                />
              </Grid>
            </Grid>
            <Button
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
              onClick={handleRegister}
            >
              Register
            </Button>
            <Grid container justifyContent="flex-end">
              <Grid item>
                <Link to="/login">Already have an account? Login</Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
      </Container>
    </>
  );
};

export default Register;
