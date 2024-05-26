package com.ftn.sbnz.service.tests;

import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.drools.decisiontable.ExternalSpreadsheetCompiler;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.time.SessionPseudoClock;
import org.kie.internal.utils.KieHelper;

import com.ftn.sbnz.model.models.users.Patient;
import com.ftn.sbnz.model.models.examinations.Symptom;
import com.ftn.sbnz.model.models.examinations.SymptomFrequency;
import com.ftn.sbnz.model.models.alarms.Alarm;
import com.ftn.sbnz.model.models.examinations.Examination;

public class RuleTemplatesTest1 {

    @Test
    public void testSimpleTemplateWithSpreadsheet2() {
        try {
            InputStream template = RuleTemplatesTest1.class.getResourceAsStream("/rules/template/symptom_aggravation.drt");
            InputStream data = RuleTemplatesTest1.class.getResourceAsStream("/rules/template/symptom_aggravation_data.xls");

            ExternalSpreadsheetCompiler converter = new ExternalSpreadsheetCompiler();
            String drl = converter.compile(data, template, 3, 2);

            System.out.println(drl); 

            KieSession ksession = createKieSessionFromDRLWithPseudoClock(drl);

            List<Alarm> alarmList = new ArrayList<>();
            ksession.setGlobal("alarms", alarmList);

            SessionPseudoClock clock = ksession.getSessionClock();

            Patient patient = new Patient();
            patient.setId(1);
            patient.setIme("John Doe");

            Symptom symptom1 = new Symptom();
            symptom1.setIntensity(9);
            symptom1.setSymptomFrequency(SymptomFrequency.CONSTANT);

            Symptom symptom2 = new Symptom();
            symptom2.setIntensity(9);
            symptom2.setSymptomFrequency(SymptomFrequency.CONSTANT);

            Symptom symptom3 = new Symptom();
            symptom3.setIntensity(9);
            symptom3.setSymptomFrequency(SymptomFrequency.CONSTANT);

            Examination examination = new Examination();
            examination.setSymptoms(new HashSet<>(List.of(symptom1, symptom2, symptom3)));

            patient.setExaminations(new HashSet<>(List.of(examination)));

            ksession.insert(patient);
            ksession.insert(examination);
            ksession.insert(symptom1);
            ksession.insert(symptom2);
            ksession.insert(symptom3);

            clock.advanceTime(10, TimeUnit.MINUTES);
            clock.advanceTime(5, TimeUnit.MINUTES);
            clock.advanceTime(1, TimeUnit.MINUTES);

            int firedRules = ksession.fireAllRules();

            System.out.println("Number of fired rules: " + firedRules);

            assertTrue("Alarm list should not be empty", !alarmList.isEmpty());
            boolean alarmCreated = alarmList.stream()
                .anyMatch(alarm -> alarm.getPatient().getId().equals(patient.getId()) &&
                                   alarm.getDescription().contains("Symptom aggravation detected for patient: " + patient.getIme()));

            assertTrue("Alarm should be created for the patient", alarmCreated);

        } catch (Exception e) {
            e.printStackTrace();
            throw e; 
        }
    }

    private KieSession createKieSessionFromDRLWithPseudoClock(String drl) {
        KieHelper kieHelper = new KieHelper();
        kieHelper.addContent(drl, ResourceType.DRL);
    
        Results results = kieHelper.verify();
    
        if (results.hasMessages(Message.Level.WARNING, Message.Level.ERROR)) {
            List<Message> messages = results.getMessages(Message.Level.WARNING, Message.Level.ERROR);
            for (Message message : messages) {
                System.out.println("Error: " + message.getText());
            }
            throw new IllegalStateException("Compilation errors were found. Check the logs.");
        }

        KieServices kieServices = KieServices.Factory.get();
        org.kie.api.runtime.KieSessionConfiguration config = kieServices.newKieSessionConfiguration();
        config.setOption(org.kie.api.runtime.conf.ClockTypeOption.get("pseudo"));
        return kieHelper.build(EventProcessingOption.STREAM).newKieSession(config, null);
    }
}
