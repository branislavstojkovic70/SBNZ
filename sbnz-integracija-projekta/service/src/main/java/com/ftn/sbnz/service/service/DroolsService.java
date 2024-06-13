package com.ftn.sbnz.service.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.drools.decisiontable.ExternalSpreadsheetCompiler;
import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.sbnz.model.models.Result;
import com.ftn.sbnz.model.models.alarms.Alarm;
import com.ftn.sbnz.service.repository.alarms.AlarmRepository;
import com.ftn.sbnz.service.repository.examination.ExaminationRepository;
import com.ftn.sbnz.service.repository.therapy.TherapyRepository;
import com.ftn.sbnz.service.repository.users.DoctorRepository;
import com.ftn.sbnz.service.repository.users.PatientRepository;

@Service
public class DroolsService {
    private ExaminationRepository examinationRepository;
    private PatientRepository patientRepository;
    private WebSocketService webSocketService;
    private DoctorRepository doctorRepository;
    private AlarmRepository alarmRepository;
    private TherapyRepository therapyRepository;

    private KieSession bwKsession;
    private KieSession cepKsession;
    private KieSession simpleKsession;
    private KieSession forward1Ksession;
    private KieSession forward2Ksession;
    private KieSession template1Ksession;
    private KieSession template2Ksession;
    private KieSession reportsKsession;
    private static Result result;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public DroolsService(ExaminationRepository examinationRepository, PatientRepository patientRepository,
            WebSocketService webSocketService, DoctorRepository doctorRepository, AlarmRepository alarmRepository,
            EntityManager em, TherapyRepository therapyRepository) {
        this.examinationRepository = examinationRepository;
        this.patientRepository = patientRepository;
        this.webSocketService = webSocketService;
        this.doctorRepository = doctorRepository;
        this.alarmRepository = alarmRepository;
        this.entityManager = em;
        this.therapyRepository = therapyRepository;
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();

        this.result = new Result();

        this.bwKsession = kieContainer.newKieSession("bwKsession");
        this.bwKsession.setGlobal("result", this.result);
        this.bwKsession.setGlobal("webSocketService", this.webSocketService);
        this.bwKsession.setGlobal("patientRepository", this.patientRepository);
        this.bwKsession.setGlobal("doctorRepository", this.doctorRepository);
        this.bwKsession.setGlobal("alarmRepository", this.alarmRepository);

        this.cepKsession = createCepKsession(kieContainer, kieServices);

        this.simpleKsession = kieContainer.newKieSession("simpleKsession");
        Set<String> criticalSymptoms = new HashSet<>(
                Arrays.asList("umor", "slabost", "kašalj", "otežano disanje", "bol u grudima", "hemoptiza"));
        this.simpleKsession.setGlobal("criticalSymptoms", criticalSymptoms);

        this.forward1Ksession = kieContainer.newKieSession("forward1Ksession");
        this.forward1Ksession.setGlobal("criticalSymptoms", criticalSymptoms);
        this.forward1Ksession.setGlobal("examinationRepository", this.examinationRepository);
        this.forward1Ksession.setGlobal("patientRepository", this.patientRepository);
        this.forward1Ksession.setGlobal("webSocketService", this.webSocketService);
        this.forward1Ksession.setGlobal("entityManager", this.entityManager);

        this.forward2Ksession = kieContainer.newKieSession("forward2Ksession");
        this.forward2Ksession.setGlobal("webSocketService", this.webSocketService);


        this.template1Ksession = createTemplate1Ksession(kieServices);
        List<Alarm> alarmList = new ArrayList<>();
        this.template1Ksession.setGlobal("alarms", alarmList);
        this.template1Ksession.setGlobal("webSocketService", this.webSocketService);
        this.template1Ksession.setGlobal("alarmRepository", this.alarmRepository);
        this.template1Ksession.setGlobal("entityManager", this.entityManager);

        this.template2Ksession = createTemplate2Ksession(kieServices);
        this.template2Ksession.setGlobal("alarms", alarmList);
        this.template2Ksession.setGlobal("webSocketService", this.webSocketService);
        this.template2Ksession.setGlobal("alarmRepository", this.alarmRepository);
        this.template2Ksession.setGlobal("entityManager", this.entityManager);

        this.reportsKsession = kieContainer.newKieSession("reportsKsession");
    }

    public KieSession getBwKsession() {
        return this.bwKsession;
    }

    public KieSession getCepKsession() {
        return this.cepKsession;
    }

    public KieSession getSimpleKsession() {
        return this.simpleKsession;
    }

    public KieSession getForward1Ksession() {
        return this.forward1Ksession;
    }

    public KieSession getForward2Ksession() {
        return this.forward2Ksession;
    }

    public KieSession getTemplate1Ksession() {
        return this.template1Ksession;
    }

    public KieSession getTemplate2Ksession() {
        return this.template2Ksession;
    }

    public KieSession getReportsKsession() {
        return this.reportsKsession;
    }

    private KieSession createCepKsession(KieContainer kieContainer, KieServices kieServices) {
        KieSessionConfiguration config = kieServices.newKieSessionConfiguration();
        config.setOption(ClockTypeOption.get("pseudo"));
        KieSession cepKsession = kieContainer.newKieSession("cepKsession", config);
        Result result = new Result();
        cepKsession.setGlobal("result", result);
        cepKsession.setGlobal("webSocketService", this.webSocketService);
        cepKsession.setGlobal("alarmRepository", this.alarmRepository);
        return cepKsession;
    }

    private KieSession createTemplate1Ksession(KieServices kieServices) {
        try {
            InputStream template = getClass().getResourceAsStream("/rules/template/symptom_aggravation.drt");
            InputStream data = getClass().getResourceAsStream("/rules/template/symptom_aggravation_data.xls");

            if (template == null || data == null) {
                throw new RuntimeException("Template or data file not found 1");
            }
            ExternalSpreadsheetCompiler converter = new ExternalSpreadsheetCompiler();
            String drl = converter.compile(data, template, 3, 2);

            System.out.println(drl);

            return createKieSessionFromDRLWithPseudoClock(drl);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating template KieSession", e);
        }
    }

    private KieSession createTemplate2Ksession(KieServices kieServices) {
        try {
            InputStream template = getClass().getResourceAsStream("/rules/template/hypoxia_monitoring.drt");
            InputStream data = getClass().getResourceAsStream("/rules/template/hypoxia_monitoring_data.xls");

            if (template == null || data == null) {
                throw new RuntimeException("Template or data file not found 2");
            }
            ExternalSpreadsheetCompiler converter = new ExternalSpreadsheetCompiler();
            String drl = converter.compile(data, template, 3, 2);

            System.out.println(drl);

            return createKieSessionFromDRLWithPseudoClock(drl);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating template KieSession", e);
        }
    }

    public QueryResults getAllHighRiskedPatients() {
        return this.reportsKsession.getQueryResults("HighRiskPatientsWithMultipleFactors");
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
