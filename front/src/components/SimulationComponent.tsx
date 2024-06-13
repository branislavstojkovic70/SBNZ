import React, { useState } from 'react';
import { Button, Container, Box, Typography } from '@mui/material';
import { simulateParentHasCancer, simulateHypoxia, simulateSymptomAggravation, simulateForward2 } from '../services/alarmService';

const SimulationComponent: React.FC = () => {
    const [message, setMessage] = useState<string | null>(null);

    const handleSimulateParentHasCancer = async () => {
        try {
            const response = await simulateParentHasCancer();
            setMessage(response);
        } catch (error: any) {
            setMessage(error.message);
        }
    };

    const handleSimulateHypoxia = async () => {
        try {
            const response = await simulateHypoxia();
            setMessage(response);
        } catch (error: any) {
            setMessage(error.message);
        }
    };

    const handleSimulateSymptomAggravation = async () => {
        try {
            const response = await simulateSymptomAggravation();
            setMessage(response);
        } catch (error: any) {
            setMessage(error.message);
        }
    };

    const handleSimulateForward2 = async () => {
        try {
            const response = await simulateForward2();
            setMessage(response);
        } catch (error: any) {
            setMessage(error.message);
        }
    };

    return (
        <Container maxWidth="md">
            <Box sx={{ mt: 5 }}>
                <Typography variant="h4" gutterBottom>Simulation Controls</Typography>
                {message && <Typography variant="body1" color="error">{message}</Typography>}
                <Box sx={{ display: 'flex', justifyContent: 'space-around', mt: 3 }}>
                    <Button variant="contained" color="primary" onClick={handleSimulateParentHasCancer} sx={{ m: 1 }}>
                        Simulate Parent Has Cancer
                    </Button>
                    <Button variant="contained" color="primary" onClick={handleSimulateHypoxia} sx={{ m: 1 }}>
                        Simulate Hypoxia
                    </Button>
                    <Button variant="contained" color="primary" onClick={handleSimulateSymptomAggravation} sx={{ m: 1 }}>
                        Simulate Symptom Aggravation
                    </Button>
                    <Button variant="contained" color="primary" onClick={handleSimulateForward2} sx={{ m: 1 }}>
                        Simulate Forward2
                    </Button>
                </Box>
            </Box>
        </Container>
    );
};

export default SimulationComponent;
