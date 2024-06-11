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
import { Alarm } from '../model/alarms/Alarm';
import { fetchAlarms } from '../services/alarmService';
import { useAppSelector } from '../hooks/redux-hooks';

const AlarmTable: React.FC = () => {
  const [alarms, setAlarms] = useState<Alarm[]>([]);
  const [selectedAlarm, setSelectedAlarm] = useState<Alarm | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [dialogOpen, setDialogOpen] = useState<boolean>(false);
  const basicUserInfo = useAppSelector((state) => state.auth.basicUserInfo);

  useEffect(() => {
    const getAlarms = async () => {
      try {
        if(basicUserInfo !== undefined && basicUserInfo !== null){
          const data = await fetchAlarms(basicUserInfo.id);
          setAlarms(data);
          setLoading(false);
        }
        
      } catch (err) {
        setError('Failed to fetch alarms');
        setLoading(false);
      }
    };

    getAlarms();
  }, []);

  const handleRowClick = (alarm: Alarm) => {
    setSelectedAlarm(alarm);
    setDialogOpen(true);
  };

  const handleCloseDialog = () => {
    setDialogOpen(false);
    setSelectedAlarm(null);
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
        <Table sx={{ minWidth: 650 }} aria-label="alarm table">
          <TableHead>
            <TableRow>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>ID</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>Patient Name</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>Description</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>Time</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {alarms.map((alarm) => (
              <TableRow key={alarm.id} onClick={() => handleRowClick(alarm)} style={{ cursor: 'pointer' }}>
                <TableCell>{alarm.id}</TableCell>
                <TableCell>{`${alarm.patient.ime} ${alarm.patient.prezime}`}</TableCell>
                <TableCell>{alarm.description}</TableCell>
                <TableCell>{new Date(alarm.time).toLocaleString()}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {selectedAlarm && (
        <Dialog open={dialogOpen} onClose={handleCloseDialog}>
          <DialogTitle>Alarm Details</DialogTitle>
          <DialogContent>
            <DialogContentText>
              <strong>ID:</strong> {selectedAlarm.id}<br />
              <strong>Description:</strong> {selectedAlarm.description}<br />
              <strong>Time:</strong> {new Date(selectedAlarm.time).toLocaleString()}<br />
              <strong>Patient Details:</strong><br />
              <strong>ID:</strong> {selectedAlarm.patient.id}<br />
              <strong>Name:</strong> {`${selectedAlarm.patient.ime} ${selectedAlarm.patient.prezime}`}<br />
              <strong>Email:</strong> {selectedAlarm.patient.email}<br />
              <strong>Date of Birth:</strong> {new Date(selectedAlarm.patient.dateOfBirth).toLocaleString()}<br />
              <strong>Role:</strong> {selectedAlarm.patient.role}<br />
              <strong>Blood Pressure:</strong> {selectedAlarm.patient.bloodPressure}<br />
              <strong>Pulse:</strong> {selectedAlarm.patient.puls}<br />
              <strong>Oxygen Saturation:</strong> {selectedAlarm.patient.saturationO2}<br />
              <strong>Body Temperature:</strong> {selectedAlarm.patient.bodyTemperature}<br />
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

export default AlarmTable;
