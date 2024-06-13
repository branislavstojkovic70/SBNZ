package com.ftn.sbnz.service.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.drools.core.time.SessionPseudoClock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;

import com.ftn.sbnz.model.events.PulseEvent;
import com.ftn.sbnz.model.events.TemperatureEvent;
import com.ftn.sbnz.model.models.users.Patient;
import com.ftn.sbnz.model.models.users.Role;

import org.springframework.stereotype.Service;

import com.ftn.sbnz.model.events.OxygenSaturationEvent;
import com.ftn.sbnz.model.models.Fact;
import com.ftn.sbnz.model.models.examinations.*;
import com.ftn.sbnz.model.models.therapy.*;
import com.ftn.sbnz.model.models.users.Doctor;
import com.ftn.sbnz.model.models.users.OperatedPatient;
import com.ftn.sbnz.model.models.users.Patient;
import com.ftn.sbnz.service.repository.examination.ExaminationRepository;
import com.ftn.sbnz.service.repository.users.DoctorRepository;
import com.ftn.sbnz.service.repository.users.PatientRepository;

@Service
public class SimulationService {

        private final PatientRepository patientRepository;
        private final DoctorRepository doctorRepository;
        private final DroolsService droolsService;
        private final EntityManager entityManager;
        private final ExaminationRepository examinationRepository;

        @Autowired
        public SimulationService(PatientRepository patientRepository,
                        DoctorRepository doctorRepository,
                        DroolsService droolsService, EntityManager entityManager, ExaminationRepository examinationRepository) {
                this.entityManager = entityManager;
                this.patientRepository = patientRepository;
                this.doctorRepository = doctorRepository;
                this.droolsService = droolsService;
                this.examinationRepository = examinationRepository;
        }

