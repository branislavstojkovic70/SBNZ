package com.ftn.sbnz.service.service.users;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.sbnz.model.models.examinations.Diagnosis;
import com.ftn.sbnz.model.models.examinations.Examination;
import com.ftn.sbnz.model.models.examinations.ExaminationState;
import com.ftn.sbnz.model.models.therapy.Therapy;
import com.ftn.sbnz.model.models.users.Patient;
import com.ftn.sbnz.service.repository.users.PatientRepository;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public Optional<Patient> findById(Integer id) {
        return patientRepository.findById(id);
    }

    public Patient save(Patient patient) {
        Patient savedPatient = patientRepository.save(patient);
        return savedPatient;
    }

    public void deleteById(Integer id) {
        patientRepository.deleteById(id);
    }

    public List<Examination> getScheduledExaminationsByPatientId(Integer patientId) {
        Patient patient = findById(patientId).get();
        if (patient != null) {
            return patient.getExaminations().stream()
                    .filter(examination -> examination.getExaminationState() == ExaminationState.SCHEDULED)
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<Examination> getCompletedExaminationsByPatientId(Integer patientId) {
        Patient patient = findById(patientId).get();
        if (patient != null) {
            return patient.getExaminations().stream()
                    .filter(examination -> examination.getExaminationState() == ExaminationState.DONE)
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<Diagnosis> getDiagnosesByPatientId(Integer patientId) {
        Patient patient = findById(patientId).get();
        if (patient != null) {
            return patient.getExaminations().stream()
                    .map(examination -> examination.getDiagnosis())
                    .filter(diagnosis -> diagnosis != null)
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<Therapy> getTherapiesByPatientId(Integer patientId) {
        Patient patient = findById(patientId).get();
        if (patient != null) {
            return patient.getExaminations().stream()
                    .map(examination -> examination.getTherapy())
                    .filter(therapy -> therapy != null)
                    .collect(Collectors.toList());
        }
        return null;
    }

    public Patient getPatientByExaminationId(Integer examinationId) {
        return patientRepository.findPatientByExaminationId(examinationId);
    }

    public List<Patient> getPatientsWithOperationTherapy() {
        return patientRepository.findAllPatientsWithOperationTherapy();
    }
}