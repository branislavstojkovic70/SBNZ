import { Therapy } from './Therapy';

export interface ChemoTherapy extends Therapy {
    protocol: string;
    medicine: string;
    dose: number;
    durationInDays: number;
    adverseEffectsDescription: string;
}
