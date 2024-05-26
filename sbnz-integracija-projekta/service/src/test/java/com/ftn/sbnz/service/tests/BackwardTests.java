package com.ftn.sbnz.service.tests;

import com.ftn.sbnz.model.models.Fact;
import com.ftn.sbnz.model.models.Result;
import com.ftn.sbnz.model.models.alarms.Alarm;
import com.ftn.sbnz.model.models.examinations.Diagnosis;
import com.ftn.sbnz.model.models.examinations.Examination;
import com.ftn.sbnz.model.models.users.Patient;
import com.ftn.sbnz.model.models.users.Role;
import com.ftn.sbnz.service.ServiceApplication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ServiceApplication.class)
public class BackwardTests {
    private static KieSession kSession;
    private static Result result;
    @BeforeAll
    public static void setup() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        kSession = kContainer.newKieSession("bwKsession");
        result = new Result();
        kSession.setGlobal("result", result);
    }

    @Test
    public void testWarnChildrenOfTumorDiagnosis() {
        Patient parent = new Patient(1, "Marko", "Markovic", "parent@example.com", "password", LocalDateTime.now().minusYears(30), Role.Patient, 120.0, 70, 98.0, 36.5, new HashSet<>());
        Patient child = new Patient(2, "Janko", "Markovic", "child@example.com", "password", LocalDateTime.now().minusYears(10), Role.Patient, 110.0, 80, 97.0, 36.0, new HashSet<>());

        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setTumorDetected(true);

        Examination examination = new Examination();
        examination.setDiagnosis(diagnosis);

        HashSet<Examination> examinations = new HashSet<>();
        examinations.add(examination);

        parent.setExaminations(examinations);

        kSession.insert(parent);
        kSession.insert(child);
        kSession.insert(new Fact(child, parent));

        kSession.fireAllRules();

        assertEquals(3, result.getFacts().size());
        assertEquals("Janko IS CHILD OF Marko", result.getFacts().get(0));
        assertEquals("Warning: Janko should schedule a screening due to parent's tumor diagnosis", result.getFacts().get(1));

         boolean alarmCreated = kSession.getObjects().stream()
            .anyMatch(obj -> obj instanceof Alarm && ((Alarm) obj).getDescription().equals("Parent has tumor") && ((Alarm) obj).getPatient().equals(child));
        assertTrue(alarmCreated, "Expected an alarm to be created for the child.");


    }
}
