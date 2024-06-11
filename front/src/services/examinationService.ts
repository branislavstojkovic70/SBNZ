import axiosInstance from '../api/axiosInstance';
import { Diagnosis } from '../model/examination/Diagnosis';
import { Examination } from '../model/examination/Examination';
import { ExaminationState } from '../model/examination/ExaminationState';
import { Therapy } from '../model/therpy/Therapy';
import { Doctor } from '../model/users/Doctor';
import { Patient } from '../model/users/Patient';

export const fetchExaminations = async (patientId: number, fetchType: number): Promise<Examination[]> => {
  switch (fetchType) {
    case 1:
      const response2 = await axiosInstance.get(`/api/doctors/${patientId}/scheduled-examinations`);
      console.log(patientId);
      console.log(response2);
      return response2.data;
      break;
    case 2:
      const response1 = await axiosInstance.get(`/api/doctors/${patientId}/completed-examinations`);
      console.log(patientId);
      console.log(response1);
      return response1.data;
      break;

    case 3:
      const response3 = await axiosInstance.get(`/api/patients/${patientId}/examinations/scheduled`);
      return response3.data;
      break;
    case 4:
      const response4 = await axiosInstance.get(`/api/patients/${patientId}/examinations/completed`);
      return response4.data;
      break;
    default:
      throw new Error('Invalid fetch type');
  }

};

export const fetchDiagnoses = async (patientId: number): Promise<Diagnosis[]> => {
  const response = await axiosInstance.get(`/api/patients/${patientId}/diagnoses`);
  return response.data;
};

export const fetchTherapies = async (patientId: number): Promise<Therapy[]> => {
  const response = await axiosInstance.get(`/api/patients/${patientId}/therapies`);
  return response.data;
};

export const addExamination = async (selectedPatient : Patient, doctor : Doctor, datetime:string, note:string): Promise<Examination> => {
  const examination: Examination = {
    dateTime: datetime,
    doctor: doctor,
    note: note,
    examinationState: ExaminationState.SCHEDULED,
    symptoms: [],
  };
  const response = await axiosInstance.post(`/api/examinations/patient/${selectedPatient.id}`, examination);
  return response.data;
}

export const updateExamination = async (updatedExamination: Examination): Promise<Examination> => {
  const response = await axiosInstance.put(`/api/examinations/${updatedExamination.id}`, updatedExamination);
  return response.data;
};
