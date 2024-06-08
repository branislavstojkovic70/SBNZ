import { TumorType } from './TumorType';
import { TNMKlassification } from './TNMKlassification';

export interface Diagnosis {
    id: number;
    isTumorDetected: boolean;
    tumorType: TumorType;
    tnmKlassification: TNMKlassification;
}
