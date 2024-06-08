import { Therapy } from './Therapy';

export interface RadioTherapy extends Therapy {
    rayType: string;
    medicineDose: number;
    applicationRegion: string;
}
