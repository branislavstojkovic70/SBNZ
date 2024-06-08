import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Examination } from '../model/examination/Examination';
import { updateExamination } from '../services/examinationService';
import { Button, TextField, Container, Box, Typography, MenuItem, Grid, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, IconButton } from '@mui/material';
import { ExaminationState } from '../model/examination/ExaminationState';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';
import { SymptomFrequency } from '../model/examination/SymptomFrequency';

const UpdateExamination: React.FC = () => {
    const location = useLocation();
    const examination = location.state as { examination: Examination };
    const [updatedExamination, setUpdatedExamination] = useState<Examination>(examination.examination);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setUpdatedExamination({ ...updatedExamination, [name]: value });
    };

    const handleStateChange = (e: React.ChangeEvent<{ value: unknown }>) => {
        setUpdatedExamination({ ...updatedExamination, examinationState: e.target.value as ExaminationState });
    };

    const handleAddSymptom = () => {
        setUpdatedExamination({ ...updatedExamination, symptoms: [...updatedExamination.symptoms, { id: 0, name: '', description: '', intensity: 0, symptomFrequency: SymptomFrequency.CONSTANT }] });
    };

    const handleDeleteSymptom = (index: number) => {
        const updatedSymptoms = updatedExamination.symptoms.filter((_, i) => i !== index);
        setUpdatedExamination({ ...updatedExamination, symptoms: updatedSymptoms });
    };

    const handleSymptomChange = (index: number, e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        const updatedSymptoms = updatedExamination.symptoms.map((symptom, i) => (i === index ? { ...symptom, [name]: value } : symptom));
        setUpdatedExamination({ ...updatedExamination, symptoms: updatedSymptoms });
    };

    const handleAddExaminationType = () => {
        setUpdatedExamination({ ...updatedExamination, examinationTypes: [...(updatedExamination.examinationTypes || []), { id: 0, name: '', testResults: [] }] });
    };

    const handleDeleteExaminationType = (index: number) => {
        const updatedExaminationTypes = updatedExamination.examinationTypes?.filter((_, i) => i !== index) || [];
        setUpdatedExamination({ ...updatedExamination, examinationTypes: updatedExaminationTypes });
    };

    const handleExaminationTypeChange = (index: number, e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        const updatedExaminationTypes = updatedExamination.examinationTypes?.map((type, i) => (i === index ? { ...type, [name]: value } : type)) || [];
        setUpdatedExamination({ ...updatedExamination, examinationTypes: updatedExaminationTypes });
    };

    const handleAddTestResult = (typeIndex: number) => {
        const updatedExaminationTypes = updatedExamination.examinationTypes?.map((type, i) => (i === typeIndex ? { ...type, testResults: [...type.testResults, { id: 0, name: '', measureUnit: '', value: 0, description: '' }] } : type)) || [];
        setUpdatedExamination({ ...updatedExamination, examinationTypes: updatedExaminationTypes });
    };

    const handleDeleteTestResult = (typeIndex: number, testResultIndex: number) => {
        const updatedExaminationTypes = updatedExamination.examinationTypes?.map((type, i) => (i === typeIndex ? { ...type, testResults: type.testResults.filter((_, j) => j !== testResultIndex) } : type)) || [];
        setUpdatedExamination({ ...updatedExamination, examinationTypes: updatedExaminationTypes });
    };

    const handleTestResultChange = (typeIndex: number, testResultIndex: number, e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        const updatedExaminationTypes = updatedExamination.examinationTypes?.map((type, i) => (i === typeIndex ? { ...type, testResults: type.testResults.map((testResult, j) => (j === testResultIndex ? { ...testResult, [name]: value } : testResult)) } : type)) || [];
        setUpdatedExamination({ ...updatedExamination, examinationTypes: updatedExaminationTypes });
    };

    const handleUpdate = async () => {
        try {
            const updatedExaminationResult = await updateExamination(updatedExamination);
            console.log('Examination updated successfully:', updatedExaminationResult);
            if (updatedExamination.examinationState === ExaminationState.SCHEDULED) {
                navigate('/doctor/scheduledExaminations');
            } else if (updatedExamination.examinationState === ExaminationState.DONE) {
                navigate('/doctor/doneExaminations');
            }
        } catch (err) {
            setError('Failed to update examination');
        }
    };

    return (
        <Container maxWidth="lg">
            <Box sx={{ mt: 5 }}>
                <Typography variant="h4" gutterBottom>Update Examination</Typography>
                {error && <Typography variant="body1" color="error">{error}</Typography>}

                <TextField
                    label="Date and Time"
                    type="datetime-local"
                    fullWidth
                    margin="normal"
                    name="dateTime"
                    value={updatedExamination.dateTime}
                    onChange={handleInputChange}
                    InputLabelProps={{ shrink: true }}
                />

                <TextField
                    label="Note"
                    fullWidth
                    margin="normal"
                    name="note"
                    value={updatedExamination.note}
                    onChange={handleInputChange}
                />

                <TextField
                    label="Examination State"
                    select
                    fullWidth
                    margin="normal"
                    name="examinationState"
                    value={updatedExamination.examinationState}
                    onChange={handleStateChange}
                >
                    {Object.values(ExaminationState).map((state) => (
                        <MenuItem key={state} value={state}>
                            {state}
                        </MenuItem>
                    ))}
                </TextField>

                <Typography variant="h6">Symptoms</Typography>
                <Button onClick={handleAddSymptom} startIcon={<AddIcon />} color="primary">Add Symptom</Button>
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
                            {updatedExamination.symptoms.map((symptom, index) => (
                                <TableRow key={index}>
                                    <TableCell>
                                        <TextField
                                            name="name"
                                            value={symptom.name}
                                            onChange={(e) => handleSymptomChange(index, e)}
                                            fullWidth
                                        />
                                    </TableCell>
                                    <TableCell>
                                        <TextField
                                            name="description"
                                            value={symptom.description}
                                            onChange={(e) => handleSymptomChange(index, e)}
                                            fullWidth
                                        />
                                    </TableCell>
                                    <TableCell>
                                        <TextField
                                            name="intensity"
                                            type="number"
                                            value={symptom.intensity}
                                            onChange={(e) => handleSymptomChange(index, e)}
                                            fullWidth
                                        />
                                    </TableCell>
                                    <TableCell>
                                        <TextField
                                            name="symptomFrequency"
                                            select
                                            value={symptom.symptomFrequency}
                                            onChange={(e) => handleSymptomChange(index, e)}
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
                                        <IconButton onClick={() => handleDeleteSymptom(index)} color="secondary">
                                            <DeleteIcon />
                                        </IconButton>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>

                <Typography variant="h6" sx={{ mt: 3 }}>Examination Types</Typography>
                <Button onClick={handleAddExaminationType} startIcon={<AddIcon />} color="primary">Add Examination Type</Button>
                <TableContainer component={Paper}>
                    <Table size="small">
                        <TableHead>
                            <TableRow>
                                <TableCell>Name</TableCell>
                                <TableCell>Actions</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {updatedExamination.examinationTypes?.map((type, typeIndex) => (
                                <React.Fragment key={typeIndex}>
                                    <TableRow>
                                        <TableCell>
                                            <TextField
                                                name="name"
                                                value={type.name}
                                                onChange={(e) => handleExaminationTypeChange(typeIndex, e)}
                                                fullWidth
                                            />
                                        </TableCell>
                                        <TableCell>
                                            <IconButton onClick={() => handleDeleteExaminationType(typeIndex)} color="secondary">
                                                <DeleteIcon />
                                            </IconButton>
                                        </TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell colSpan={2}>
                                            <Typography variant="h6">Test Results</Typography>
                                            <Button onClick={() => handleAddTestResult(typeIndex)} startIcon={<AddIcon />} color="primary">Add Test Result</Button>
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
                                                    {type.testResults.map((testResult, testResultIndex) => (
                                                        <TableRow key={testResultIndex}>
                                                            <TableCell>
                                                                <TextField
                                                                    name="name"
                                                                    value={testResult.name}
                                                                    onChange={(e) => handleTestResultChange(typeIndex, testResultIndex, e)}
                                                                    fullWidth
                                                                />
                                                            </TableCell>
                                                            <TableCell>
                                                                <TextField
                                                                    name="measureUnit"
                                                                    value={testResult.measureUnit}
                                                                    onChange={(e) => handleTestResultChange(typeIndex, testResultIndex, e)}
                                                                    fullWidth
                                                                />
                                                            </TableCell>
                                                            <TableCell>
                                                                <TextField
                                                                    name="value"
                                                                    type="number"
                                                                    value={testResult.value}
                                                                    onChange={(e) => handleTestResultChange(typeIndex, testResultIndex, e)}
                                                                    fullWidth
                                                                />
                                                            </TableCell>
                                                            <TableCell>
                                                                <TextField
                                                                    name="description"
                                                                    value={testResult.description}
                                                                    onChange={(e) => handleTestResultChange(typeIndex, testResultIndex, e)}
                                                                    fullWidth
                                                                />
                                                            </TableCell>
                                                            <TableCell>
                                                                <IconButton onClick={() => handleDeleteTestResult(typeIndex, testResultIndex)} color="secondary">
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

                <Button variant="contained" color="primary" onClick={handleUpdate} sx={{ mt: 3 }}>
                    Update Examination
                </Button>
            </Box>
        </Container>
    );

};

export default UpdateExamination;
