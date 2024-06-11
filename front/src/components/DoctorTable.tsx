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
import { Doctor } from '../model/users/Doctor';
import { fetchDoctors } from '../services/adminService';
import { useAppSelector } from '../hooks/redux-hooks';

const DoctorTable: React.FC = () => {
  const [doctors, setDoctors] = useState<Doctor[]>([]);
  const [selectedDoctor, setSelectedDoctor] = useState<Doctor | null>(null);
  const [dialogOpen, setDialogOpen] = useState<boolean>(false);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const basicUserInfo = useAppSelector((state) => state.auth.basicUserInfo);

  useEffect(() => {
    const getDoctors = async () => {
      try {
        if (basicUserInfo !== undefined && basicUserInfo !== null) {
          const data = await fetchDoctors();
          setDoctors(data);
          setLoading(false);
        }
      } catch (err) {
        setError('Failed to fetch doctors');
        setLoading(false);
      }
    };

    getDoctors();
  }, [basicUserInfo]);

  const handleRowClick = (doctor: Doctor) => {
    setSelectedDoctor(doctor);
    setDialogOpen(true);
  };

  const handleCloseDialog = () => {
    setDialogOpen(false);
    setSelectedDoctor(null);
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
        <Table sx={{ minWidth: 650 }} aria-label="doctor table">
          <TableHead>
            <TableRow>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>ID</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>First Name</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>Last Name</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>Email</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>Specialization</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {doctors.map((doctor) => (
              <TableRow key={doctor.id} onClick={() => handleRowClick(doctor)} style={{ cursor: 'pointer' }}>
                <TableCell>{doctor.id}</TableCell>
                <TableCell>{doctor.ime}</TableCell>
                <TableCell>{doctor.prezime}</TableCell>
                <TableCell>{doctor.email}</TableCell>
                <TableCell>{doctor.specialization}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {selectedDoctor && (
        <Dialog open={dialogOpen} onClose={handleCloseDialog}>
          <DialogTitle>Doctor Details</DialogTitle>
          <DialogContent>
            <DialogContentText>
              <strong>ID:</strong> {selectedDoctor.id}<br />
              <strong>First Name:</strong> {selectedDoctor.ime}<br />
              <strong>Last Name:</strong> {selectedDoctor.prezime}<br />
              <strong>Email:</strong> {selectedDoctor.email}<br />
              <strong>Date of Birth:</strong> {new Date(selectedDoctor.dateOfBirth).toLocaleDateString()}<br />
              <strong>Role:</strong> {selectedDoctor.role}<br />
              <strong>Specialization:</strong> {selectedDoctor.specialization}<br />
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

export default DoctorTable;
