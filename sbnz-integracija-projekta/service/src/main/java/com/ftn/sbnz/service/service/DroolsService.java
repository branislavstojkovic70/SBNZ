package com.ftn.sbnz.service.service;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.springframework.transaction.annotation.Transactional;

import com.ftn.sbnz.model.models.Result;
import com.ftn.sbnz.model.models.alarms.Alarm;
import com.ftn.sbnz.model.models.examinations.Diagnosis;
import com.ftn.sbnz.model.models.examinations.Examination;
import com.ftn.sbnz.model.models.examinations.Symptom;
import com.ftn.sbnz.model.models.examinations.TestResult;
import com.ftn.sbnz.model.models.therapy.Therapy;
import com.ftn.sbnz.model.models.users.Patient;
import com.ftn.sbnz.service.repository.alarms.AlarmRepository;
import com.ftn.sbnz.service.repository.examination.DiagnosisRepository;
import com.ftn.sbnz.service.repository.examination.ExaminationRepository;
import com.ftn.sbnz.service.repository.examination.SymptomRepository;
import com.ftn.sbnz.service.repository.examination.TestResultRepository;
import com.ftn.sbnz.service.repository.therapy.TherapyRepository;
import com.ftn.sbnz.service.repository.users.PatientRepository;

@Service
public class DroolsService {
@Autowired
    private ExaminationRepository examinationRepository;
    @Autowired
    private SymptomRepository symptomRepository;
    @Autowired
    private TestResultRepository testResultRepository;
    @Autowired
    private DiagnosisRepository diagnosisRepository;
    @Autowired
    private TherapyRepository therapyRepository;
    @Autowired
    private AlarmRepository alarmRepository;
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private WebSocketService webSocketService;

    private KieSession bwKsession;
    private KieSession cepKsession;
    private KieSession simpleKsession;
    private KieSession forward1Ksession;
    private KieSession forward2Ksession;
    private KieSession template1Ksession;
    private KieSession template2Ksession;
    private KieSession reportsKsession;
    private static Result result;

    public DroolsService() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();

        result = new Result();

        this.bwKsession = kieContainer.newKieSession("bwKsession");
        this.bwKsession.setGlobal("result", result);
        this.bwKsession.setGlobal("webSocketService", webSocketService);

        this.cepKsession = createCepKsession(kieContainer, kieServices);

        this.simpleKsession = kieContainer.newKieSession("simpleKsession");
        Set<String> criticalSymptoms = new HashSet<>(Arrays.asList("umor", "slabost", "kašalj", "otežano disanje", "bol u grudima", "hemoptiza"));
        this.simpleKsession.setGlobal("criticalSymptoms", criticalSymptoms);

        this.forward1Ksession = kieContainer.newKieSession("forward1Ksession");
        this.forward1Ksession.setGlobal("criticalSymptoms", criticalSymptoms);

        this.forward2Ksession = kieContainer.newKieSession("forward2Ksession");

        this.template1Ksession = createTemplate1Ksession(kieServices);

        this.template2Ksession = createTemplate2Ksession(kieServices);

        this.reportsKsession = kieContainer.newKieSession("reportsKsession");
    }

    public KieSession getBwKsession() {
        return bwKsession;
    }

    public KieSession getCepKsession() {
        return cepKsession;
    }

    public KieSession getSimpleKsession() {
        return simpleKsession;
    }

    public KieSession getForward1Ksession() {
        return forward1Ksession;
    }

    public KieSession getForward2Ksession() {
        return forward2Ksession;
    }

    public KieSession getTemplate1Ksession() {
        return template1Ksession;
    }

    public KieSession getTemplate2Ksession() {
        return template2Ksession;
    }

    public KieSession getReportsKsession() {
        return reportsKsession;
    }

    private KieSession createCepKsession(KieContainer kieContainer, KieServices kieServices) {
        KieSessionConfiguration config = kieServices.newKieSessionConfiguration();
        config.setOption(ClockTypeOption.get("pseudo"));
        KieSession cepKsession = kieContainer.newKieSession("cepKsession", config);
        Result result = new Result();
        cepKsession.setGlobal("result", result);
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

    public QueryResults getAllHighRiskedPatients(){
        return reportsKsession.getQueryResults("HighRiskPatientsWithMultipleFactors");
    }

    public void insertAndFireAllRules(Object object) {
        bwKsession.insert(object);
        bwKsession.fireAllRules();
        synchronizeWithDatabase(bwKsession);

        cepKsession.insert(object);
        cepKsession.fireAllRules();
        synchronizeWithDatabase(cepKsession);

        simpleKsession.insert(object);
        simpleKsession.fireAllRules();
        synchronizeWithDatabase(simpleKsession);

        forward1Ksession.insert(object);
        forward1Ksession.fireAllRules();
        synchronizeWithDatabase(forward1Ksession);

        forward2Ksession.insert(object);
        forward2Ksession.fireAllRules();
        synchronizeWithDatabase(forward2Ksession);

        template1Ksession.insert(object);
        template1Ksession.fireAllRules();
        synchronizeWithDatabase(template1Ksession);

        template2Ksession.insert(object);
        template2Ksession.fireAllRules();
        synchronizeWithDatabase(template2Ksession);

        reportsKsession.insert(object);
        reportsKsession.fireAllRules();
        synchronizeWithDatabase(reportsKsession);
    }

    @Transactional
    public void synchronizeWithDatabase(KieSession kieSession) {
        kieSession.getObjects().forEach(obj -> {
            if (obj instanceof Examination) {
                examinationRepository.saveAndFlush((Examination) obj);
            } else if (obj instanceof Symptom) {
                symptomRepository.saveAndFlush((Symptom) obj);
            } else if (obj instanceof TestResult) {
                testResultRepository.saveAndFlush((TestResult) obj);
            } else if (obj instanceof Diagnosis) {
                diagnosisRepository.saveAndFlush((Diagnosis) obj);
            } else if (obj instanceof Therapy) {
                if (therapyRepository.existsById(((Therapy) obj).getId())) {
                    therapyRepository.save((Therapy) obj);
                } else {
                    therapyRepository.saveAndFlush((Therapy) obj);
                }
            } else if (obj instanceof Alarm) {
                alarmRepository.saveAndFlush((Alarm) obj);
            } else if (obj instanceof Patient) {
                patientRepository.saveAndFlush((Patient) obj);
            } else {
                System.out.println("Unknown object type: " + obj.getClass().getName());
            }
        });
    }


    public void disposeSessions() {
        bwKsession.dispose();
        cepKsession.dispose();
        simpleKsession.dispose();
        forward1Ksession.dispose();
        forward2Ksession.dispose();
        template1Ksession.dispose();
        template2Ksession.dispose();
        reportsKsession.dispose();
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