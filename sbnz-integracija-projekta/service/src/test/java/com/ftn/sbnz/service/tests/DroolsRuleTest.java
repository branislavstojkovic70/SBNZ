package com.ftn.sbnz.service.tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.drools.core.impl.KnowledgeBaseFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.utils.KieHelper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ftn.sbnz.model.models.examinations.Examination;
import com.ftn.sbnz.model.models.examinations.ExaminationState;
import com.ftn.sbnz.model.models.examinations.ExaminationType;
import com.ftn.sbnz.model.models.examinations.Symptom;
import com.ftn.sbnz.model.models.examinations.SymptomFrequency;
import com.ftn.sbnz.model.models.examinations.TestResult;
import com.ftn.sbnz.model.models.examinations.UpdatedExamination;
import com.ftn.sbnz.model.models.users.Doctor;
import com.ftn.sbnz.model.models.users.Patient;
import com.ftn.sbnz.service.repository.examination.ExaminationRepository;
import com.ftn.sbnz.service.repository.users.PatientRepository;
import com.ftn.sbnz.service.service.DroolsService;
import com.ftn.sbnz.service.service.WebSocketService;

public class DroolsRuleTest {

    private KieSession kieSession;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private WebSocketService webSocketService;

    @Mock
    private ExaminationRepository examinationRepository;

    @InjectMocks
    private DroolsService droolsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        kieSession = kieContainer.newKieSession("forward1Ksession");

