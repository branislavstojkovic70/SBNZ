package com.ftn.sbnz.service.tests;

import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;

import com.ftn.sbnz.model.models.users.Patient;
import com.ftn.sbnz.model.events.OxygenSaturationEvent;
import com.ftn.sbnz.model.models.alarms.Alarm;
import com.ftn.sbnz.service.ServiceApplication;
import com.ftn.sbnz.service.service.WebSocketService;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class RuleTemplatesTest2 {

    @MockBean
    private WebSocketService webSocketService;

    @Test
    public void testSimpleTemplateWithSpreadsheet2() {
        try {
            InputStream template = RuleTemplatesTest2.class.getResourceAsStream("/rules/template/hypoxia_monitoring.drt");
            InputStream data = RuleTemplatesTest2.class.getResourceAsStream("/rules/template/hypoxia_monitoring_data.xls");

            ExternalSpreadsheetCompiler converter = new ExternalSpreadsheetCompiler();
            String drl = converter.compile(data, template, 3, 2);

            System.out.println(drl);

            KieSession ksession = createKieSessionFromDRLWithPseudoClock(drl);

            List<Alarm> alarmList = new ArrayList<>();
            ksession.setGlobal("alarms", alarmList);
            ksession.setGlobal("webSocketService", webSocketService);

            SessionPseudoClock clock = ksession.getSessionClock();

            Patient patient = new Patient();
            patient.setId(1);
            patient.setIme("John Doe");

            // Dodavanje događaja prema pravilima iz tabele
            OxygenSaturationEvent event1 = new OxygenSaturationEvent(patient, 84, new Date(clock.getCurrentTime())); // 85 je prag za 72h
            ksession.insert(event1);
            clock.advanceTime(1, TimeUnit.HOURS);

            OxygenSaturationEvent event2 = new OxygenSaturationEvent(patient, 84, new Date(clock.getCurrentTime()));
            ksession.insert(event2);
            clock.advanceTime(1, TimeUnit.HOURS);

            OxygenSaturationEvent event3 = new OxygenSaturationEvent(patient, 84, new Date(clock.getCurrentTime()));
            ksession.insert(event3);
            clock.advanceTime(1, TimeUnit.HOURS);

            OxygenSaturationEvent event4 = new OxygenSaturationEvent(patient, 84, new Date(clock.getCurrentTime()));
            ksession.insert(event4);
            clock.advanceTime(1, TimeUnit.HOURS);

            OxygenSaturationEvent event5 = new OxygenSaturationEvent(patient, 84, new Date(clock.getCurrentTime()));
            ksession.insert(event5);

            ksession.insert(patient);

            System.out.println("Pre aktiviranja pravila");
            int firedRules = ksession.fireAllRules();
            System.out.println("Broj aktiviranih pravila: " + firedRules);

            assertTrue("Alarm list should not be empty", !alarmList.isEmpty());
            boolean alarmCreated = alarmList.stream()
                    .anyMatch(alarm -> alarm.getPatient().getId().equals(patient.getId()) &&
                            alarm.getDescription().contains("Hypoxia detected for patient: " + patient.getIme()));

            assertTrue("Alarm should be created for the patient", alarmCreated);
            verify(webSocketService, times(3)).sendAlarm(any(Alarm.class));

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
