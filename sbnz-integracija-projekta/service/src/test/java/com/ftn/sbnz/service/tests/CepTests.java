package com.ftn.sbnz.service.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.ftn.sbnz.model.events.TemperatureEvent;
import com.ftn.sbnz.model.models.Result;
import com.ftn.sbnz.model.models.examinations.Examination;
import com.ftn.sbnz.model.models.therapy.Operation;
import com.ftn.sbnz.model.models.therapy.TherapyState;
import com.ftn.sbnz.model.models.therapy.TherapyType;
import com.ftn.sbnz.model.models.users.Patient;

public class CepTests {
    private static KieSession kSession;
    private static Result result;

    @BeforeAll
    public static void setup() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        kSession = kContainer.newKieSession("cepKsession");
        result = new Result();
        kSession.setGlobal("result", result);
    }

    @Test
    public void testTemperatureWarningRule() {
        Patient patient = new Patient();
        patient.setId(1);
        patient.setIme("John");
        patient.setPrezime("Doe");

        Date now = new Date();
        Date fiveHoursAgo = new Date(now.getTime() - 5 * 60 * 60 * 1000);
        Date tenHoursAgo = new Date(now.getTime() - 10 * 60 * 60 * 1000);

        TemperatureEvent temp1 = new TemperatureEvent(patient, 39.0, now);
        TemperatureEvent temp2 = new TemperatureEvent(patient, 39.2, fiveHoursAgo);
        TemperatureEvent temp3 = new TemperatureEvent(patient, 38.8, tenHoursAgo);

        kSession.insert(patient);
        kSession.insert(temp1);
        kSession.insert(temp2);
        kSession.insert(temp3);

        int firedRules = kSession.fireAllRules();
        assertTrue(firedRules > 0, "Expected at least one rule to be fired.");

        // Provera rezultata
        assertTrue(result.getFacts().contains("Temperature Warning Generated for patient: John"), "Expected Temperature Warning to be generated.");
    }

    @Test
    public void testTemperatureWarningAndEmergencyInterventionRule() {
        Patient patient = new Patient();
        patient.setId(1);
        patient.setIme("John");
        patient.setPrezime("Doe");

        Date now = new Date();
        Date oneHourAgo = new Date(now.getTime() - 1 * 60 * 60 * 1000);
        Date twoHoursAgo = new Date(now.getTime() - 2 * 60 * 60 * 1000);
        Date threeHoursAgo = new Date(now.getTime() - 3 * 60 * 60 * 1000);
        Date fiveHoursAgo = new Date(now.getTime() - 5 * 60 * 60 * 1000);

        TemperatureEvent temp1 = new TemperatureEvent(patient, 39.5, now);
        TemperatureEvent temp2 = new TemperatureEvent(patient, 39.7, oneHourAgo);
        TemperatureEvent temp3 = new TemperatureEvent(patient, 39.6, twoHoursAgo);
        TemperatureEvent temp4 = new TemperatureEvent(patient, 39.0, threeHoursAgo);
        TemperatureEvent temp5 = new TemperatureEvent(patient, 39.0, fiveHoursAgo);

        Operation operation = new Operation();
        operation.setTherapyState(TherapyState.PLANNED);
        operation.setScheduledFor(LocalDateTime.now().plusHours(12));
        operation.setTherapyType(TherapyType.OPERATION);

        Examination examination = new Examination();
        examination.setTherapy(operation);

        HashSet<Examination> examinations = new HashSet<>();
        examinations.add(examination);

        patient.setExaminations(examinations);

        kSession.insert(patient);
        kSession.insert(temp1);
        kSession.insert(temp2);
        kSession.insert(temp3);
        kSession.insert(temp4);
        kSession.insert(temp5);

        int firedRules = kSession.fireAllRules();
        assertTrue(firedRules == 0, "Expected at least one rule to be fired.");

        // Provera rezultata za alarm
        firedRules += kSession.fireAllRules();

        assertTrue(result.getFacts().contains("Temperature Warning Generated for patient: John"), "Expected Temperature Warning to be generated.");

    }
}
