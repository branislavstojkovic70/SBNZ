package com.ftn.sbnz.service.service.examination;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ftn.sbnz.model.models.examinations.Examination;
import com.ftn.sbnz.model.models.users.Doctor;
import com.ftn.sbnz.model.models.users.Patient;
import com.ftn.sbnz.service.repository.examination.ExaminationRepository;
import com.ftn.sbnz.service.repository.users.PatientRepository;
import com.ftn.sbnz.service.service.DroolsService;
import com.ftn.sbnz.service.service.users.DoctorService;

@Service
public class ExaminationService {

    @Autowired
    private ExaminationRepository examinationRepository;

    @Autowired
    private PatientRepository patientRepository;

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
        Examination createdExamination = examinationRepository.save(examination);
        droolsService.insertAndFireAllRules(examination);

        patient.getExaminations().add(createdExamination);
        patientRepository.save(patient);

        return createdExamination;
    }

    public Examination getExaminationById(Integer id) {
        return examinationRepository.findById(id).orElse(null);
    }

    @Transactional
    public Examination updateExamination(Integer id, Examination updatedExamination) {
        Optional<Examination> optionalExamination = examinationRepository.findById(id);
        if (optionalExamination.isPresent()) {
            Examination examination = optionalExamination.get();
            examination.setDateTime(updatedExamination.getDateTime());
            examination.setDoctor(attachDoctor(updatedExamination.getDoctor()));
            examination.setNote(updatedExamination.getNote());
            examination.setExaminationState(updatedExamination.getExaminationState());
            examination.setExaminationTypes(updatedExamination.getExaminationTypes());
            examination.setSymptoms(updatedExamination.getSymptoms());
            examination.setDiagnosis(updatedExamination.getDiagnosis());
            examination.setTherapy(updatedExamination.getTherapy());
            droolsService.insertAndFireAllRules(examination);
            return examinationRepository.save(examination);
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
