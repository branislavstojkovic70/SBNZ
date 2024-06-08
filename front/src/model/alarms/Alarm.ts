import { Patient } from "../users/Patient";

export interface Alarm {
    id: number;
    patient: Patient;
    description: string;
    time: string;
}
