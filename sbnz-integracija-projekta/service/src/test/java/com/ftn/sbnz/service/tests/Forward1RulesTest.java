package com.ftn.sbnz.service.tests;

import com.ftn.sbnz.model.models.examinations.Examination;
import com.ftn.sbnz.model.models.examinations.ExaminationState;
import com.ftn.sbnz.model.models.examinations.ExaminationType;
import com.ftn.sbnz.model.models.examinations.Symptom;
import com.ftn.sbnz.model.models.examinations.SymptomFrequency;
import com.ftn.sbnz.model.models.examinations.TestResult;
import com.ftn.sbnz.model.models.examinations.UpdatedExamination;
import com.ftn.sbnz.model.models.therapy.TherapyType;
import com.ftn.sbnz.model.models.users.Doctor;
import com.ftn.sbnz.model.models.users.Patient;
import com.ftn.sbnz.model.models.users.Role;
import com.ftn.sbnz.service.ServiceApplication;
import com.ftn.sbnz.service.repository.examination.ExaminationRepository;
import com.ftn.sbnz.service.repository.users.PatientRepository;
import com.ftn.sbnz.service.service.WebSocketService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.kie.api.KieServices;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = ServiceApplication.class)
public class Forward1RulesTest {
    private static KieSession kSession;

    @Mock
    private ExaminationRepository examinationRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock WebSocketService webSocketService;

    @BeforeAll
    public static void setupKieSession() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        kSession = kContainer.newKieSession("forward1Ksession");

        Set<String> criticalSymptoms = new HashSet<>(
                Arrays.asList("umor", "slabost", "kašalj", "otežano disanje", "bol u grudima", "hemoptiza"));
        kSession.setGlobal("criticalSymptoms", criticalSymptoms);
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        kSession.setGlobal("examinationRepository", examinationRepository);
        kSession.setGlobal("patientRepository", patientRepository);
        kSession.setGlobal("webSocketService", webSocketService);
    }

    @Test
    public void testTriggerAllRules() {
        Symptom symptom1 = new Symptom(null, "umor", "", 3, SymptomFrequency.CONSTANT);
        Symptom symptom2 = new Symptom(null, "slabost", "", 3, SymptomFrequency.CONSTANT);
        Symptom symptom3 = new Symptom(null, "kašalj", "", 3, SymptomFrequency.CONSTANT);
        Symptom symptom4 = new Symptom(null, "otežano disanje", "", 3, SymptomFrequency.CONSTANT);
        Symptom symptom5 = new Symptom(null, "bol u grudima", "", 3, SymptomFrequency.CONSTANT);
        Symptom symptom6 = new Symptom(null, "hemoptiza", "", 3, SymptomFrequency.CONSTANT);

        Set<Symptom> symptoms = new HashSet<>(
                Arrays.asList(symptom1, symptom2, symptom3, symptom4, symptom5, symptom6));
        Doctor doctor = new Doctor(null, "d", "d", "d@gmail.com", "d", LocalDateTime.now().minusYears(30), Role.Doctor,
                "Pulmology");
        Examination initialExamination = new Examination(null, LocalDateTime.now().plusDays(1), doctor,
                "Initial examination",
                ExaminationState.DONE, new HashSet<ExaminationType>(), symptoms, null, null);

        Set<Examination> examinations = new HashSet<>(Arrays.asList(initialExamination));

        Patient patient = new Patient(1, "John", "Doe", "john.doe@example.com", "password", LocalDateTime.now(),
                Role.Patient, 120, 70, 98, 36.5, examinations);

        Examination ctExamination = new Examination(null, LocalDateTime.now().plusDays(1), doctor, "CT scan",
                ExaminationState.DONE, createExaminationTypes("positive for changes"), symptoms, null, null);

        Examination rtgExamination = new Examination(null, LocalDateTime.now().plusDays(2), doctor, "RTG scan",
                ExaminationState.DONE, createExaminationTypes("positive for changes"), symptoms, null, null);

        Examination biopsyExamination = new Examination(null, LocalDateTime.now().plusDays(3), doctor, "biopsy",
                ExaminationState.DONE, createExaminationTypes("malignant"), symptoms, null, null);

        Examination spirometryExamination = new Examination(null, LocalDateTime.now().plusDays(4), doctor, "spirometry",
                ExaminationState.DONE, createExaminationTypes("positive for changes"), symptoms, null, null);

        Examination spiroergometryExamination = new Examination(null, LocalDateTime.now().plusDays(5), doctor,
                "spiroergometry",
                ExaminationState.DONE, createExaminationTypes("positive for changes"), symptoms, null, null);

        Examination dlcoExamination = new Examination(null, LocalDateTime.now().plusDays(6), doctor, "DLCO",
                ExaminationState.DONE, createExaminationTypes("positive for changes"), symptoms, null, null);

        Examination geneticTestExamination = new Examination(null, LocalDateTime.now().plusDays(7), doctor,
                "genetic test",
                ExaminationState.DONE, createExaminationTypes("positive for changes"), symptoms, null, null);

        examinations.add(ctExamination);
        examinations.add(rtgExamination);
        examinations.add(biopsyExamination);
        examinations.add(spirometryExamination);
        examinations.add(spiroergometryExamination);
        examinations.add(dlcoExamination);
        examinations.add(geneticTestExamination);

        patient.setExaminations(examinations);

        kSession.insert(initialExamination);
        kSession.insert(patient);

        kSession.insert(new UpdatedExamination(patient, initialExamination));
        kSession.fireAllRules();

        kSession.insert(ctExamination);
        kSession.insert(rtgExamination);
        kSession.insert(biopsyExamination);
        kSession.insert(spirometryExamination);
        kSession.insert(spiroergometryExamination);
        kSession.insert(dlcoExamination);
        kSession.insert(geneticTestExamination);

        kSession.fireAllRules();
        assertEquals(8, patient.getExaminations().size());
        assertTrue(patient.getExaminations().stream().anyMatch(exam -> exam.getNote().contains("genetic test")));
        assertTrue(patient.getExaminations().stream().anyMatch(exam -> exam.getDiagnosis() != null));
        assertTrue(patient.getExaminations().stream().anyMatch(exam -> exam.getTherapy() != null));
        assertTrue(patient.getExaminations().stream()
                .anyMatch(exam -> exam.getTherapy().getTherapyType() == TherapyType.OPERATION));

        System.out.println("All rules triggered successfully.");
    }

    private Set<ExaminationType> createExaminationTypes(String resultDescription) {
        TestResult testResult = new TestResult();
        testResult.setDescription(resultDescription);
        testResult.setValue(1.0);

        Set<TestResult> testResults = new HashSet<>();
        testResults.add(testResult);

        ExaminationType examinationType = new ExaminationType();
        examinationType.setTestResults(testResults);

        Set<ExaminationType> examinationTypes = new HashSet<>();
        examinationTypes.add(examinationType);

        return examinationTypes;
    }

}
