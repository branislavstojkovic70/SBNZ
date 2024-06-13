package com.ftn.sbnz.service.tests;

import com.ftn.sbnz.model.models.examinations.*;
import com.ftn.sbnz.model.models.therapy.*;
import com.ftn.sbnz.model.models.users.OperatedPatient;
import com.ftn.sbnz.model.models.users.Patient;
import com.ftn.sbnz.service.ServiceApplication;
import com.ftn.sbnz.service.service.WebSocketService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.test.context.SpringBootTest;
import org.kie.api.KieServices;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ServiceApplication.class)
public class Forward2RulesTest {
    private static KieSession kSession;
    private static WebSocketService webSocketService;

    @BeforeAll
    public static void setup() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        kSession = kContainer.newKieSession("forward2Ksession");

        // Create a mock WebSocketService
        webSocketService = mock(WebSocketService.class);
        kSession.setGlobal("webSocketService", webSocketService);
    }

    @Test
    public void testAllRules() {
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

        OperatedPatient operatedPatient = new OperatedPatient();
        operatedPatient.setPatient(patient);
        operatedPatient.setOperation(operation);
        operatedPatient.setPulmonaryResistance(false);
        operatedPatient.setPulmonaryHypertension(false);

        kSession.insert(patient);
        kSession.insert(operationExamination);
        kSession.insert(operation);
        kSession.insert(operatedPatient);

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

        kSession.insert(ultrasoundExamination);
        kSession.insert(ultrasoundResult);
        kSession.insert(spiroergometryExamination);
        kSession.insert(spiroergometryResult);

        // Diagnose pulmonary hypertension
        operatedPatient.setPulmonaryResistance(true);
        kSession.insert(operatedPatient);

        // Recommend lifestyle changes and palliative care
        operatedPatient.setPulmonaryHypertension(true);
        kSession.insert(operatedPatient);

        // Recommend heart and lung transplant
        Examination examination = new Examination();
        PaliativeCare palliativeCare = new PaliativeCare();
        palliativeCare.setTherapyState(TherapyState.FINISHED);
        palliativeCare.setTherapyType(TherapyType.PALLIATIVE_CARE);
        examination.setTherapy(palliativeCare);
        examinations.add(examination);

        kSession.insert(examination);
        kSession.insert(palliativeCare);

        int firedRules = kSession.fireAllRules();

        // Assertions
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

        assertTrue(operatedPatient.isPulmonaryResistance(), "Pulmonary resistance should be detected");
        assertTrue(operatedPatient.isPulmonaryHypertension(), "Pulmonary hypertension should be true");

        assertNotNull(operatedPatient.getNote(), "Lifestyle changes note should not be null");
        assertTrue(operatedPatient.getNote().contains("Recommend lifestyle changes for patient"),
                "Note should contain lifestyle change recommendation");

        Therapy therapy = operatedPatient.getFurtherTherapy();
        assertNotNull(therapy, "Therapy should not be null");
        assertTrue(therapy instanceof Operation, "Therapy should be of type Operation");
        assertEquals(TherapyType.OPERATION, therapy.getTherapyType(), "Therapy type should be OPERATION");
        assertEquals(TherapyState.PLANNED, therapy.getTherapyState(), "Therapy state should be PLANNED");
        assertEquals("Recommended heart and lung transplant for advanced pulmonary hypertension.",
                therapy.getDescription(), "Therapy description should be correct");

        System.out.println("Number of rules fired: " + firedRules);
        assertEquals(13, firedRules, "5 Rules fired");
    }
}
