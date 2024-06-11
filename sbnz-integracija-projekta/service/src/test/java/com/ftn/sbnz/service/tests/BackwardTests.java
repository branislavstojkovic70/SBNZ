package com.ftn.sbnz.service.tests;

import com.ftn.sbnz.model.models.Fact;
import com.ftn.sbnz.model.models.Result;
import com.ftn.sbnz.model.models.alarms.Alarm;
import com.ftn.sbnz.model.models.examinations.Diagnosis;
import com.ftn.sbnz.model.models.examinations.Examination;
import com.ftn.sbnz.model.models.examinations.UpdatedExamination;
import com.ftn.sbnz.model.models.users.Doctor;
import com.ftn.sbnz.model.models.users.Patient;
import com.ftn.sbnz.model.models.users.Role;
import com.ftn.sbnz.service.ServiceApplication;
import com.ftn.sbnz.service.repository.alarms.AlarmRepository;
import com.ftn.sbnz.service.repository.users.PatientRepository;
import com.ftn.sbnz.service.repository.users.DoctorRepository;
import com.ftn.sbnz.service.service.WebSocketService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = ServiceApplication.class)
public class BackwardTests {

    private static KieSession kSession;
    private static Result result;

    @MockBean
    private WebSocketService webSocketService;

    @MockBean
    private PatientRepository patientRepository;

    @MockBean
    private DoctorRepository doctorRepository;

    @MockBean
    private AlarmRepository alarmRepository;

    @BeforeAll
    public static void setup() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        kSession = kContainer.newKieSession("bwKsession");
        result = new Result();
        kSession.setGlobal("result", result);
    }

    @BeforeEach
    public void init() {
        reset(webSocketService, patientRepository, doctorRepository, alarmRepository);
        kSession.setGlobal("webSocketService", webSocketService);
        kSession.setGlobal("patientRepository", patientRepository);
        kSession.setGlobal("doctorRepository", doctorRepository);
        kSession.setGlobal("alarmRepository", alarmRepository);
    }

    @Test
    public void testWarnChildrenOfTumorDiagnosis() {
        // Create parent and child patients
        Patient parent = new Patient(2, "Marko", "Markovic", "parent@example.com", "password", LocalDateTime.now().minusYears(30), Role.Patient, 120.0, 70, 98.0, 36.5, new HashSet<>());
        Patient child = new Patient(1, "Janko", "Markovic", "child@example.com", "password", LocalDateTime.now().minusYears(10), Role.Patient, 110.0, 80, 97.0, 36.0, new HashSet<>());

        // Create diagnosis with tumor detected
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setTumorDetected(true);

        // Create examination with the diagnosis
        Examination examination = new Examination();
        examination.setDiagnosis(diagnosis);

        // Add examination to parent's examinations
        HashSet<Examination> examinations = new HashSet<>();
        examinations.add(examination);
        parent.setExaminations(examinations);

        // Insert parent, child and fact into the session
        kSession.insert(parent);
        kSession.insert(child);
        kSession.insert(new Fact(child, parent));
        kSession.insert(new UpdatedExamination(parent, examination));

        // Mock repositories and services
        when(doctorRepository.findById(2)).thenReturn(Optional.of(new Doctor(2, "Dr. Doctor", "Doctor", "doctor@gmail.com", "password", LocalDateTime.now().minusYears(40), Role.Doctor, "pulmolog")));

        // Fire all rules
        kSession.fireAllRules();

        // Assert facts and alarms
        assertEquals(3, result.getFacts().size());
        assertEquals("Warning: Janko should schedule a screening due to parent's tumor diagnosis", result.getFacts().get(1));

        boolean alarmCreated = kSession.getObjects().stream()
                .anyMatch(obj -> obj instanceof Alarm && ((Alarm) obj).getDescription().equals("Parent has tumor") && ((Alarm) obj).getPatient().equals(child));
        assertTrue(alarmCreated, "Expected an alarm to be created for the child.");

        // Verify that the WebSocketService was called
        verify(webSocketService, times(1)).sendAlarm(any(Alarm.class));
    }
}
