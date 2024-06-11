package com.ftn.sbnz.service.service.examination;

import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import com.ftn.sbnz.model.models.examinations.Examination;
import com.ftn.sbnz.model.models.examinations.ExaminationType;
import com.ftn.sbnz.model.models.examinations.Symptom;
import com.ftn.sbnz.model.models.examinations.TestResult;
import com.ftn.sbnz.model.models.examinations.UpdatedExamination;
import com.ftn.sbnz.model.models.users.Doctor;
import com.ftn.sbnz.model.models.users.Patient;
import com.ftn.sbnz.service.repository.examination.ExaminationRepository;
import com.ftn.sbnz.service.repository.examination.ExaminationTypeRepository;
import com.ftn.sbnz.service.repository.examination.SymptomRepository;
import com.ftn.sbnz.service.repository.examination.TestResultRepository;
import com.ftn.sbnz.service.repository.users.PatientRepository;
import com.ftn.sbnz.service.service.DroolsService;
import com.ftn.sbnz.service.service.users.DoctorService;

@Service
public class ExaminationService {

    @Autowired
    private ExaminationRepository examinationRepository;

    @Autowired
    private SymptomRepository symptomRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ExaminationTypeRepository examinationTypeRepository;

    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private DroolsService droolsService;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Examination addExaminationForPatient(Integer patientId, Examination examination) {
        Doctor doctor = doctorService.findDoctorById(examination.getDoctor().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid doctor ID"));
        Patient patient = patientRepository.findOneById(patientId);
        if (patient == null) {
            throw new IllegalArgumentException("Invalid patient ID");
        }
        examination.setDoctor(doctor);
        Examination savedExamination = examinationRepository.save(examination);

        patient.getExaminations().add(savedExamination);
        patientRepository.save(patient);

        droolsService.getForward1Ksession().insert(patient);
        droolsService.getForward1Ksession().fireAllRules();

        return examination;
    }

    public Examination getExaminationById(Integer id) {
        return examinationRepository.findById(id).orElse(null);
    }

    @Transactional
    public Examination updateExamination(Integer id, Examination updatedExamination) {
        Optional<Examination> optionalExamination = examinationRepository.findById(id);
        if (optionalExamination.isPresent()) {
            Examination existingExamination = optionalExamination.get();
            existingExamination.setDateTime(updatedExamination.getDateTime());
            existingExamination.setDoctor(attachDoctor(updatedExamination.getDoctor()));
            existingExamination.setNote(updatedExamination.getNote());
            existingExamination.setExaminationState(updatedExamination.getExaminationState());
            existingExamination.setDiagnosis(updatedExamination.getDiagnosis());
            existingExamination.setTherapy(updatedExamination.getTherapy());
            
            // Update Symptoms
            Set<Symptom> updatedSymptoms = updatedExamination.getSymptoms();
            existingExamination.getSymptoms().clear();
            for (Symptom symptom : updatedSymptoms) {
                Symptom existingSymptom;
                if (symptom.getId() == null) {
                    existingSymptom = symptomRepository.save(symptom);
                } else {
                    existingSymptom = symptomRepository.findById(symptom.getId())
                            .orElseThrow(() -> new NotFoundException("Symptom not found with id " + symptom.getId()));
                }
                existingExamination.getSymptoms().add(existingSymptom);
            }

            // Update Examination Types and Test Results
            Set<ExaminationType> updatedExaminationTypes = updatedExamination.getExaminationTypes();
            existingExamination.getExaminationTypes().clear();
            for (ExaminationType type : updatedExaminationTypes) {
                ExaminationType existingType;
                if (type.getId() == null) {
                    existingType = examinationTypeRepository.save(type);
                } else {
                    existingType = examinationTypeRepository.findById(type.getId())
                            .orElseThrow(() -> new NotFoundException("ExaminationType not found with id " + type.getId()));
                }

                existingType.getTestResults().clear();
                for (TestResult result : type.getTestResults()) {
                    TestResult existingResult;
                    if (result.getId() == null) {
                        existingResult = testResultRepository.save(result);
                    } else {
                        existingResult = testResultRepository.findById(result.getId())
                                .orElseThrow(() -> new NotFoundException("TestResult not found with id " + result.getId()));
                    }
                    existingType.getTestResults().add(existingResult);
                }

                existingExamination.getExaminationTypes().add(existingType);
            }

            Patient patient = patientRepository.findPatientByExaminationId(id);
            if (patient != null) {
                droolsService.getForward1Ksession().insert(existingExamination);
                droolsService.getForward1Ksession().insert(patient);
                droolsService.getForward1Ksession().insert(new UpdatedExamination(patient, existingExamination));
                droolsService.getForward1Ksession().fireAllRules();
            }
            return existingExamination;
        }
        return null;
    }

    private Doctor attachDoctor(Doctor doctor) {
        if (doctor != null) {
            return entityManager.merge(doctor);
        }
        return null;
    }

}
