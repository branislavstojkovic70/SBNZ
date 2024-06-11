import axiosInstance from '../api/axiosInstance';
import { Doctor } from '../model/users/Doctor';

export const findDoctorById = async (doctorId: number): Promise<Doctor> => {
  const response = await axiosInstance.get(`/api/doctors/${doctorId}`);
  return response.data;
};