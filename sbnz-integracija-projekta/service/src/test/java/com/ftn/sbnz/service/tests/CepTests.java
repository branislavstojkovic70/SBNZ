package com.ftn.sbnz.service.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.ftn.sbnz.service.ServiceApplication;
import com.ftn.sbnz.service.repository.alarms.AlarmRepository;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ftn.sbnz.model.events.PulseEvent;
import com.ftn.sbnz.model.events.TemperatureEvent;
import com.ftn.sbnz.model.models.Result;
import com.ftn.sbnz.model.models.examinations.Examination;
import com.ftn.sbnz.model.models.therapy.Operation;
import com.ftn.sbnz.model.models.therapy.OperationType;
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
    @MockBean
    private AlarmRepository alarmRepository;

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
        kSession.setGlobal("alarmRepository", alarmRepository);
    }

    @Test
    public void testAllRules() {
        Patient patient = new Patient();
        patient.setId(1);
        patient.setIme("John");

        TemperatureEvent tempEvent1 = new TemperatureEvent(patient, 39.0, new Date());
        kSession.insert(tempEvent1);
        clock.advanceTime(1, TimeUnit.HOURS);
        
        TemperatureEvent tempEvent2 = new TemperatureEvent(patient, 39.5, new Date());
        kSession.insert(tempEvent2);
        clock.advanceTime(1, TimeUnit.HOURS);

        PulseEvent pulseEvent1 = new PulseEvent(patient, 110, new Date());
        kSession.insert(pulseEvent1);
        clock.advanceTime(1, TimeUnit.HOURS);
        
        PulseEvent pulseEvent2 = new PulseEvent(patient, 115, new Date());
        kSession.insert(pulseEvent2);
        clock.advanceTime(1, TimeUnit.HOURS);

        Examination examination = new Examination();
        Operation o = new Operation(null, LocalDateTime.now(), LocalDateTime.now().plusDays(1), "Operation1 ",
        TherapyType.OPERATION, TherapyState.PLANNED, "desc2", LocalDateTime.now().plusHours(12),
        "", OperationType.LOBECTOMY);
        examination.setTherapy(o);
        Set<Examination> examinations = new HashSet<Examination>();
        examinations.add(examination);
        patient.setExaminations(examinations);

        kSession.insert(patient);
        kSession.insert(examination);
        kSession.insert(o);

        kSession.fireAllRules();

        assertTrue(result.getFacts().contains("Temperature Warning Generated for patient: John"));
        assertTrue(result.getFacts().contains("Pulse Warning Generated for patient: John"));
        assertEquals(TherapyState.CANCELED, examination.getTherapy().getTherapyState());
        assertTrue(result.getFacts().contains("Emergency Intervention Required for patient: John"));

        verify(alarmRepository, times(2)).save(any());
    }
}
