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
import { addDoctor } from "../services/adminService";
import { Role } from "../model/users/Role";

const AddDoctor = () => {
    const dispatch = useAppDispatch();
    const [ime, setIme] = useState("");
    const [prezime, setPrezime] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [dateOfBirth, setDateOfBirth] = useState("");
    const [specialization, setSpecialization] = useState("");
    const navigate = useNavigate();

    const handleAddDoctor = async () => {
        if (
            ime &&
            prezime &&
            email &&
            password &&
            dateOfBirth &&
            specialization
        ) {
            try {
                const formattedDateOfBirth = new Date(dateOfBirth).toISOString();
                await addDoctor({
                    ime,
                    prezime,
                    email,
                    password,
                    dateOfBirth: formattedDateOfBirth,
                    role: Role.Doctor,
                    specialization,
                });
                navigate("/admin/doctors");
            } catch (e) {
                console.error(e);
                alert("Adding doctor failed! Try again later.");
            }
        } else {
            alert("Please fill all the fields.");
        }
    };

    return (
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
                <Typography variant="h5">Add Doctor</Typography>
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
                                name="specialization"
                                label="Specialization"
                                id="specialization"
                                value={specialization}
                                onChange={(e) => setSpecialization(e.target.value)}
                            />
                        </Grid>
                    </Grid>
                    <Button
                        fullWidth
                        variant="contained"
                        sx={{ mt: 3, mb: 2 }}
                        onClick={handleAddDoctor}
                    >
                        Add Doctor
                    </Button>
                    <Grid container justifyContent="flex-end">
                        <Grid item>
                            <Link to="/dashboard">Back to Dashboard</Link>
                        </Grid>
                    </Grid>
                </Box>
            </Box>
        </Container>
    );
};

export default AddDoctor;
