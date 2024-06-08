import { ExaminationState } from './ExaminationState';
import { ExaminationType } from './ExaminationType';
import { Symptom } from './Symptom';
import { Diagnosis } from './Diagnosis';
import { Doctor } from '../users/Doctor';
import { Therapy } from '../therpy/Therapy';

export interface Examination {
    id?: number;
    dateTime: string;
    doctor: Doctor;
    note: string;
    examinationState: ExaminationState;
    examinationTypes?: ExaminationType[];
    symptoms: Symptom[];
    diagnosis?: Diagnosis;
    therapy?: Therapy;
}
