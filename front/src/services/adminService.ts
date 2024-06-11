import axiosInstance from "../api/axiosInstance";
import { Doctor } from "../model/users/Doctor";
import { Role } from "../model/users/Role";
interface NewDoctor {
    ime: string;
    prezime: string;
    email: string;
    password: string;
    dateOfBirth: string; 
    role: Role;
    specialization: string;
}
export const addDoctor = async (doctor: NewDoctor) => {
  const response = await axiosInstance.post("/api/admin/new-doctor", doctor);
  return response.data;
};

export const fetchDoctors = async (): Promise<Doctor[]> => {
  const response = await axiosInstance.get("/api/admin/doctors");
  return response.data;
};
