import axiosInstance from '../api/axiosInstance';
import { Alarm } from '../model/alarms/Alarm';

export const fetchAlarms = async (patientId: number): Promise<Alarm[]> => {
  const response = await axiosInstance.get(`/api/alarms/${patientId}/alarms-for-patient`);
  return response.data;
};
