package com.ftn.sbnz.service.tests;

import com.ftn.sbnz.model.models.users.Patient;
import com.ftn.sbnz.model.models.users.Role;
import com.ftn.sbnz.service.ServiceApplication;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.test.context.SpringBootTest;
import org.kie.api.KieServices;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
            185, 
            70,
            98,
            36.5,
            new HashSet<>()
        );

        kSession.insert(patient);
        int firedRules = kSession.fireAllRules();

        assertEquals(1, firedRules);
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
            120,
            130,
            98,
            36.5,
            new HashSet<>()
        );

        kSession.insert(patient);
        int firedRules = kSession.fireAllRules();

        assertEquals(1, firedRules);
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
            120,
            70,
            85,
            36.5,
            new HashSet<>()
        );

        kSession.insert(patient);
        int firedRules = kSession.fireAllRules();

        assertEquals(1, firedRules);
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
            120,
            70,
            98,
            39.5,
            new HashSet<>()
        );

        kSession.insert(patient);
        int firedRules = kSession.fireAllRules();

        assertEquals(1, firedRules);
    }
    
}