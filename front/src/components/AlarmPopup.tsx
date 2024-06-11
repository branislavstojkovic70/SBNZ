import React from 'react';
import { Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions, Button } from '@mui/material';

interface AlarmPopupProps {
  open: boolean;
  onClose: () => void;
  description: string;
}

const AlarmPopup: React.FC<AlarmPopupProps> = ({ open, onClose, description }) => {
  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>Alarm</DialogTitle>
      <DialogContent>
        <DialogContentText>{description}</DialogContentText>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} color="primary">
          Close
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default AlarmPopup;
