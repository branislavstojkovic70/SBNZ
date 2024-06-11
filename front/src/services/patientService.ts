import axiosInstance from '../api/axiosInstance';
import { Patient } from '../model/users/Patient';

export const fetchPatients = async (doctorId: number): Promise<Patient[]> => {
  const response = await axiosInstance.get(`/api/doctors/${doctorId}/my-patients`);
  return response.data;
};

export const fetchAllPatients = async (): Promise<Patient[]> => {
  const response = await axiosInstance.get(`/api/patients`);
  return response.data;
};

export const fetchHighRiskPatients = async (): Promise<Patient[]> => {
  const response = await axiosInstance.get(`/api/admin/high-risk-patients`);
  return response.data;
};