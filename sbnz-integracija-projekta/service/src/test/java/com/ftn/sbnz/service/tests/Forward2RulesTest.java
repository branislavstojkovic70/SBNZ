package com.ftn.sbnz.service.tests;

import com.ftn.sbnz.model.models.examinations.Examination;
import com.ftn.sbnz.model.models.examinations.ExaminationState;
import com.ftn.sbnz.model.models.examinations.ExaminationType;
import com.ftn.sbnz.model.models.examinations.TestResult;
import com.ftn.sbnz.model.models.therapy.Operation;
import com.ftn.sbnz.model.models.therapy.PaliativeCare;
import com.ftn.sbnz.model.models.therapy.Therapy;
import com.ftn.sbnz.model.models.therapy.TherapyState;
import com.ftn.sbnz.model.models.therapy.TherapyType;
import com.ftn.sbnz.model.models.users.OperatedPatient;
import com.ftn.sbnz.model.models.users.Patient;
import com.ftn.sbnz.service.ServiceApplication;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.test.context.SpringBootTest;
import org.kie.api.KieServices;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = ServiceApplication.class)
public class Forward2RulesTest {
    private static KieSession kSession;

    @BeforeAll
    public static void setup() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        kSession = kContainer.newKieSession("forward2Ksession");
    }

    @Test
    public void testCreateOperatedPatient() {
        Patient patient = new Patient();
        Set<Examination> examinations = new HashSet<>();
        patient.setExaminations(examinations);

        Examination operationExamination = new Examination();
        operationExamination.setExaminationState(ExaminationState.DONE);
        operationExamination.setNote("operation");

        Operation operation = new Operation();
        operationExamination.setTherapy(operation);

        examinations.add(operationExamination);

        kSession.insert(patient);

        kSession.insert(operationExamination);
        kSession.insert(operation);

        int firedRules = kSession.fireAllRules();

        OperatedPatient createdOperatedPatient = null;
        for (Object obj : kSession.getObjects()) {
            if (obj instanceof OperatedPatient) {
                createdOperatedPatient = (OperatedPatient) obj;
                break;
            }
        }

        assertNotNull(createdOperatedPatient, "OperatedPatient should have been created");
        assertEquals(patient, createdOperatedPatient.getPatient(), "Patient should match");
        assertEquals(operation, createdOperatedPatient.getOperation(), "Operation should match");
        System.out.println("Number of rules fired: " + firedRules);
        assertEquals(5, firedRules, "5 Rules fired");
    }

    @Test
    public void testCheckForResistanceInPulmonaryBloodVessels() {
        Patient patient = new Patient();
        Set<Examination> examinations = new HashSet<>();
        patient.setExaminations(examinations);

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

        OperatedPatient operatedPatient = new OperatedPatient();
        operatedPatient.setPatient(patient);
        operatedPatient.setOperation(new Operation());

        kSession.insert(patient);
        kSession.insert(ultrasoundExamination);
        kSession.insert(ultrasoundResult);
        kSession.insert(spiroergometryExamination);
        kSession.insert(spiroergometryResult);
        kSession.insert(operatedPatient);

        int firedRules = kSession.fireAllRules();

        assertTrue(operatedPatient.isPulmonaryResistance(), "Pulmonary resistance should be detected");
        System.out.println("Number of rules fired: " + firedRules);
    }

    @Test
    public void testDiagnosePulmonaryHypertension() {
        Patient patient = new Patient();
        Operation operation = new Operation();
        OperatedPatient operatedPatient = new OperatedPatient();
        operatedPatient.setPatient(patient);
        operatedPatient.setOperation(operation);
        operatedPatient.setPulmonaryResistance(true);

        kSession.insert(operatedPatient);

        int firedRules = kSession.fireAllRules();

        assertTrue(operatedPatient.isPulmonaryHypertension(), "Pulmonary hypertension should be true");
        System.out.println("Number of rules fired: " + firedRules);
        assertEquals(2, firedRules, "Two rules should be fired");
    }

    @Test
    public void testNoDiagnosePulmonaryHypertensionWithoutResistance() {
        Patient patient = new Patient();
        Operation operation = new Operation();
        OperatedPatient operatedPatient = new OperatedPatient();
        operatedPatient.setPatient(patient);
        operatedPatient.setOperation(operation);
        operatedPatient.setPulmonaryResistance(false);

        kSession.insert(operatedPatient);

        int firedRules = kSession.fireAllRules();

        assertFalse(operatedPatient.isPulmonaryHypertension(), "Pulmonary hypertension should not be true");
        System.out.println("Number of rules fired: " + firedRules);
        assertEquals(0, firedRules, "No rules should have fired");
    }

    @Test
    public void testRecommendLifestyleChangesAndPalliativeCare() {
        Patient patient = new Patient();
        Operation operation = new Operation();
        OperatedPatient operatedPatient = new OperatedPatient();
        operatedPatient.setPatient(patient);
        operatedPatient.setOperation(operation);
        operatedPatient.setPulmonaryHypertension(true);

        kSession.insert(operatedPatient);

        int firedRules = kSession.fireAllRules();

        assertNotNull(operatedPatient.getNote(), "Lifestyle changes note should not be null");
        assertTrue(operatedPatient.getNote().contains("Recommend lifestyle changes for patient"), "Note should contain lifestyle change recommendation");

        PaliativeCare therapy = (PaliativeCare) operatedPatient.getFurtherTherapy();
        assertNotNull(therapy, "Therapy should not be null");
        assertEquals(TherapyType.PALLIATIVE_CARE, therapy.getTherapyType(), "Therapy type should be PALLIATIVE_CARE");
        assertEquals(TherapyState.DURING, therapy.getTherapyState(), "Therapy state should be DURING");
        assertEquals("Recommended palliative care for pulmonary hypertension.", therapy.getDescription(), "Therapy description should be correct");
        assertEquals("Medication", therapy.getApplicationMethod(), "Application method should be Medication");

        System.out.println("Number of rules fired: " + firedRules);
        assertEquals(1, firedRules, "Exactly one rule should have fired");
    }

    @Test
    public void testNoRecommendLifestyleChangesAndPalliativeCareWithoutHypertension() {
        Patient patient = new Patient();
        Operation operation = new Operation();
        OperatedPatient operatedPatient = new OperatedPatient();
        operatedPatient.setPatient(patient);
        operatedPatient.setOperation(operation);
        operatedPatient.setPulmonaryHypertension(false);

        kSession.insert(operatedPatient);

        int firedRules = kSession.fireAllRules();

        assertNull("Lifestyle changes note should be null", operatedPatient.getNote());
        assertNull("Therapy should be null", operatedPatient.getFurtherTherapy());

        System.out.println("Number of rules fired: " + firedRules);
        assertEquals(0, firedRules, "No rules should have fired");
    }

    @Test
    public void testRecommendHeartAndLungTransplant() {
        Patient patient = new Patient();
        Operation operation = new Operation();
        OperatedPatient operatedPatient = new OperatedPatient();
        operatedPatient.setPatient(patient);
        operatedPatient.setOperation(operation);
        operatedPatient.setPulmonaryHypertension(true);

        Examination examination = new Examination();
        PaliativeCare palliativeCare = new PaliativeCare();
        palliativeCare.setTherapyState(TherapyState.FINISHED);
        palliativeCare.setTherapyType(TherapyType.PALLIATIVE_CARE);
        examination.setTherapy(palliativeCare);

        Set<Examination> examinations = new HashSet<>();
        examinations.add(examination);
        patient.setExaminations(examinations);

        kSession.insert(operatedPatient);

        int firedRules = kSession.fireAllRules();
        Therapy therapy = operatedPatient.getFurtherTherapy();
        assertNotNull(therapy, "Therapy should not be null");
        assertTrue(therapy instanceof Operation, "Therapy should be of type Operation");
        assertEquals(TherapyType.OPERATION, therapy.getTherapyType(), "Therapy type should be OPERATION");
        assertEquals(TherapyState.PLANNED, therapy.getTherapyState(), "Therapy state should be PLANNED");
        assertEquals("Recommended heart and lung transplant for advanced pulmonary hypertension.", therapy.getDescription(), "Therapy description should be correct");

        System.out.println("Number of rules fired: " + firedRules);
        assertEquals(2, firedRules, "Exactly one rule should have fired");
    }

}