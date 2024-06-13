import { Patient } from "../users/Patient";
import { Examination } from "./Examination";

export interface DetermineDiagnosis {
    patient: Patient;
    examinations: Examination[];
}