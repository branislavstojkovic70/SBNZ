import { SymptomFrequency } from './SymptomFrequency';

export interface Symptom {
    id?: number;
    name: string;
    description: string;
    intensity: number;
    symptomFrequency: SymptomFrequency;
}
