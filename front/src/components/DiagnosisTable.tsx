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
import { Diagnosis } from '../model/examination/Diagnosis';
import { fetchDiagnoses } from '../services/examinationService';

const DiagnosisTable: React.FC = () => {
  const [diagnoses, setDiagnoses] = useState<Diagnosis[]>([]);
  const [selectedDiagnosis, setSelectedDiagnosis] = useState<Diagnosis | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [dialogOpen, setDialogOpen] = useState<boolean>(false);

  useEffect(() => {
    const getDiagnoses = async () => {
      try {
        const data = await fetchDiagnoses(3);
        setDiagnoses(data);
        setLoading(false);
      } catch (err) {
        setError('Failed to fetch diagnoses');
        setLoading(false);
      }
    };

    getDiagnoses();
  }, []);

  const handleRowClick = (diagnosis: Diagnosis) => {
    setSelectedDiagnosis(diagnosis);
    setDialogOpen(true);
  };

  const handleCloseDialog = () => {
    setDialogOpen(false);
    setSelectedDiagnosis(null);
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
        <Table sx={{ minWidth: 650 }} aria-label="diagnosis table">
          <TableHead>
            <TableRow>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>ID</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>Tumor Detected</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>Tumor Type</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>T Classification</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>N Classification</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>M Classification</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>Date</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {diagnoses.map((diagnosis) => (
              <TableRow key={diagnosis.id} onClick={() => handleRowClick(diagnosis)} style={{ cursor: 'pointer' }}>
                <TableCell>{diagnosis.id}</TableCell>
                <TableCell>{diagnosis.isTumorDetected ? 'Yes' : 'No'}</TableCell>
                <TableCell>{diagnosis.tumorType}</TableCell>
                <TableCell>{diagnosis.tnmKlassification.tKlassification}</TableCell>
                <TableCell>{diagnosis.tnmKlassification.nKlassification}</TableCell>
                <TableCell>{diagnosis.tnmKlassification.mKlassification}</TableCell>
                <TableCell>{new Date(diagnosis.tnmKlassification.date).toLocaleString()}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {selectedDiagnosis && (
        <Dialog open={dialogOpen} onClose={handleCloseDialog}>
          <DialogTitle>Diagnosis Details</DialogTitle>
          <DialogContent>
            <DialogContentText>
              <strong>ID:</strong> {selectedDiagnosis.id}<br />
              <strong>Tumor Detected:</strong> {selectedDiagnosis.isTumorDetected ? 'Yes' : 'No'}<br />
              <strong>Tumor Type:</strong> {selectedDiagnosis.tumorType}<br />
              <strong>TNM Classification:</strong><br />
              <strong>T:</strong> {selectedDiagnosis.tnmKlassification.tKlassification}<br />
              <strong>N:</strong> {selectedDiagnosis.tnmKlassification.nKlassification}<br />
              <strong>M:</strong> {selectedDiagnosis.tnmKlassification.mKlassification}<br />
              <strong>Date:</strong> {new Date(selectedDiagnosis.tnmKlassification.date).toLocaleString()}
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

export default DiagnosisTable;
