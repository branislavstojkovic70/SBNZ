import { TherapyType } from './TherapyType';
import { TherapyState } from './TherapyState';

export interface Therapy {
    id: number;
    strDateTime: string;
    enDateTime: string;
    description: string;
    therapyType: TherapyType;
    therapyState: TherapyState;
}
