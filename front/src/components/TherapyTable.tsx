// src/components/TherapyTable.tsx
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
import axiosInstance from '../api/axiosInstance';
import { Therapy } from '../model/therpy/Therapy';
import { fetchTherapies } from '../services/examinationService';

const TherapyTable: React.FC = () => {
  const [therapies, setTherapies] = useState<Therapy[]>([]);
  const [selectedTherapy, setSelectedTherapy] = useState<Therapy | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [dialogOpen, setDialogOpen] = useState<boolean>(false);

  useEffect(() => {
    const getTherapies = async () => {
      try {
        const data = await fetchTherapies(3);
        setTherapies(data);
        setLoading(false);
      } catch (err) {
        setError('Failed to fetch therapies');
        setLoading(false);
      }
    };

    getTherapies();
  }, []);

  const handleRowClick = (therapy: Therapy) => {
    setSelectedTherapy(therapy);
    setDialogOpen(true);
  };

  const handleCloseDialog = () => {
    setDialogOpen(false);
    setSelectedTherapy(null);
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
        <Table sx={{ minWidth: 650 }} aria-label="therapy table">
          <TableHead>
            <TableRow>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>ID</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>Start DateTime</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>End DateTime</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>Description</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>Therapy Type</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>Therapy State</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {therapies.map((therapy) => (
              <TableRow key={therapy.id} onClick={() => handleRowClick(therapy)} style={{ cursor: 'pointer' }}>
                <TableCell>{therapy.id}</TableCell>
                <TableCell>{new Date(therapy.strDateTime).toLocaleString()}</TableCell>
                <TableCell>{new Date(therapy.enDateTime).toLocaleString()}</TableCell>
                <TableCell>{therapy.description}</TableCell>
                <TableCell>{therapy.therapyType}</TableCell>
                <TableCell>{therapy.therapyState}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {selectedTherapy && (
        <Dialog open={dialogOpen} onClose={handleCloseDialog}>
          <DialogTitle>Therapy Details</DialogTitle>
          <DialogContent>
            <DialogContentText>
              <strong>ID:</strong> {selectedTherapy.id}<br />
              <strong>Start DateTime:</strong> {new Date(selectedTherapy.strDateTime).toLocaleString()}<br />
              <strong>End DateTime:</strong> {new Date(selectedTherapy.enDateTime).toLocaleString()}<br />
              <strong>Description:</strong> {selectedTherapy.description}<br />
              <strong>Therapy Type:</strong> {selectedTherapy.therapyType}<br />
              <strong>Therapy State:</strong> {selectedTherapy.therapyState}
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

export default TherapyTable;
