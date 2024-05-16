package com.ftn.sbnz.service.tests;

import com.ftn.sbnz.model.models.examinations.Diagnosis;
import com.ftn.sbnz.model.models.examinations.Examination;
import com.ftn.sbnz.model.models.examinations.ExaminationState;
import com.ftn.sbnz.model.models.examinations.ExaminationType;
import com.ftn.sbnz.model.models.examinations.Symptom;
import com.ftn.sbnz.model.models.examinations.SymptomFrequency;
import com.ftn.sbnz.model.models.examinations.TNMKlassification;
import com.ftn.sbnz.model.models.examinations.TestResult;
import com.ftn.sbnz.model.models.examinations.TumorType;
import com.ftn.sbnz.model.models.therapy.TherapyState;
import com.ftn.sbnz.model.models.therapy.TherapyType;
import com.ftn.sbnz.model.models.users.Patient;
import com.ftn.sbnz.model.models.users.Role;
import com.ftn.sbnz.service.ServiceApplication;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.kie.api.KieServices;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ServiceApplication.class)
public class Forward1RulesTests {
    private static KieSession kSession;

    @BeforeAll
    public static void setup() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        kSession = kContainer.newKieSession("forward1Ksession");

        Set<String> criticalSymptoms = new HashSet<>(Arrays.asList("umor", "slabost", "kašalj", "otežano disanje", "bol u grudima", "hemoptiza"));
        kSession.setGlobal("criticalSymptoms", criticalSymptoms);
    }

    @Test
    public void testSymptoms() {
        Symptom symptom1 = new Symptom(null, "umor", "", 3, SymptomFrequency.CONSTANT);
        Symptom symptom2 = new Symptom(null, "slabost", "", 3, SymptomFrequency.CONSTANT);
        Symptom symptom3 = new Symptom(null, "kašalj", "", 3, SymptomFrequency.CONSTANT);
        Symptom symptom4 = new Symptom(null, "otežano disanje", "", 3, SymptomFrequency.CONSTANT);
        Symptom symptom5 = new Symptom(null, "bol u grudima", "", 3, SymptomFrequency.CONSTANT);
        Symptom symptom6 = new Symptom(null, "hemoptiza", "", 3, SymptomFrequency.CONSTANT);

        Set<Symptom> symptoms = new HashSet<>(Arrays.asList(symptom1, symptom2, symptom3, symptom4, symptom5, symptom6));

        Examination initialExamination = new Examination();
        initialExamination.setDateTime(LocalDateTime.now());
        initialExamination.setExaminationState(ExaminationState.SCHEDULED);
        initialExamination.setSymptoms(symptoms);

        Set<Examination> examinations = new HashSet<>(Arrays.asList(initialExamination));

        Patient patient = new Patient(1, "John", "Doe", "john.doe@example.com", "password", LocalDateTime.now(), Role.Patient, 120, 70, 98, 36.5, examinations);

        kSession.insert(patient);
        kSession.fireAllRules();

        assertEquals(1, patient.getExaminations().size());
    }

    @Test
    public void testNoScheduleWithLessThanThreeCriticalSymptoms() {
        Symptom symptom1 = new Symptom(null, "umor", "", 3, SymptomFrequency.CONSTANT);
        Symptom symptom2 = new Symptom(null, "slabost", "", 3, SymptomFrequency.CONSTANT);

        Set<Symptom> symptoms = new HashSet<>(Arrays.asList(symptom1, symptom2));

        Examination initialExamination = new Examination();
        initialExamination.setDateTime(LocalDateTime.now());
        initialExamination.setExaminationState(ExaminationState.SCHEDULED);
        initialExamination.setSymptoms(symptoms);

        Set<Examination> examinations = new HashSet<>(Arrays.asList(initialExamination));

        Patient patient = new Patient(1, "John", "Doe", "john.doe@example.com", "password", LocalDateTime.now(), Role.Patient, 120, 70, 98, 36.5, examinations);

        kSession.insert(patient);
        kSession.fireAllRules();

        assertEquals(1, patient.getExaminations().size());
    }

    @Test
    public void testScheduleBiopsy() {
        Patient patient = new Patient();
        Set<Examination> examinations = new HashSet<>();
        patient.setExaminations(examinations);

        Examination ctExamination = new Examination();
        ctExamination.setDateTime(LocalDateTime.now());
        ctExamination.setExaminationState(ExaminationState.DONE);
        ctExamination.setNote("CT scan");

        TestResult ctResult = new TestResult();
        ctResult.setDescription("positive for changes");
        ctResult.setValue(1.0);
        Set<TestResult> ctResults = new HashSet<>();
        ctResults.add(ctResult);
        ExaminationType ctType = new ExaminationType();
        ctType.setTestResults(ctResults);
        Set<ExaminationType> ctTypes = new HashSet<>();
        ctTypes.add(ctType);
        ctExamination.setExaminationTypes(ctTypes);

        Examination rtgExamination = new Examination();
        rtgExamination.setDateTime(LocalDateTime.now());
        rtgExamination.setExaminationState(ExaminationState.DONE);
        rtgExamination.setNote("RTG scan");

        TestResult rtgResult = new TestResult();
        rtgResult.setDescription("positive for changes");
        rtgResult.setValue(1.0);
        Set<TestResult> rtgResults = new HashSet<>();
        rtgResults.add(rtgResult);
        ExaminationType rtgType = new ExaminationType();
        rtgType.setTestResults(rtgResults);
        Set<ExaminationType> rtgTypes = new HashSet<>();
        rtgTypes.add(rtgType);
        rtgExamination.setExaminationTypes(rtgTypes);

        examinations.add(ctExamination);
        examinations.add(rtgExamination);

        kSession.insert(patient);
        kSession.fireAllRules();

        assertEquals(3, patient.getExaminations().size());
        assertTrue(patient.getExaminations().stream().anyMatch(exam -> exam.getNote().contains("Scheduled biopsy based on positive CT and RTG results")));
    }

    @Test
    public void testFollowUpAfterMalignantBiopsy() {
        Patient patient = new Patient();
        Set<Examination> examinations = new HashSet<>();
        patient.setExaminations(examinations);

        Examination biopsyExamination = new Examination();
        biopsyExamination.setDateTime(LocalDateTime.now());
        biopsyExamination.setExaminationState(ExaminationState.DONE);
        biopsyExamination.setNote("biopsy");

        TestResult biopsyResult = new TestResult();
        biopsyResult.setDescription("malignant");
        biopsyResult.setValue(1.0);
        Set<TestResult> biopsyResults = new HashSet<>();
        biopsyResults.add(biopsyResult);
        ExaminationType biopsyType = new ExaminationType();
        biopsyType.setTestResults(biopsyResults);
        Set<ExaminationType> biopsyTypes = new HashSet<>();
        biopsyTypes.add(biopsyType);
        biopsyExamination.setExaminationTypes(biopsyTypes);

        examinations.add(biopsyExamination);

        kSession.insert(patient);
        kSession.insert(biopsyExamination);
        kSession.insert(biopsyType);
        kSession.insert(biopsyResult);

        // Ispis pre izvršavanja pravila
        System.out.println("Pre izvršavanja pravila:");
        printSessionFacts(kSession);

        int firedRules = kSession.fireAllRules();

        // Ispis posle izvršavanja pravila
        System.out.println("Posle izvršavanja pravila:");
        printSessionFacts(kSession);

        assertEquals(4, patient.getExaminations().size());
        assertTrue(patient.getExaminations().stream().anyMatch(exam -> exam.getNote().contains("Scheduled Spirometrija")));
        assertTrue(patient.getExaminations().stream().anyMatch(exam -> exam.getNote().contains("Scheduled Test difuznog kapaciteta pluća (DLCO)")));
        assertTrue(patient.getExaminations().stream().anyMatch(exam -> exam.getNote().contains("Scheduled Spiroergometrija")));

        System.out.println("Number of rules fired: " + firedRules);
    }

    private void printSessionFacts(KieSession kSession) {
        for (Object fact : kSession.getObjects()) {
            System.out.println(fact);
        }
    }


    @Test
    public void testScheduleGeneticTestAfterBadResults() {
        Patient patient = new Patient();
        Set<Examination> examinations = new HashSet<>();
        patient.setExaminations(examinations);

        // Spirometrija Examination
        Examination spirometryExamination = new Examination();
        spirometryExamination.setDateTime(LocalDateTime.now());
        spirometryExamination.setExaminationState(ExaminationState.DONE);
        spirometryExamination.setNote("Scheduled Spirometrija");

        TestResult spirometryResult = new TestResult();
        spirometryResult.setDescription("FEV1");
        spirometryResult.setValue(50.0); // Loš rezultat
        Set<TestResult> spirometryResults = new HashSet<>();
        spirometryResults.add(spirometryResult);
        ExaminationType spirometryType = new ExaminationType();
        spirometryType.setTestResults(spirometryResults);
        Set<ExaminationType> spirometryTypes = new HashSet<>();
        spirometryTypes.add(spirometryType);
        spirometryExamination.setExaminationTypes(spirometryTypes);

        // DLCO Examination
        Examination dlcoExamination = new Examination();
        dlcoExamination.setDateTime(LocalDateTime.now());
        dlcoExamination.setExaminationState(ExaminationState.DONE);
        dlcoExamination.setNote("Scheduled Test difuznog kapaciteta pluća (DLCO)");

        TestResult dlcoResult = new TestResult();
        dlcoResult.setDescription("DLCO");
        dlcoResult.setValue(50.0); // Loš rezultat
        Set<TestResult> dlcoResults = new HashSet<>();
        dlcoResults.add(dlcoResult);
        ExaminationType dlcoType = new ExaminationType();
        dlcoType.setTestResults(dlcoResults);
        Set<ExaminationType> dlcoTypes = new HashSet<>();
        dlcoTypes.add(dlcoType);
        dlcoExamination.setExaminationTypes(dlcoTypes);

        // Spiroergometrija Examination
        Examination spiroergometryExamination = new Examination();
        spiroergometryExamination.setDateTime(LocalDateTime.now());
        spiroergometryExamination.setExaminationState(ExaminationState.DONE);
        spiroergometryExamination.setNote("Scheduled Spiroergometrija");

        TestResult spiroergometryResult = new TestResult();
        spiroergometryResult.setDescription("VO2 max");
        spiroergometryResult.setValue(50.0); // Loš rezultat
        Set<TestResult> spiroergometryResults = new HashSet<>();
        spiroergometryResults.add(spiroergometryResult);
        ExaminationType spiroergometryType = new ExaminationType();
        spiroergometryType.setTestResults(spiroergometryResults);
        Set<ExaminationType> spiroergometryTypes = new HashSet<>();
        spiroergometryTypes.add(spiroergometryType);
        spiroergometryExamination.setExaminationTypes(spiroergometryTypes);

        // Add examinations to the patient
        examinations.add(spirometryExamination);
        examinations.add(dlcoExamination);
        examinations.add(spiroergometryExamination);

        kSession.insert(patient);
        kSession.fireAllRules();

        assertEquals(4, patient.getExaminations().size());
        assertTrue(patient.getExaminations().stream().anyMatch(exam -> exam.getNote().contains("Scheduled Genetic Test")));
    }

    @Test
    public void testScheduleGeneticTestAfterBadResults2() {
        Patient patient = new Patient();
        Set<Examination> examinations = new HashSet<>();
        patient.setExaminations(examinations);

        // Spirometrija Examination
        Examination spirometryExamination = createExamination("Scheduled Spirometrija", createTestResult("FEV1", 50.0));
        examinations.add(spirometryExamination);

        // DLCO Examination
        Examination dlcoExamination = createExamination("Scheduled Test difuznog kapaciteta pluća (DLCO)", createTestResult("DLCO", 50.0));
        examinations.add(dlcoExamination);

        // Spiroergometrija Examination
        Examination spiroergometryExamination = createExamination("Scheduled Spiroergometrija", createTestResult("VO2 max", 50.0));
        examinations.add(spiroergometryExamination);

        kSession.insert(patient);
        kSession.fireAllRules();

        assertEquals(4, patient.getExaminations().size());
        assertTrue(patient.getExaminations().stream().anyMatch(exam -> exam.getNote().contains("Scheduled Genetic Test")));
    }

    @Test
    public void testNoScheduleGeneticTestIfAlreadyScheduled() {
        Patient patient = new Patient();
        Set<Examination> examinations = new HashSet<>();
        patient.setExaminations(examinations);

        // Spirometrija Examination
        Examination spirometryExamination = createExamination("Scheduled Spirometrija", createTestResult("FEV1", 50.0));
        examinations.add(spirometryExamination);

        // DLCO Examination
        Examination dlcoExamination = createExamination("Scheduled Test difuznog kapaciteta pluća (DLCO)", createTestResult("DLCO", 50.0));
        examinations.add(dlcoExamination);

        // Spiroergometrija Examination
        Examination spiroergometryExamination = createExamination("Scheduled Spiroergometrija", createTestResult("VO2 max", 50.0));
        examinations.add(spiroergometryExamination);

        // Already scheduled genetic test
        Examination geneticTest = new Examination();
        geneticTest.setDateTime(LocalDateTime.now());
        geneticTest.setExaminationState(ExaminationState.SCHEDULED);
        geneticTest.setNote("Scheduled Genetic Test");
        examinations.add(geneticTest);

        kSession.insert(patient);
        kSession.fireAllRules();

        assertEquals(4, patient.getExaminations().size());
    }

    @Test
    public void testNoScheduleGeneticTestIfResultsAreGood() {
        Patient patient = new Patient();
        Set<Examination> examinations = new HashSet<>();
        patient.setExaminations(examinations);

        // Spirometrija Examination
        Examination spirometryExamination = createExamination("Scheduled Spirometrija", createTestResult("FEV1", 90.0));
        examinations.add(spirometryExamination);

        // DLCO Examination
        Examination dlcoExamination = createExamination("Scheduled Test difuznog kapaciteta pluća (DLCO)", createTestResult("DLCO", 90.0));
        examinations.add(dlcoExamination);

        // Spiroergometrija Examination
        Examination spiroergometryExamination = createExamination("Scheduled Spiroergometrija", createTestResult("VO2 max", 90.0));
        examinations.add(spiroergometryExamination);

        kSession.insert(patient);
        kSession.fireAllRules();

        assertEquals(3, patient.getExaminations().size());
        assertTrue(patient.getExaminations().stream().noneMatch(exam -> exam.getNote().contains("Scheduled Genetic Test")));
    }

    private Examination createExamination(String note, TestResult result) {
        Examination examination = new Examination();
        examination.setDateTime(LocalDateTime.now());
        examination.setExaminationState(ExaminationState.DONE);
        examination.setNote(note);

        Set<TestResult> results = new HashSet<>();
        results.add(result);
        ExaminationType type = new ExaminationType();
        type.setTestResults(results);
        Set<ExaminationType> types = new HashSet<>();
        types.add(type);
        examination.setExaminationTypes(types);

        return examination;
    }

    private TestResult createTestResult(String description, double value) {
        TestResult result = new TestResult();
        result.setDescription(description);
        result.setValue(value);
        return result;
    }

    @Test
    public void testDetermineCancerStageAndCreateDiagnosis() {
        Patient patient = new Patient();
        Set<Examination> examinations = new HashSet<>();
        patient.setExaminations(examinations);

        // Create examination
        Examination examination = new Examination();
        examination.setExaminationState(ExaminationState.DONE);
        examination.setExaminationTypes(new HashSet<>());
        examination.setNote("metastatski");

        // Create examination type
        ExaminationType examinationType = new ExaminationType();
        examination.setExaminationTypes(Set.of(examinationType));

        // Create test results
        TestResult biopsyResult = new TestResult();
        biopsyResult.setDescription("malignant");
        biopsyResult.setValue(1.0);
        TestResult spirometryResult = new TestResult();
        spirometryResult.setDescription("FEV1");
        spirometryResult.setValue(70.0);
        TestResult dlcoResult = new TestResult();
        dlcoResult.setDescription("DLCO");
        dlcoResult.setValue(70.0);
        TestResult spiroergometryResult = new TestResult();
        spiroergometryResult.setDescription("VO2 max");
        spiroergometryResult.setValue(70.0);

        examinationType.setTestResults(Set.of(biopsyResult, spirometryResult, dlcoResult, spiroergometryResult));

        // Add examination to patient
        examinations.add(examination);

        // Insert facts
        kSession.insert(patient);
        kSession.insert(examination);
        kSession.insert(examinationType);
        kSession.insert(biopsyResult);
        kSession.insert(spirometryResult);
        kSession.insert(dlcoResult);
        kSession.insert(spiroergometryResult);

        // Fire rules
        int firedRules = kSession.fireAllRules();

        // Assertions
        assertEquals(6, firedRules);
        assertNotNull(examination.getDiagnosis());
        assertTrue(examination.getDiagnosis().isTumorDetected());
        assertNotNull(examination.getDiagnosis().getTnmKlassification());
        assertEquals(2.0, examination.getDiagnosis().getTnmKlassification().gettKlassification());
        assertEquals(1.0, examination.getDiagnosis().getTnmKlassification().getnKlassification());
        assertEquals(0.0, examination.getDiagnosis().getTnmKlassification().getmKlassification());
        assertEquals(TumorType.EXTRATORACAL_PULMONARY, examination.getDiagnosis().getTumorType());

    }

    @Test
    public void testDetermineTherapyBasedOnTNMClassification() {
        Patient patient = new Patient();
        Set<Examination> examinations = new HashSet<>();
        patient.setExaminations(examinations);

        Examination examination = new Examination();
        examination.setExaminationState(ExaminationState.DONE);

        Diagnosis diagnosis = new Diagnosis();
        TNMKlassification tnm = new TNMKlassification();
        tnm.settKlassification(2.0);
        tnm.setnKlassification(0.0);
        tnm.setmKlassification(0.0);
        diagnosis.setTnmKlassification(tnm);
        diagnosis.setTumorDetected(true);
        diagnosis.setTumorType(TumorType.EXTRATORACAL_PULMONARY);
        examination.setDiagnosis(diagnosis);

        examinations.add(examination);

        kSession.insert(patient);
        kSession.insert(examination);

        int firedRules = kSession.fireAllRules();

        assertEquals(5, firedRules);
        assertNotNull(examination.getTherapy());
        assertEquals(TherapyType.OPERATION, examination.getTherapy().getTherapyType());
        assertEquals(TherapyState.PLANNED, examination.getTherapy().getTherapyState());
        assertEquals("Planned surgery based on TNM classification", examination.getTherapy().getDescription());
    }

}
