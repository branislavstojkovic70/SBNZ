package com.ftn.sbnz.service.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import com.ftn.sbnz.model.models.alarms.Alarm;
import com.ftn.sbnz.service.ServiceApplication;
import com.ftn.sbnz.service.service.WebSocketService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.api.time.SessionPseudoClock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ftn.sbnz.model.events.PulseEvent;
import com.ftn.sbnz.model.events.TemperatureEvent;
import com.ftn.sbnz.model.models.Result;
import com.ftn.sbnz.model.models.examinations.Examination;
import com.ftn.sbnz.model.models.therapy.Operation;
import com.ftn.sbnz.model.models.therapy.TherapyState;
import com.ftn.sbnz.model.models.therapy.TherapyType;
import com.ftn.sbnz.model.models.users.Patient;

@SpringBootTest(classes = ServiceApplication.class)
public class CepTests {
    private static KieSession kSession;
    private static Result result;
    private static SessionPseudoClock clock;

    @MockBean
    private WebSocketService webSocketService;

    @BeforeAll
    public static void setup() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        KieSessionConfiguration config = ks.newKieSessionConfiguration();
        config.setOption(ClockTypeOption.get("pseudo"));
        kSession = kContainer.newKieSession("cepKsession", config);
        result = new Result();
        kSession.setGlobal("result", result);
        clock = kSession.getSessionClock();
    }

    @BeforeEach
    public void init() {
        reset(webSocketService); // Reset the mock before each test
        kSession.setGlobal("webSocketService", webSocketService);
    }

    @Test
    public void testGenerateTemperatureWarning() {
        Patient patient = new Patient();
        patient.setId(1);
        patient.setIme("John");
        patient.setPrezime("Doe");

        long now = clock.getCurrentTime();
        TemperatureEvent temp1 = new TemperatureEvent(patient, 39.0, new Date(now));
        kSession.insert(patient);
        kSession.insert(temp1);
        kSession.fireAllRules();

        clock.advanceTime(5, TimeUnit.HOURS);
        TemperatureEvent temp2 = new TemperatureEvent(patient, 39.2, new Date(clock.getCurrentTime()));
        kSession.insert(temp2);
        kSession.fireAllRules();

        clock.advanceTime(5, TimeUnit.HOURS);
        TemperatureEvent temp3 = new TemperatureEvent(patient, 38.8, new Date(clock.getCurrentTime()));
        kSession.insert(temp3);
        kSession.fireAllRules();

        clock.advanceTime(1, TimeUnit.SECONDS);
        int firedRules = kSession.fireAllRules();
        System.out.println("Number of fired rules: " + firedRules);

        assertTrue(result.getFacts().contains("Temperature Warning Generated for patient: John"), "Expected Temperature Warning to be generated.");
    }

    @Test
    public void testCheckPulseAfterTemperatureWarning() {
        Patient patient = new Patient();
        patient.setId(1);
        patient.setIme("John");
        patient.setPrezime("Doe");

        long now = clock.getCurrentTime();
        TemperatureEvent temp1 = new TemperatureEvent(patient, 39.0, new Date(now));
        kSession.insert(patient);
        kSession.insert(temp1);
        kSession.fireAllRules();

        clock.advanceTime(1, TimeUnit.HOURS);
        TemperatureEvent temp2 = new TemperatureEvent(patient, 39.1, new Date(clock.getCurrentTime()));
        kSession.insert(temp2);
        kSession.fireAllRules();

        clock.advanceTime(2, TimeUnit.HOURS);
        PulseEvent pulse1 = new PulseEvent(patient, 110, new Date(clock.getCurrentTime()));
        kSession.insert(pulse1);
        kSession.fireAllRules();

        clock.advanceTime(1, TimeUnit.HOURS);
        PulseEvent pulse2 = new PulseEvent(patient, 105, new Date(clock.getCurrentTime()));
        kSession.insert(pulse2);
        kSession.fireAllRules();

        clock.advanceTime(2, TimeUnit.HOURS);
        PulseEvent pulse3 = new PulseEvent(patient, 108, new Date(clock.getCurrentTime()));
        kSession.insert(pulse3);
        kSession.fireAllRules();

        clock.advanceTime(1, TimeUnit.SECONDS);
        int firedRules = kSession.fireAllRules();
        System.out.println("Number of fired rules: " + firedRules);

        assertTrue(result.getFacts().contains("Temperature Warning Generated for patient: John"), "Expected Temperature Warning to be generated.");
        assertTrue(result.getFacts().contains("Pulse Warning Generated for patient: John"), "Expected Pulse Warning to be generated.");
    }

    @Test
    public void testGenerateEmergencyIntervention() {
        Patient patient = new Patient();
        patient.setId(1);
        patient.setIme("John");
        patient.setPrezime("Doe");

        long now = clock.getCurrentTime();
        TemperatureEvent temp1 = new TemperatureEvent(patient, 39.0, new Date(now));
        kSession.insert(patient);
        kSession.insert(temp1);
        kSession.fireAllRules();

        clock.advanceTime(1, TimeUnit.HOURS);
        TemperatureEvent temp2 = new TemperatureEvent(patient, 39.1, new Date(clock.getCurrentTime()));
        kSession.insert(temp2);
        kSession.fireAllRules();

        clock.advanceTime(1, TimeUnit.HOURS);
        PulseEvent pulse1 = new PulseEvent(patient, 110, new Date(clock.getCurrentTime()));
        kSession.insert(pulse1);
        kSession.fireAllRules();

        clock.advanceTime(1, TimeUnit.HOURS);
        PulseEvent pulse2 = new PulseEvent(patient, 105, new Date(clock.getCurrentTime()));
        kSession.insert(pulse2);
        kSession.fireAllRules();

        Operation operation = new Operation();
        operation.setTherapyState(TherapyState.PLANNED);
        operation.setScheduledFor(LocalDateTime.now().plusHours(12));
        operation.setTherapyType(TherapyType.OPERATION);

        Examination examination = new Examination();
        examination.setTherapy(operation);

        HashSet<Examination> examinations = new HashSet<>();
        examinations.add(examination);

        patient.setExaminations(examinations);

        kSession.insert(operation);
        kSession.insert(examination);

        clock.advanceTime(1, TimeUnit.HOURS);
        PulseEvent pulse3 = new PulseEvent(patient, 108, new Date(clock.getCurrentTime()));
        kSession.insert(pulse3);
        kSession.fireAllRules();

        clock.advanceTime(1, TimeUnit.SECONDS);
        int firedRules = kSession.fireAllRules();
        System.out.println("Number of fired rules: " + firedRules);

        assertTrue(result.getFacts().contains("Temperature Warning Generated for patient: John"), "Expected Temperature Warning to be generated.");
        assertTrue(result.getFacts().contains("Pulse Warning Generated for patient: John"), "Expected Pulse Warning to be generated.");
        assertTrue(result.getFacts().contains("Emergency Intervention Required for patient: John"), "Expected Emergency Intervention to be generated.");
        assertEquals(TherapyState.CANCELED, operation.getTherapyState(), "Expected operation to be canceled.");

        verify(webSocketService, times(1)).sendAlarm(any(Alarm.class));
    }
}
