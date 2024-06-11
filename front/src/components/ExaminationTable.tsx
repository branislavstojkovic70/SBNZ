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
import { Examination } from '../model/examination/Examination';
import { fetchExaminations } from '../services/examinationService';
import { useAppSelector } from '../hooks/redux-hooks';
import { useNavigate } from 'react-router-dom';

type ExaminationTableProps = {
  fetchType: number;
};

const ExaminationTable: React.FC<ExaminationTableProps> = ({ fetchType }) => {
  const [examinations, setExaminations] = useState<Examination[]>([]);
  const [selectedExamination, setSelectedExamination] = useState<Examination | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [dialogOpen, setDialogOpen] = useState<boolean>(false);
  const basicUserInfo = useAppSelector((state) => state.auth.basicUserInfo);
  const navigate = useNavigate();

  useEffect(() => {
    const getExaminations = async () => {
      try {
        if (basicUserInfo !== undefined && basicUserInfo !== null) {
          const data = await fetchExaminations(basicUserInfo.id, fetchType);
          setExaminations(data);
          setLoading(false);
        }
      } catch (err) {
        setError('Failed to fetch examinations');
        setLoading(false);
      }
    };

    getExaminations();
  }, [basicUserInfo, fetchType]);

  const handleRowClick = (examination: Examination) => {
    setSelectedExamination(examination);
    setDialogOpen(true);
  };

  const handleCloseDialog = () => {
    setDialogOpen(false);
    setSelectedExamination(null);
  };

  const handleUpdateClick = () => {
    if (selectedExamination) {
      navigate('/doctor/updateExamination', { state: { examination: selectedExamination } });
    }
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
        <Table sx={{ minWidth: 650 }} aria-label="examination table">
          <TableHead>
            <TableRow>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>ID</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>DateTime</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>Doctor</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>Note</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>State</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>Types</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>Symptoms</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>Diagnosis</TableCell>
              <TableCell sx={{ backgroundColor: '#007bff', color: 'white' }}>Therapy</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {examinations.map((examination) => (
              <TableRow key={examination.id} onClick={() => handleRowClick(examination)} style={{ cursor: 'pointer' }}>
                <TableCell>{examination.id}</TableCell>
                <TableCell>{new Date(examination.dateTime).toLocaleString()}</TableCell>
                <TableCell>{`${examination.doctor.ime} ${examination.doctor.prezime}`}</TableCell>
                <TableCell>{examination.note}</TableCell>
                <TableCell>{examination.examinationState}</TableCell>
                <TableCell>
                  {examination.examinationTypes?.map((type) => type.name).join(', ') || 'N/A'}
                </TableCell>
                <TableCell>
                  {examination.symptoms.map((symptom) => symptom.name).join(', ')}
                </TableCell>
                <TableCell>{examination.diagnosis ? examination.diagnosis.tumorType : 'N/A'}</TableCell>
                <TableCell>{examination.therapy ? examination.therapy.description : 'N/A'}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {selectedExamination && (
        <Dialog open={dialogOpen} onClose={handleCloseDialog}>
          <DialogTitle>Examination Details</DialogTitle>
          <DialogContent>
            <DialogContentText>
              <strong>ID:</strong> {selectedExamination.id}<br />
              <strong>DateTime:</strong> {new Date(selectedExamination.dateTime).toLocaleString()}<br />
              <strong>Doctor:</strong> {`${selectedExamination.doctor.ime} ${selectedExamination.doctor.prezime}`}<br />
              <strong>Email:</strong> {selectedExamination.doctor.email}<br />
              <strong>Specialization:</strong> {selectedExamination.doctor.specialization}<br />
              <strong>Note:</strong> {selectedExamination.note}<br />
              <strong>State:</strong> {selectedExamination.examinationState}<br />
              <strong>Types:</strong> {selectedExamination.examinationTypes?.map((type) => type.name).join(', ') || 'N/A'}<br />
              <strong>Symptoms:</strong> {selectedExamination.symptoms.map((symptom) => symptom.name).join(', ')}<br />
              {selectedExamination.diagnosis && (
                <>
                  <strong>Diagnosis ID:</strong> {selectedExamination.diagnosis.id}<br />
                  <strong>Tumor Type:</strong> {selectedExamination.diagnosis.tumorType}<br />
                  <strong>Tumor Detected:</strong> {selectedExamination.diagnosis.isTumorDetected ? 'Yes' : 'No'}<br />
                  <strong>TNM Classification:</strong><br />
                  <strong>T:</strong> {selectedExamination.diagnosis.tnmKlassification.tKlassification}<br />
                  <strong>N:</strong> {selectedExamination.diagnosis.tnmKlassification.nKlassification}<br />
                  <strong>M:</strong> {selectedExamination.diagnosis.tnmKlassification.mKlassification}<br />
                  <strong>Date:</strong> {new Date(selectedExamination.diagnosis.tnmKlassification.date).toLocaleString()}<br />
                </>
              )}
              {selectedExamination.therapy && (
                <>
                  <strong>Therapy ID:</strong> {selectedExamination.therapy.id}<br />
                  <strong>Description:</strong> {selectedExamination.therapy.description}<br />
                  <strong>Start DateTime:</strong> {new Date(selectedExamination.therapy.strDateTime).toLocaleString()}<br />
                  <strong>End DateTime:</strong> {new Date(selectedExamination.therapy.enDateTime).toLocaleString()}<br />
                  <strong>Type:</strong> {selectedExamination.therapy.therapyType}<br />
                  <strong>State:</strong> {selectedExamination.therapy.therapyState}<br />
                </>
              )}
            </DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseDialog} color="primary">Close</Button>
            {fetchType === 1 && basicUserInfo?.role === 'Doctor' && (
              <Button onClick={handleUpdateClick} color="secondary">Update</Button>
            )}
          </DialogActions>

        </Dialog>
      )}
    </>
  );
};

export default ExaminationTable;
