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
import com.ftn.sbnz.model.models.therapy.Operation;
import com.ftn.sbnz.model.models.therapy.PaliativeCare;
import com.ftn.sbnz.model.models.therapy.Therapy;
import com.ftn.sbnz.model.models.therapy.TherapyState;
import com.ftn.sbnz.model.models.therapy.TherapyType;
import com.ftn.sbnz.model.models.users.OperatedPatient;
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

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ServiceApplication.class)
public class CEPConfigTest {

    private static KieSession kSession;

    @BeforeAll
    public static void setup() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        kSession = kContainer.newKieSession("simpleKsession");

        Set<String> criticalSymptoms = new HashSet<>(Arrays.asList("umor", "slabost", "kašalj", "otežano disanje", "bol u grudima", "hemoptiza"));
        kSession.setGlobal("criticalSymptoms", criticalSymptoms);
    }

    @Test
    public void testCriticalBloodPressureAlert() {
        Patient patient = new Patient(
            1,
            "John",
            "Doe",
            "john.doe@example.com",
            "password",
            LocalDateTime.now(),
            Role.Patient,
            185,    // bloodPressure
            70,     // puls
            98,     // saturationO2
            36.5,   // bodyTemperature
            new HashSet<>() // examinations
        );

        kSession.insert(patient);
        int firedRules = kSession.fireAllRules();

        assertEquals(1, firedRules); // Assuming only one rule will be triggered
    }

    @Test
    public void testCriticalPulseAlert() {
        Patient patient = new Patient(
            2,
            "Jane",
            "Doe",
            "jane.doe@example.com",
            "password",
            LocalDateTime.now(),
            Role.Patient,
            120,    // bloodPressure
            130,    // puls
            98,     // saturationO2
            36.5,   // bodyTemperature
            new HashSet<>() // examinations
        );

        kSession.insert(patient);
        int firedRules = kSession.fireAllRules();

        assertEquals(1, firedRules); // Assuming only one rule will be triggered
    }

    @Test
    public void testCriticalOxygenSaturationAlert() {
        Patient patient = new Patient(
            3,
            "Alice",
            "Smith",
            "alice.smith@example.com",
            "password",
            LocalDateTime.now(),
            Role.Patient,
            120,    // bloodPressure
            70,     // puls
            85,     // saturationO2
            36.5,   // bodyTemperature
            new HashSet<>() // examinations
        );

        kSession.insert(patient);
        int firedRules = kSession.fireAllRules();

        assertEquals(1, firedRules); // Assuming only one rule will be triggered
    }

    @Test
    public void testCriticalBodyTemperatureAlert() {
        Patient patient = new Patient(
            4,
            "Bob",
            "Smith",
            "bob.smith@example.com",
            "password",
            LocalDateTime.now(),
            Role.Patient,
            120,    // bloodPressure
            70,     // puls
            98,     // saturationO2
            39.5,   // bodyTemperature
            new HashSet<>() // examinations
        );

        kSession.insert(patient);
        int firedRules = kSession.fireAllRules();

        assertEquals(1, firedRules); // Assuming only one rule will be triggered
    }
    
}