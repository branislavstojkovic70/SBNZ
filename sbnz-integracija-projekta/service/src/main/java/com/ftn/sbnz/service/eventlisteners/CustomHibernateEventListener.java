package com.ftn.sbnz.service.eventlisteners;

import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ftn.sbnz.model.models.alarms.Alarm;
import com.ftn.sbnz.model.models.examinations.Diagnosis;
import com.ftn.sbnz.model.models.examinations.Examination;
import com.ftn.sbnz.model.models.examinations.Symptom;
import com.ftn.sbnz.model.models.examinations.TestResult;
import com.ftn.sbnz.model.models.therapy.Therapy;
import com.ftn.sbnz.model.models.users.Patient;
import com.ftn.sbnz.service.repository.alarms.AlarmRepository;
import com.ftn.sbnz.service.repository.examination.DiagnosisRepository;
import com.ftn.sbnz.service.repository.examination.ExaminationRepository;
import com.ftn.sbnz.service.repository.examination.SymptomRepository;
import com.ftn.sbnz.service.repository.examination.TestResultRepository;
import com.ftn.sbnz.service.repository.therapy.TherapyRepository;
import com.ftn.sbnz.service.repository.users.PatientRepository;

@Component
public class CustomHibernateEventListener implements PostInsertEventListener, PostUpdateEventListener {

    @Autowired
    private ExaminationRepository examinationRepository;
    @Autowired
    private SymptomRepository symptomRepository;
    @Autowired
    private TestResultRepository testResultRepository;
    @Autowired
    private DiagnosisRepository diagnosisRepository;
    @Autowired
    private TherapyRepository therapyRepository;
    @Autowired
    private AlarmRepository alarmRepository;
    @Autowired
    private PatientRepository patientRepository;

    @Override
    @Transactional
    public void onPostInsert(PostInsertEvent event) {
        Object entity = event.getEntity();
        if (entity instanceof Examination) {
            examinationRepository.save((Examination) entity);
        } else if (entity instanceof Symptom) {
            symptomRepository.save((Symptom) entity);
        } else if (entity instanceof TestResult) {
            testResultRepository.save((TestResult) entity);
        } else if (entity instanceof Diagnosis) {
            diagnosisRepository.save((Diagnosis) entity);
        } else if (entity instanceof Therapy) {
            therapyRepository.save((Therapy) entity);
        } else if (entity instanceof Alarm) {
            alarmRepository.save((Alarm) entity);
        } else if (entity instanceof Patient) {
            patientRepository.save((Patient) entity);
        }
    }

    @Override
    @Transactional
    public void onPostUpdate(PostUpdateEvent event) {
        Object entity = event.getEntity();
        if (entity instanceof Examination) {
            examinationRepository.save((Examination) entity);
        } else if (entity instanceof Symptom) {
            symptomRepository.save((Symptom) entity);
        } else if (entity instanceof TestResult) {
            testResultRepository.save((TestResult) entity);
        } else if (entity instanceof Diagnosis) {
            diagnosisRepository.save((Diagnosis) entity);
        } else if (entity instanceof Therapy) {
            therapyRepository.save((Therapy) entity);
        } else if (entity instanceof Alarm) {
            alarmRepository.save((Alarm) entity);
        } else if (entity instanceof Patient) {
            patientRepository.save((Patient) entity);
        }
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }
}

