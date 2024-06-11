import { Therapy } from './Therapy';
import { OperationType } from './OperationType';

export interface Operation extends Therapy {
    scheduledFor: string;
    outcome: string;
    operationType: OperationType;
}