        Set<String> criticalSymptoms = new HashSet<>(Arrays.asList("umor", "slabost", "kašalj", "otežano disanje", "bol u grudima", "hemoptiza"));
        kieSession.setGlobal("criticalSymptoms", criticalSymptoms);
        kieSession.setGlobal("patientRepository", this.patientRepository);
        kieSession.setGlobal("examinationRepository", this.examinationRepository);
        kieSession.setGlobal("webSocketService", this.webSocketService);
    }

    @Test
    public void testScheduleCTandRTGBasedOnSpecificSymptoms() {
        // Create doctor
        Doctor doctor = new Doctor();
        doctor.setId(1);
        doctor.setIme("Dr. John");

        // Create symptoms
        Symptom symptom1 = new Symptom(null, "umor", "", 8, SymptomFrequency.CONSTANT);
        Symptom symptom2 = new Symptom(null, "slabost", "", 9, SymptomFrequency.CONSTANT);
        Symptom symptom3 = new Symptom(null, "kašalj", "", 9, SymptomFrequency.CONSTANT);
        Symptom symptom4 = new Symptom(null, "hemoptiza", "", 9, SymptomFrequency.CONSTANT);
        Set<Symptom> symptoms = new HashSet<>(Arrays.asList(symptom1, symptom2, symptom3, symptom4));

        // Create examination
        Examination examination = new Examination(null, LocalDateTime.now(), doctor, "Initial examination",
                ExaminationState.SCHEDULED, new HashSet<>(), symptoms, null, null);

        // Create patient
        Patient patient = new Patient();
        patient.setId(1);
        patient.setIme("Patient 1");
        patient.setExaminations(new HashSet<>(Arrays.asList(examination)));

        // Create UpdatedExamination
        UpdatedExamination updatedExamination = new UpdatedExamination(patient, examination);

        // Insert UpdatedExamination into the session
        kieSession.insert(patient);
        kieSession.insert(updatedExamination);

        // Fire all rules
        kieSession.fireAllRules();

        // Verify that the patientRepository.save() was called
        verify(patientRepository, times(1)).save(patient);

        // Verify that new examinations were added
        assertEquals(3, patient.getExaminations().size());

        // Verify the details of the newly added examinations
        boolean ctScheduled = patient.getExaminations().stream()
                .anyMatch(exam -> "Scheduled CT scan based on symptoms".equals(exam.getNote()) && ExaminationState.SCHEDULED.equals(exam.getExaminationState()));
        boolean rtgScheduled = patient.getExaminations().stream()
                .anyMatch(exam -> "Scheduled RTG scan based on symptoms".equals(exam.getNote()) && ExaminationState.SCHEDULED.equals(exam.getExaminationState()));

        assertTrue(ctScheduled);
        assertTrue(rtgScheduled);

        Examination ctExamination = patient.getExaminations().stream()
                .filter(exam -> "Scheduled CT scan based on symptoms".equals(exam.getNote()))
                .findFirst().orElse(null);
        Examination rtgExamination = patient.getExaminations().stream()
                .filter(exam -> "Scheduled RTG scan based on symptoms".equals(exam.getNote()))
                .findFirst().orElse(null);
        if (ctExamination != null) {
            ctExamination.setExaminationState(ExaminationState.DONE);
            ctExamination.setNote("CT scan");
            ExaminationType ctExaminationType = new ExaminationType();
            ctExaminationType.setTestResults(new HashSet<>(Arrays.asList(new TestResult(null, "CT scan", "l/min", 3.0, "positive for changes"))));
            ctExamination.setExaminationTypes(new HashSet<>(Arrays.asList(ctExaminationType)));
        }

        if (rtgExamination != null) {
            rtgExamination.setExaminationState(ExaminationState.DONE);
            rtgExamination.setNote("RTG scan");
            ExaminationType rtgExaminationType = new ExaminationType();
            rtgExaminationType.setTestResults(new HashSet<>(Arrays.asList(new TestResult(null, "RTG scan", "l/min", 3.0, "positive for changes"))));
            rtgExamination.setExaminationTypes(new HashSet<>(Arrays.asList(rtgExaminationType)));
        }
        UpdatedExamination updatedExamination2 = new UpdatedExamination(patient, ctExamination);
        kieSession.insert(updatedExamination2);
        // UpdatedExamination updatedExamination3 = new UpdatedExamination(patient, rtgExamination);
        // kieSession.insert(updatedExamination3);

        // Fire all rules again
        kieSession.fireAllRules();

        // Verify that the biopsy was scheduled
        boolean biopsyScheduled = patient.getExaminations().stream()
                .anyMatch(exam -> exam.getNote().contains("Scheduled biopsy based on positive CT or RTG results") && ExaminationState.SCHEDULED.equals(exam.getExaminationState()));
        assertTrue(biopsyScheduled);

        // Simulate completion of biopsy examination with malignant results
        Examination biopsyExamination = patient.getExaminations().stream()
                .filter(exam -> exam.getNote().contains("Scheduled biopsy based on positive CT or RTG results"))
                .findFirst().orElse(null);
        if (biopsyExamination != null) {
            biopsyExamination.setExaminationState(ExaminationState.DONE);
            biopsyExamination.setNote("biopsy result");
            ExaminationType biopsyExaminationType = new ExaminationType();
            biopsyExaminationType.setTestResults(new HashSet<>(Arrays.asList(new TestResult(null, "biopsy", "result", 1.0, "malignant"))));
            biopsyExamination.setExaminationTypes(new HashSet<>(Arrays.asList(biopsyExaminationType)));
        }

        // Create UpdatedExamination for biopsy to trigger the follow-up tests rule
        UpdatedExamination updatedExamination4 = new UpdatedExamination(patient, biopsyExamination);
        kieSession.insert(updatedExamination4);

        // Fire all rules again
        kieSession.fireAllRules();

        // Verify that the follow-up tests were scheduled
        boolean followUpTestsScheduled = patient.getExaminations().stream()
                .anyMatch(exam -> exam.getNote().contains("Scheduled Spirometrija") || exam.getNote().contains("Scheduled Test difuznog kapaciteta pluća (DLCO)") || exam.getNote().contains("Scheduled Spiroergometrija"));
        assertTrue(followUpTestsScheduled);
    }
}
