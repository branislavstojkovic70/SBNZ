import { TestResult } from './TestResult';

export interface ExaminationType {
    id: number;
    name: string;
    testResults: TestResult[];
}
