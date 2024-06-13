import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Examination } from '../model/examination/Examination';
import { determineDiagnosis } from '../services/examinationService';
import {
    Button, TextField, Container, Box, Typography, MenuItem, Table, TableBody, TableCell,
    TableContainer, TableHead, TableRow, Paper, IconButton, Select,
    Collapse
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';
import { ExaminationState } from '../model/examination/ExaminationState';
import { SymptomFrequency } from '../model/examination/SymptomFrequency';
import { fetchAllPatients } from '../services/patientService';
import { useAppSelector } from '../hooks/redux-hooks';
import { Doctor } from '../model/users/Doctor';
import { findDoctorById } from '../services/doctorService';
import { Patient } from '../model/users/Patient';

const DetermineDiagnosis: React.FC = () => {
    const [patients, setPatients] = useState<Patient[]>([]);
    const [selectedPatient, setSelectedPatient] = useState<Patient | null>(null);
    const [examinations, setExaminations] = useState<Examination[]>([]);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();
    const basicUserInfo = useAppSelector((state) => state.auth.basicUserInfo);
    const [doctor, setDoctor] = useState<Doctor | null>(null);
    const [openExaminationIndex, setOpenExaminationIndex] = useState<number | null>(null);


    useEffect(() => {
        const getPatients = async () => {
            try {
                const data = await fetchAllPatients();
                setPatients(data);
                if (basicUserInfo !== undefined && basicUserInfo !== null) {
                    const doctor = await findDoctorById(basicUserInfo?.id);
                    setDoctor(doctor);
                }
            } catch (err) {
                alert("Failed to fetch patients");
            }
        };

        getPatients();
    }, []);

    const handleAddExamination = () => {
        if (doctor != null) {
            const newExaminations = [
                ...examinations,
                {
                    dateTime: '',
                    doctor: doctor,
                    note: '',
                    examinationState: ExaminationState.DONE,
                    symptoms: [],
                    examinationTypes: []
                }
            ];
            setExaminations(newExaminations);
            setOpenExaminationIndex(newExaminations.length - 1); // Set the last added examination as open
        }
    };

    const handleDeleteExamination = (index: number) => {
        const updatedExaminations = examinations.filter((_, i) => i !== index);
        setExaminations(updatedExaminations);
        if (openExaminationIndex === index) {
            setOpenExaminationIndex(null); // Close the examination if it was open
        } else if (openExaminationIndex && openExaminationIndex > index) {
            setOpenExaminationIndex(openExaminationIndex - 1); // Adjust the open index if needed
        }
    };


    const handleExaminationChange = (index: number, e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        const updatedExaminations = examinations.map((exam, i) => (i === index ? { ...exam, [name]: value } : exam));
        setExaminations(updatedExaminations);
    };

    const handleSymptomChange = (examIndex: number, symptomIndex: number, e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        const updatedExaminations = examinations.map((exam, i) => {
            if (i === examIndex) {
                const updatedSymptoms = exam.symptoms.map((symptom, j) => (j === symptomIndex ? { ...symptom, [name]: value } : symptom));
                return { ...exam, symptoms: updatedSymptoms };
            }
            return exam;
        });
        setExaminations(updatedExaminations);
    };

    const handleAddSymptom = (examIndex: number) => {
        const updatedExaminations = examinations.map((exam, i) => {
            if (i === examIndex) {
                return { ...exam, symptoms: [...exam.symptoms, { name: '', description: '', intensity: 0, symptomFrequency: SymptomFrequency.CONSTANT }] };
            }
            return exam;
        });
        setExaminations(updatedExaminations);
    };

    const handleDeleteSymptom = (examIndex: number, symptomIndex: number) => {
        const updatedExaminations = examinations.map((exam, i) => {
            if (i === examIndex) {
                const updatedSymptoms = exam.symptoms.filter((_, j) => j !== symptomIndex);
                return { ...exam, symptoms: updatedSymptoms };
            }
            return exam;
        });
        setExaminations(updatedExaminations);
    };

    const handleAddExaminationType = (examIndex: number) => {
        const updatedExaminations = examinations.map((exam, i) => {
            if (i === examIndex) {
                return { ...exam, examinationTypes: [...exam.examinationTypes!, { name: '', testResults: [] }] };
            }
            return exam;
        });
        setExaminations(updatedExaminations);
    };

    const handleDeleteExaminationType = (examIndex: number, typeIndex: number) => {
        const updatedExaminations = examinations.map((exam, i) => {
            if (i === examIndex) {
                const updatedTypes = exam.examinationTypes!.filter((_, j) => j !== typeIndex);
                return { ...exam, examinationTypes: updatedTypes };
            }
            return exam;
        });
        setExaminations(updatedExaminations);
    };

    const handleExaminationTypeChange = (examIndex: number, typeIndex: number, e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        const updatedExaminations = examinations.map((exam, i) => {
            if (i === examIndex) {
                const updatedTypes = exam.examinationTypes!.map((type, j) => (j === typeIndex ? { ...type, [name]: value } : type));
                return { ...exam, examinationTypes: updatedTypes };
            }
            return exam;
        });
        setExaminations(updatedExaminations);
    };

    const handleAddTestResult = (examIndex: number, typeIndex: number) => {
        const updatedExaminations = examinations.map((exam, i) => {
            if (i === examIndex) {
                const updatedTypes = exam.examinationTypes!.map((type, j) => {
                    if (j === typeIndex) {
                        return { ...type, testResults: [...type.testResults, { name: '', measureUnit: '', value: 0, description: '' }] };
                    }
                    return type;
                });
                return { ...exam, examinationTypes: updatedTypes };
            }
            return exam;
        });
        setExaminations(updatedExaminations);
    };

    const handleDeleteTestResult = (examIndex: number, typeIndex: number, resultIndex: number) => {
        const updatedExaminations = examinations.map((exam, i) => {
            if (i === examIndex) {
                const updatedTypes = exam.examinationTypes!.map((type, j) => {
                    if (j === typeIndex) {
                        const updatedResults = type.testResults.filter((_, k) => k !== resultIndex);
                        return { ...type, testResults: updatedResults };
                    }
                    return type;
                });
                return { ...exam, examinationTypes: updatedTypes };
            }
            return exam;
        });
        setExaminations(updatedExaminations);
    };

    const handleTestResultChange = (examIndex: number, typeIndex: number, resultIndex: number, e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        const updatedExaminations = examinations.map((exam, i) => {
            if (i === examIndex) {
                const updatedTypes = exam.examinationTypes!.map((type, j) => {
                    if (j === typeIndex) {
                        const updatedResults = type.testResults.map((result, k) => (k === resultIndex ? { ...result, [name]: value } : result));
                        return { ...type, testResults: updatedResults };
                    }
                    return type;
                });
                return { ...exam, examinationTypes: updatedTypes };
            }
            return exam;
        });
        setExaminations(updatedExaminations);
    };

    const handleSubmit = async () => {
        if (selectedPatient) {
            try {
                const request = {
                    patient: selectedPatient,
                    examinations: examinations
                };
                console.log(request);
                await determineDiagnosis(request);
                navigate('/doctor/doneExaminations');
            } catch (err) {
                setError('Failed to determine diagnosis');
            }
        }
    };

    return (
        <Container maxWidth="lg">
            <Box sx={{ mt: 5 }}>
                <Typography variant="h4" gutterBottom>Determine Diagnosis</Typography>
                {error && <Typography variant="body1" color="error">{error}</Typography>}

                <Select
                    label="Patient"
                    fullWidth
                    value={selectedPatient ? selectedPatient.id : ''}
                    onChange={(e) => {
                        const patient = patients.find(p => p.id === Number(e.target.value));
                        setSelectedPatient(patient || null);
                    }}
                >
                    {patients.map((patient) => (
                        <MenuItem key={patient.email} value={patient.id}>
                            {patient.ime} {patient.prezime}
                        </MenuItem>
                    ))}
                </Select>

                <Button onClick={handleAddExamination} startIcon={<AddIcon />} color="primary" sx={{ mt: 3 }}>
                    Add Examination
                </Button>

                {examinations.map((exam, examIndex) => (
                    <Box key={examIndex} sx={{ mt: 3, mb: 3 }}>
                        <Button onClick={() => setOpenExaminationIndex(openExaminationIndex === examIndex ? null : examIndex)}>
                            {openExaminationIndex === examIndex ? "Collapse" : "Expand"}
                        </Button>
                        <Collapse in={openExaminationIndex === examIndex}>
                            <Box sx={{ p: 2, border: '1px solid #ccc', borderRadius: '8px' }}>
                                <TextField
                                    label="Date and Time"
                                    type="datetime-local"
                                    fullWidth
                                    margin="normal"
                                    name="dateTime"
                                    value={exam.dateTime}
                                    onChange={(e) => handleExaminationChange(examIndex, e)}
                                    InputLabelProps={{ shrink: true }}
                                />

                                <TextField
                                    label="Note"
                                    fullWidth
                                    margin="normal"
                                    name="note"
                                    value={exam.note}
                                    onChange={(e) => handleExaminationChange(examIndex, e)}
                                />

                                <TextField
                                    label="Examination State"
                                    select
                                    fullWidth
                                    margin="normal"
                                    name="examinationState"
                                    value={exam.examinationState}
                                    onChange={(e) => handleExaminationChange(examIndex, e)}
                                >
                                    {Object.values(ExaminationState).map((state) => (
                                        <MenuItem key={state} value={state}>
                                            {state}
                                        </MenuItem>
                                    ))}
                                </TextField>

                                <Typography variant="h6">Symptoms</Typography>
                                <Button onClick={() => handleAddSymptom(examIndex)} startIcon={<AddIcon />} color="primary">Add Symptom</Button>
                                <TableContainer component={Paper}>
                                    <Table size="small">
                                        <TableHead>
                                            <TableRow>
                                                <TableCell>Name</TableCell>
                                                <TableCell>Description</TableCell>
                                                <TableCell>Intensity</TableCell>
                                                <TableCell>Frequency</TableCell>
                                                <TableCell>Actions</TableCell>
                                            </TableRow>
                                        </TableHead>
                                        <TableBody>
                                            {exam.symptoms.map((symptom, symptomIndex) => (
                                                <TableRow key={symptomIndex}>
                                                    <TableCell>
                                                        <TextField
                                                            name="name"
                                                            value={symptom.name}
                                                            onChange={(e) => handleSymptomChange(examIndex, symptomIndex, e)}
                                                            fullWidth
                                                        />
                                                    </TableCell>
                                                    <TableCell>
                                                        <TextField
                                                            name="description"
                                                            value={symptom.description}
                                                            onChange={(e) => handleSymptomChange(examIndex, symptomIndex, e)}
                                                            fullWidth
                                                        />
                                                    </TableCell>
                                                    <TableCell>
                                                        <TextField
                                                            name="intensity"
                                                            type="number"
                                                            value={symptom.intensity}
                                                            onChange={(e) => handleSymptomChange(examIndex, symptomIndex, e)}
                                                            fullWidth
                                                        />
                                                    </TableCell>
                                                    <TableCell>
                                                        <TextField
                                                            name="symptomFrequency"
                                                            select
                                                            value={symptom.symptomFrequency}
                                                            onChange={(e) => handleSymptomChange(examIndex, symptomIndex, e)}
                                                            fullWidth
                                                        >
                                                            {Object.values(SymptomFrequency).map((freq) => (
                                                                <MenuItem key={freq} value={freq}>
                                                                    {freq}
                                                                </MenuItem>
                                                            ))}
                                                        </TextField>
                                                    </TableCell>
                                                    <TableCell>
                                                        <IconButton onClick={() => handleDeleteSymptom(examIndex, symptomIndex)} color="secondary">
                                                            <DeleteIcon />
                                                        </IconButton>
                                                    </TableCell>
                                                </TableRow>
                                            ))}
                                        </TableBody>
                                    </Table>
                                </TableContainer>

                                <Typography variant="h6">Examination Types</Typography>
                                <Button onClick={() => handleAddExaminationType(examIndex)} startIcon={<AddIcon />} color="primary">Add Examination Type</Button>
                                <TableContainer component={Paper}>
                                    <Table size="small">
                                        <TableHead>
                                            <TableRow>
                                                <TableCell>Name</TableCell>
                                                <TableCell>Actions</TableCell>
                                            </TableRow>
                                        </TableHead>
                                        <TableBody>
                                            {exam.examinationTypes!.map((type, typeIndex) => (
                                                <React.Fragment key={typeIndex}>
                                                    <TableRow>
                                                        <TableCell>
                                                            <TextField
                                                                name="name"
                                                                value={type.name}
                                                                onChange={(e) => handleExaminationTypeChange(examIndex, typeIndex, e)}
                                                                fullWidth
                                                            />
                                                        </TableCell>
                                                        <TableCell>
                                                            <IconButton onClick={() => handleDeleteExaminationType(examIndex, typeIndex)} color="secondary">
                                                                <DeleteIcon />
                                                            </IconButton>
                                                        </TableCell>
                                                    </TableRow>
                                                    <TableRow>
                                                        <TableCell colSpan={2}>
                                                            <Typography variant="h6">Test Results</Typography>
                                                            <Button onClick={() => handleAddTestResult(examIndex, typeIndex)} startIcon={<AddIcon />} color="primary">Add Test Result</Button>
                                                            <Table size="small">
                                                                <TableHead>
                                                                    <TableRow>
                                                                        <TableCell>Name</TableCell>
                                                                        <TableCell>Measure Unit</TableCell>
                                                                        <TableCell>Value</TableCell>
                                                                        <TableCell>Description</TableCell>
                                                                        <TableCell>Actions</TableCell>
                                                                    </TableRow>
                                                                </TableHead>
                                                                <TableBody>
                                                                    {type.testResults.map((result, resultIndex) => (
                                                                        <TableRow key={resultIndex}>
                                                                            <TableCell>
                                                                                <TextField
                                                                                    name="name"
                                                                                    value={result.name}
                                                                                    onChange={(e) => handleTestResultChange(examIndex, typeIndex, resultIndex, e)}
                                                                                    fullWidth
                                                                                />
                                                                            </TableCell>
                                                                            <TableCell>
                                                                                <TextField
                                                                                    name="measureUnit"
                                                                                    value={result.measureUnit}
                                                                                    onChange={(e) => handleTestResultChange(examIndex, typeIndex, resultIndex, e)}
                                                                                    fullWidth
                                                                                />
                                                                            </TableCell>
                                                                            <TableCell>
                                                                                <TextField
                                                                                    name="value"
                                                                                    type="number"
                                                                                    value={result.value}
                                                                                    onChange={(e) => handleTestResultChange(examIndex, typeIndex, resultIndex, e)}
                                                                                    fullWidth
                                                                                />
                                                                            </TableCell>
                                                                            <TableCell>
                                                                                <TextField
                                                                                    name="description"
                                                                                    value={result.description}
                                                                                    onChange={(e) => handleTestResultChange(examIndex, typeIndex, resultIndex, e)}
                                                                                    fullWidth
                                                                                />
                                                                            </TableCell>
                                                                            <TableCell>
                                                                                <IconButton onClick={() => handleDeleteTestResult(examIndex, typeIndex, resultIndex)} color="secondary">
                                                                                    <DeleteIcon />
                                                                                </IconButton>
                                                                            </TableCell>
                                                                        </TableRow>
                                                                    ))}
                                                                </TableBody>
                                                            </Table>
                                                        </TableCell>
                                                    </TableRow>
                                                </React.Fragment>
                                            ))}
                                        </TableBody>
                                    </Table>
                                </TableContainer>
                            </Box>
                        </Collapse>
                    </Box>
                ))}

                <Button variant="contained" color="primary" onClick={handleSubmit} sx={{ mt: 3 }}>
                    Determine Diagnosis
                </Button>
            </Box>
        </Container>
    );
};

export default DetermineDiagnosis;
