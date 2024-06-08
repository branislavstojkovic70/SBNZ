import React, { useEffect, useState } from 'react';
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  CircularProgress,
  Typography,
  Box,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  Button,
} from '@mui/material';
import { Patient } from '../model/users/Patient';
import { fetchHighRiskPatients, fetchPatients } from '../services/patientService';
import { useAppSelector } from '../hooks/redux-hooks';

type PatientTableProps = {
  fetchType: number;
};

const PatientTable = () => {
  const [patients, setPatients] = useState<Patient[]>([]);
  const [selectedPatient, setSelectedPatient] = useState<Patient | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [dialogOpen, setDialogOpen] = useState<boolean>(false);
  const basicUserInfo = useAppSelector((state) => state.auth.basicUserInfo);

  useEffect(() => {
    const getPatients = async () => {
      try {
        if(basicUserInfo !== undefined && basicUserInfo !== null){
          const data = await fetchPatients(basicUserInfo?.id);
          setPatients(data);
          setLoading(false);
        }
      } catch (err) {
        setError('Failed to fetch patients');
        setLoading(false);
      }
    };
    const getHighRiskPatients = async () => {
      try {
        if(basicUserInfo !== undefined && basicUserInfo !== null){
          const data = await fetchHighRiskPatients();
          setPatients(data);
          setLoading(false);
        }
      } catch (err) {
        setError('Failed to fetch patients');
        setLoading(false);
      }
    };
    if (basicUserInfo?.role === "Admin"){
      getHighRiskPatients();
    }
    else{
      getPatients();
    }
    
  }, []);

  const handleRowClick = (patient: Patient) => {
    setSelectedPatient(patient);
    setDialogOpen(true);
  };

  const handleCloseDialog = () => {
    setDialogOpen(false);
    setSelectedPatient(null);
  };

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh">
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Typography variant="h6" color="error" align="center">
        {error}
      </Typography>
    );
  }

  return (
    <>
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 650 }} aria-label="patient table">
          <TableHead>
            <TableRow>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>ID</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>Name</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>Email</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>Date of Birth</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>Role</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {patients.map((patient) => (
              <TableRow key={patient.id} onClick={() => handleRowClick(patient)} style={{ cursor: 'pointer' }}>
                <TableCell>{patient.id}</TableCell>
                <TableCell>{`${patient.ime} ${patient.prezime}`}</TableCell>
                <TableCell>{patient.email}</TableCell>
                <TableCell>{new Date(patient.dateOfBirth).toLocaleString()}</TableCell>
                <TableCell>{patient.role}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {selectedPatient && (
        <Dialog open={dialogOpen} onClose={handleCloseDialog}>
          <DialogTitle>Patient Details</DialogTitle>
          <DialogContent>
            <DialogContentText>
              <strong>ID:</strong> {selectedPatient.id}<br />
              <strong>Name:</strong> {`${selectedPatient.ime} ${selectedPatient.prezime}`}<br />
              <strong>Email:</strong> {selectedPatient.email}<br />
              <strong>Date of Birth:</strong> {new Date(selectedPatient.dateOfBirth).toLocaleString()}<br />
              <strong>Role:</strong> {selectedPatient.role}<br />
              <strong>Blood Pressure:</strong> {selectedPatient.bloodPressure}<br />
              <strong>Pulse:</strong> {selectedPatient.puls}<br />
              <strong>Oxygen Saturation:</strong> {selectedPatient.saturationO2}<br />
              <strong>Body Temperature:</strong> {selectedPatient.bodyTemperature}<br />
              <strong>Examinations:</strong> {selectedPatient.examinations.map((exam) => exam.id).join(', ')}
            </DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseDialog} color="primary">Close</Button>
          </DialogActions>
        </Dialog>
      )}
    </>
  );
};

export default PatientTable;
