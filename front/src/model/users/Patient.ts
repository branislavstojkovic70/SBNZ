import { Examination } from '../examination/Examination';
import { User } from './User';

export interface Patient extends User {
    bloodPressure: number;
    puls: number;
    saturationO2: number;
    bodyTemperature: number;
    examinations: Examination[];
}
