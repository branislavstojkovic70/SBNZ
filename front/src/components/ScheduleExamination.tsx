import {
    Avatar,
    Box,
    Button,
    Container,
    CssBaseline,
    Grid,
    TextField,
    Typography,
    MenuItem,
} from "@mui/material";
import { EventNote } from "@mui/icons-material";
import { useState, useEffect } from "react";
import { fetchAllPatients } from "../services/patientService";
import { Patient } from "../model/users/Patient";
import { Doctor } from "../model/users/Doctor";
import { addExamination } from "../services/examinationService"; // Putanja do vaše funkcije
import { useAppSelector } from "../hooks/redux-hooks";
import { findDoctorById } from "../services/doctorService";
import { useNavigate } from "react-router-dom";

const ScheduleExamination = () => {
    const [patients, setPatients] = useState<Patient[]>([]);
    const [selectedPatient, setSelectedPatient] = useState<Patient | null>(null);
    const [dateTime, setDateTime] = useState("");
    const [note, setNote] = useState("");
    const [doctor, setDoctor] = useState<Doctor | null>(null);
    const basicUserInfo = useAppSelector((state) => state.auth.basicUserInfo);
    const navigate = useNavigate();

    useEffect(() => {
        const getPatients = async () => {
            try {
                const data = await fetchAllPatients();
                setPatients(data);
                if(basicUserInfo !== undefined && basicUserInfo !== null){
                    const doctor = await findDoctorById(basicUserInfo?.id);
                    setDoctor(doctor);
                  }
            } catch (err) {
                alert("Failed to fetch patients");
            }
        };

        getPatients();
    }, []);

    const handleAddExamination = async () => {
        if (selectedPatient && doctor && dateTime && note) {
            try {
                await addExamination(selectedPatient, doctor, dateTime, note);
                navigate('/doctor/scheduledExaminations');
            } catch (e) {
                console.error(e);
                alert("Failed to add examination. Try again later.");
            }
        } else {
            alert("Please fill all the fields.");
        }
    };

    return (
        <Container maxWidth="md">
            <CssBaseline />
            <Box
                sx={{
                    mt: 5,
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center",
                }}
            >
                <Avatar sx={{ m: 1, bgcolor: "primary.light" }}>
                    <EventNote />
                </Avatar>
                <Typography variant="h5">Add Examination</Typography>
                <Box sx={{ mt: 3 }}>
                    <Grid container spacing={2}>
                        <Grid item xs={12}>
                            <TextField
                                select
                                required
                                fullWidth
                                id="patient"
                                label="Select Patient"
                                value={selectedPatient ? selectedPatient.id : ""}
                                onChange={(e) =>
                                    setSelectedPatient(
                                        patients.find((patient) => patient.id === Number(e.target.value)) || null
                                    )
                                }
                            >
                                {patients.map((patient) => (
                                    <MenuItem key={patient.id} value={patient.id}>
                                        {patient.ime} {patient.prezime}
                                    </MenuItem>
                                ))}
                            </TextField>
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                required
                                fullWidth
                                id="dateTime"
                                label="Date and Time"
                                type="datetime-local"
                                InputLabelProps={{ shrink: true }}
                                value={dateTime}
                                onChange={(e) => setDateTime(e.target.value)}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                fullWidth
                                id="note"
                                label="Note"
                                value={note}
                                onChange={(e) => setNote(e.target.value)}
                            />
                        </Grid>
                    </Grid>
                    <Button
                        fullWidth
                        variant="contained"
                        sx={{ mt: 3, mb: 2 }}
                        onClick={handleAddExamination}
                    >
                        Add Examination
                    </Button>
                </Box>
            </Box>
        </Container>
    );
};

export default ScheduleExamination;
