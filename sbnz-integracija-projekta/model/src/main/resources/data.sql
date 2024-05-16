INSERT INTO User (ime, prezime, email, password, dateOfBirth, role, bloodPressure, puls, saturationO2, bodyTemperature) VALUES
('Ana', 'Anić', 'ana.anic@example.com', 'hash', '1990-05-21 00:00:00', 'Patient', 120.0, 80, 98.6, 36.6),
('Branislav', 'Stojkovic', 'bane@gmail.com', 'hash', '2001-04-12 00:00:00', 'Patient', 118.0, 90, 95.6, 37.6);

INSERT INTO User (ime, prezime, email, password, dateOfBirth, role, specialization) VALUES
('Marko', 'Marković', 'marko.markovic@example.com', 'hashed_password', '1985-03-15 00:00:00', 'Doctor', 'Pulmology'),
('Janko', 'Janković', 'janko@example.com', 'hashed_password', '1985-03-15 00:00:00', 'Doctor', 'Pulmology');

INSERT INTO User (ime, prezime, email, password, dateOfBirth, role) VALUES
('Jovan', 'Jovanović', 'jovan.jovanovic@example.com', 'hashed_password', '1975-11-30 00:00:00', 'Admin'),
('Petar', 'Jovanović', 'pera@example.com', 'hashed_password', '1975-11-30 00:00:00', 'Admin');

INSERT INTO RadioTherapy (from, to, description, therapyType, therapyState, rayType, medicineDose, applicationRegion) VALUES
('2024-05-01 00:00:00', '2024-06-01 00:00:00', 'Initial treatment for lung cancer', 'RADIOTHERAPY', 'PLANNED', 'X-Ray', 120.0, 'Chest'),
('2024-08-01 00:00:00', '2024-09-01 00:00:00', 'Secondary treatment for lung cancer', 'RADIOTHERAPY', 'DURING', 'Proton', 100.0, 'Upper Chest');

INSERT INTO PaliativeCare (from, to, description, therapyType, therapyState, applicationMethod) VALUES
('2024-05-15 00:00:00', '2024-05-30 00:00:00', 'Comfort care for terminal patients', 'PALLIATIVE_CARE', 'PLANNED', 'Intravenous Medication'),
('2024-10-15 00:00:00', '2024-11-01 00:00:00', 'Maintenance care for chronic conditions', 'PALLIATIVE_CARE', 'FINISHED', 'Oral Medication');

INSERT INTO Operation (from, to, description, therapyType, therapyState, scheduledFor, outcome, operationType) VALUES
('2024-07-01 00:00:00', '2024-07-15 00:00:00', 'Complete lung removal', 'OPERATION', 'PLANNED', '2024-07-02 00:00:00', 'Successful', 'PNEUMONECTOMY'),
('2024-09-01 00:00:00', '2024-09-10 00:00:00', 'Partial lung removal', 'OPERATION', 'DURING', '2024-09-02 00:00:00', 'Pending', 'LOBECTOMY');

INSERT INTO ChemoTherapy (from, to, description, therapyType, therapyState, protocol, medicine, dose, durationInDays, adverseEffectsDescription) VALUES
('2024-06-01 00:00:00', '2024-08-01 00:00:00', 'Primary chemotherapy for aggressive lung cancer', 'CHEMOTHERAPY', 'PLANNED', 'Standard Protocol A', 'Cisplatin', 75.0, 60, 'Nausea, hair loss'),
('2024-11-01 00:00:00', '2024-12-15 00:00:00', 'Adjuvant chemotherapy for residual cancer cells', 'CHEMOTHERAPY', 'DURING', 'Standard Protocol B', 'Carboplatin', 50.0, 45, 'Fatigue, neuropathy');

INSERT INTO TestResult (name, measureUnit, value, description) VALUES
('Blood Pressure', 'mmHg', 120.0, 'Normal systolic blood pressure'),
('Blood Sugar', 'mg/dL', 104.0, 'Normal fasting glucose level');

INSERT INTO Symptom (name, description, intensity, symptomFrequency) VALUES
('Cough', 'Persistent dry cough', 3, 'CONSTANT'),
('Fatigue', 'General feeling of tiredness', 2, 'FREQUENT');

INSERT INTO TNMKlassification (tKlassification, nKlassification, mKlassification, date) VALUES
(2.0, 0.0, 0.0, '2024-05-01 00:00:00'),
(3.0, 1.0, 0.0, '2024-05-02 00:00:00');

INSERT INTO Diagnosis (isTumorDetected, tumorType, tnm_id) VALUES
(TRUE, 'INTRATHORACAL_PULMONARY', 1),
(TRUE, 'INTRATHORACAL_NONPULMONARY', 2);

INSERT INTO Examination (dateTime, doctor_id, note, examinationState, diagnosis_id, therapy_id, patient_id) VALUES
('2024-05-03 09:00:00', 1, 'Routine follow-up examination.', 'SCHEDULED', 1, 1, 1),
('2024-05-04 09:00:00', 2, 'Detailed diagnostic examination.', 'DONE', 2, 2, 2);
