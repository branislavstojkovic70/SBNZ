package com.ftn.sbnz.service.tests;

import com.ftn.sbnz.model.models.users.Patient;
import com.ftn.sbnz.model.models.users.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.KieServices;

import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReportsTests {
    private static KieSession kSession;

    @BeforeAll
    public static void setup() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        kSession = kContainer.newKieSession("reportsKsession");
    }

    @Test
    public void testHighRiskPatientsWithMultipleFactors() {
        Patient highRiskPatient = new Patient(
            1,
            "John",
            "Doe",
            "john.doe@example.com",
            "password",
            LocalDateTime.now(),
            Role.Patient,
            150,    // bloodPressure
            110,    // puls
            85,     // saturationO2
            36.5,   // bodyTemperature
            new HashSet<>() // examinations
        );

        Patient lowRiskPatient = new Patient(
            2,
            "Jane",
            "Doe",
            "jane.doe@example.com",
            "password",
            LocalDateTime.now(),
            Role.Patient,
            120,    // bloodPressure
            80,     // puls
            95,     // saturationO2
            36.5,   // bodyTemperature
            new HashSet<>() // examinations
        );

        kSession.insert(highRiskPatient);
        kSession.insert(lowRiskPatient);
        kSession.fireAllRules();

        QueryResults results = kSession.getQueryResults("HighRiskPatientsWithMultipleFactors");

        assertEquals(1, results.size());
        assertTrue(results.iterator().next().get("$patient") instanceof Patient);
        assertEquals(highRiskPatient, results.iterator().next().get("$patient"));
    }

    
}
