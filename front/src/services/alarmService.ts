import axiosInstance from '../api/axiosInstance';
import { Alarm } from '../model/alarms/Alarm';

export const fetchAlarms = async (patientId: number): Promise<Alarm[]> => {
  const response = await axiosInstance.get(`/api/alarms/${patientId}/alarms-for-patient`);
  return response.data;
};

export const simulateParentHasCancer = async (): Promise<string> => {
  try {
      const response = await axiosInstance.put('/api/alarms/simulate-parent-has-cancer');
      return response.data;
  } catch (error: any) {
      throw new Error(error.response?.data || 'An error occurred during the simulation.');
  }
};

export const simulateHypoxia = async (): Promise<string> => {
  try {
      const response = await axiosInstance.put('/api/alarms/simulate-hypoxia');
      return response.data;
  } catch (error: any) {
      throw new Error(error.response?.data || 'An error occurred during the simulation.');
  }
};

export const simulateForward2 = async (): Promise<string> => {
  try {
      const response = await axiosInstance.put('/api/alarms/simulate-symptom-aggravation-after-operation');
      return response.data;
  } catch (error: any) {
      throw new Error(error.response?.data || 'An error occurred during the simulation.');
  }
};

export const simulateSymptomAggravation = async (): Promise<string> => {
  try {
      const response = await axiosInstance.put('/api/alarms/simulate-symptom-aggravation');
      return response.data;
  } catch (error: any) {
      throw new Error(error.response?.data || 'An error occurred during the simulation.');
  }
};