        @Transactional
        public void testTemplateSymptomAggravation(Integer patientId) {
                SessionPseudoClock clock = (SessionPseudoClock) this.droolsService.getTemplate1Ksession()
                                .getSessionClock();

                Patient p = this.patientRepository.findById(patientId)
                                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));
                p = this.entityManager.merge(p);
                Doctor d = this.doctorRepository.findById(2)
                                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

                Random random = new Random();
                Symptom symptom1 = new Symptom(null, "symptom1", "s1", random.nextInt(10), SymptomFrequency.CONSTANT);
                Symptom symptom2 = new Symptom(null, "symptom2", "s2", random.nextInt(10), SymptomFrequency.CONSTANT);
                Symptom symptom3 = new Symptom(null, "symptom3", "s3", random.nextInt(10), SymptomFrequency.CONSTANT);

                Examination examination = new Examination(null, LocalDateTime.now(), d, "Urgent examination",
                                ExaminationState.DONE,
                                new HashSet<>(), new HashSet<>(List.of(symptom1, symptom2, symptom3)),
                                new Diagnosis(null, false, null, null),
                                new Therapy(null, LocalDateTime.now(), LocalDateTime.now().plusMonths(1),
                                                "Urgent examination therapy",
                                                TherapyType.PALLIATIVE_CARE, TherapyState.DURING));

                Set<Examination> examinations = p.getExaminations();
                examinations.add(examination);
                p.setExaminations(examinations);

                this.droolsService.getTemplate1Ksession().insert(p);
                this.droolsService.getTemplate1Ksession().insert(examination);
                this.droolsService.getTemplate1Ksession().insert(symptom1);
                this.droolsService.getTemplate1Ksession().insert(symptom2);
                this.droolsService.getTemplate1Ksession().insert(symptom3);

                clock.advanceTime(10, TimeUnit.MINUTES);
                clock.advanceTime(5, TimeUnit.MINUTES);
                clock.advanceTime(1, TimeUnit.MINUTES);

                this.droolsService.getTemplate1Ksession().fireAllRules();
        }

        @Transactional
        public void testTemplateHypoxia(Integer patientId) {
                SessionPseudoClock clock = (SessionPseudoClock) this.droolsService.getTemplate2Ksession()
                                .getSessionClock();

                Patient p = this.patientRepository.findById(patientId)
                                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));
                p = this.entityManager.merge(p);
                Random random = new Random();
                for (int i = 0; i < 5; i++) {
                        int saturationLevel = 80 + random.nextInt(10);
                        OxygenSaturationEvent event = new OxygenSaturationEvent(p, saturationLevel,
                                        new Date(clock.getCurrentTime()));
                        this.droolsService.getTemplate2Ksession().insert(event);
                        clock.advanceTime(1, TimeUnit.HOURS);
                }

                this.droolsService.getTemplate2Ksession().insert(p);
                this.droolsService.getTemplate2Ksession().fireAllRules();
        }

        @Scheduled(fixedRate = 600000)
        @Transactional
        public void processRules() {
                Random random = new Random();
                SessionPseudoClock clock = this.droolsService.getCepKsession().getSessionClock();

                Patient patient = this.patientRepository.save(
                                new Patient(null, "Novi", "Pacijent", "novi@gmail.com" + random.nextInt(10000),
                                                "novi", LocalDateTime.now().minusYears(30), Role.Patient, 120, 100, 95,
                                                36.5, new HashSet<>()));

                Doctor d = this.doctorRepository.findById(2)
                                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

                TemperatureEvent tempEvent1 = new TemperatureEvent(patient, 39.0, new Date());
                this.droolsService.getCepKsession().insert(tempEvent1);
                clock.advanceTime(1, TimeUnit.HOURS);

                TemperatureEvent tempEvent2 = new TemperatureEvent(patient, 39.5, new Date());
                this.droolsService.getCepKsession().insert(tempEvent2);
                clock.advanceTime(1, TimeUnit.HOURS);

                PulseEvent pulseEvent1 = new PulseEvent(patient, 110, new Date());
                this.droolsService.getCepKsession().insert(pulseEvent1);
                clock.advanceTime(1, TimeUnit.HOURS);

                PulseEvent pulseEvent2 = new PulseEvent(patient, 115, new Date());
                this.droolsService.getCepKsession().insert(pulseEvent2);
                clock.advanceTime(1, TimeUnit.HOURS);

                Operation operation = new Operation(null, LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                                "Operation1",
                                TherapyType.OPERATION, TherapyState.PLANNED, "desc2", LocalDateTime.now().plusHours(12),
                                "", OperationType.LOBECTOMY);

                Examination examination = new Examination(null, LocalDateTime.now(), d, "note",
                                ExaminationState.DONE, new HashSet<ExaminationType>(), new HashSet<Symptom>(),
                                null, operation);

                Set<Examination> examinations = patient.getExaminations();
                examinations.add(examination);
                patient.setExaminations(examinations);
                this.droolsService.getCepKsession().insert(patient);
                this.droolsService.getCepKsession().insert(examination);
                this.droolsService.getCepKsession().insert(operation);

                this.droolsService.getCepKsession().fireAllRules();
                patient = this.patientRepository.save(patient);
        }

        @Transactional
        public void simulateAlarmForParentServiceMethod(Integer childId) {
                Patient child = this.patientRepository.findById(childId).get();
                Random r = new Random();
                Patient parent = this.patientRepository
                                .save(new Patient(null, "Marko", "Markovic", "parent11@example.com" + r.nextInt(100000),
                                                "parent",
                                                LocalDateTime.now().minusYears(60), Role.Patient, 120.0, 70, 98.0, 36.5,
                                                new HashSet<>()));
                Doctor doctor = this.doctorRepository.findById(2).get();
                Examination examination = new Examination(null, LocalDateTime.now(), attachDoctor(doctor),
                                "Tumor is detected",
                                ExaminationState.DONE, new HashSet<ExaminationType>(), new HashSet<Symptom>(),
                                new Diagnosis(null, true, TumorType.EXTRATORACAL_NONPULMONARY,
                                                new TNMKlassification(null, 1.5, 2.0, 3.5, LocalDateTime.now())),
                                new Therapy());

                HashSet<Examination> examinations = new HashSet<>();
                examinations.add(examination);
                parent.setExaminations(examinations);

                this.droolsService.getBwKsession().insert(parent);
                this.droolsService.getBwKsession().insert(child);
                this.droolsService.getBwKsession().insert(new Fact(child, parent));
                this.droolsService.getBwKsession().insert(new UpdatedExamination(parent, examination));
                this.droolsService.getBwKsession().fireAllRules();
        }

        @Transactional
        public void testForward2(Integer patientId) {
                Patient p = this.patientRepository.findById(patientId)
                                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));
                p = this.entityManager.merge(p);

                List<Examination> allExaminations = new ArrayList<>(); // List to collect all examinations

                try {
                        // Create Patient
                        Patient patient = new Patient();
                        Set<Examination> examinations = new HashSet<>();
                        patient.setExaminations(examinations);

                        // Create OperatedPatient
                        Examination operationExamination = new Examination();
                        operationExamination.setExaminationState(ExaminationState.DONE);
                        operationExamination.setNote("operation");

                        Operation operation = new Operation();
                        operationExamination.setTherapy(operation);

                        examinations.add(operationExamination);
                        allExaminations.add(operationExamination); // Add to the list

                        OperatedPatient operatedPatient = new OperatedPatient();
                        operatedPatient.setPatient(patient);
                        operatedPatient.setOperation(operation);
                        operatedPatient.setPulmonaryResistance(false);
                        operatedPatient.setPulmonaryHypertension(false);

                        droolsService.getForward2Ksession().insert(patient);
                        droolsService.getForward2Ksession().insert(operationExamination);
                        droolsService.getForward2Ksession().insert(operation);
                        droolsService.getForward2Ksession().insert(operatedPatient);

                        // Check for resistance in pulmonary blood vessels
                        Examination ultrasoundExamination = new Examination();
                        ultrasoundExamination.setExaminationState(ExaminationState.DONE);
                        ultrasoundExamination.setNote("ultrasound");

                        TestResult ultrasoundResult = new TestResult();
                        ultrasoundResult.setDescription("pulmonary artery pressure");
                        ultrasoundResult.setValue(30.0);

                        ExaminationType ultrasoundType = new ExaminationType();
                        Set<TestResult> ultrasoundResults = new HashSet<>();
                        ultrasoundResults.add(ultrasoundResult);
                        ultrasoundType.setTestResults(ultrasoundResults);
                        Set<ExaminationType> ultrasoundTypes = new HashSet<>();
                        ultrasoundTypes.add(ultrasoundType);
                        ultrasoundExamination.setExaminationTypes(ultrasoundTypes);

                        Examination spiroergometryExamination = new Examination();
                        spiroergometryExamination.setExaminationState(ExaminationState.DONE);
                        spiroergometryExamination.setNote("spiroergometry");

                        TestResult spiroergometryResult = new TestResult();
                        spiroergometryResult.setDescription("VO2 max");
                        spiroergometryResult.setValue(70.0);

                        ExaminationType spiroergometryType = new ExaminationType();
                        Set<TestResult> spiroergometryResults = new HashSet<>();
                        spiroergometryResults.add(spiroergometryResult);
                        spiroergometryType.setTestResults(spiroergometryResults);
                        Set<ExaminationType> spiroergometryTypes = new HashSet<>();
                        spiroergometryTypes.add(spiroergometryType);
                        spiroergometryExamination.setExaminationTypes(spiroergometryTypes);

                        examinations.add(ultrasoundExamination);
                        examinations.add(spiroergometryExamination);
                        allExaminations.add(ultrasoundExamination); // Add to the list
                        allExaminations.add(spiroergometryExamination); // Add to the list

                        droolsService.getForward2Ksession().insert(ultrasoundExamination);
                        droolsService.getForward2Ksession().insert(ultrasoundResult);
                        droolsService.getForward2Ksession().insert(spiroergometryExamination);
                        droolsService.getForward2Ksession().insert(spiroergometryResult);

                        // Diagnose pulmonary hypertension
                        operatedPatient.setPulmonaryResistance(true);
                        droolsService.getForward2Ksession().insert(operatedPatient);
                        operatedPatient.setPulmonaryHypertension(true);
                        droolsService.getForward2Ksession().insert(operatedPatient);
                        Examination examination = new Examination();
                        PaliativeCare palliativeCare = new PaliativeCare();
                        palliativeCare.setTherapyState(TherapyState.FINISHED);
                        palliativeCare.setTherapyType(TherapyType.PALLIATIVE_CARE);
                        examination.setTherapy(palliativeCare);
                        examinations.add(examination);
                        allExaminations.add(examination); 

                        droolsService.getForward2Ksession().insert(examination);
                        droolsService.getForward2Ksession().insert(palliativeCare);

                        int firedRules = droolsService.getForward2Ksession().fireAllRules();
                        
                        System.out.println("Number of rules fired: " + firedRules);
                        
                        System.out.println("All Examinations:");
                        Doctor doctor = this.doctorRepository.findById(2).get();
                        Set<Examination> examinations2 = patient.getExaminations();
                        for (Examination exam : allExaminations) {
                                exam.setDoctor(attachDoctor(doctor));
                                exam.setExaminationState(ExaminationState.DONE);
                                exam = this.examinationRepository.save(exam);
                                examinations2.add(exam);
                        }
                        patient.setExaminations(examinations2);
                        patientRepository.save(patient);
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        private Doctor attachDoctor(Doctor doctor) {
                if (doctor != null) {
                        return this.entityManager.merge(doctor);
                }
                return null;
        }
}
