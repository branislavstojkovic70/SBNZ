import { Role } from './Role';

export interface User {
    id: number;
    ime: string;
    prezime: string;
    email: string;
    password: string;
    dateOfBirth: string; 
    role: Role;
}
