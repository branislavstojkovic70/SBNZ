package com.ftn.sbnz.model.models.examinations;

import java.util.Set;

import com.ftn.sbnz.model.models.therapy.Therapy;
import com.ftn.sbnz.model.models.users.Patient;

public class ExaminationProcess {
    private Patient patient;
    private Set<Examination> examinations;
    private boolean ctDone;
    private boolean rtgDone;
    private boolean ctPositive;
    private boolean rtgPositive;
    private boolean biopsyDone;
    private boolean biopsyPositive;
    private boolean spirometryDone;
    private boolean dlcoDone;
    private boolean spiroergometryDone;
    private boolean spirometryBadResult;
    private boolean dlcoBadResult;
    private boolean spiroergometryBadResult;
    private boolean geneticTestDone;
    private boolean geneticTestBadResult;
    private Diagnosis diagnosis;
    private Therapy therapy;
    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    public Set<Examination> getExaminations() {
        return examinations;
    }
    public void setExaminations(Set<Examination> examinations) {
        this.examinations = examinations;
    }
    public boolean isCtDone() {
        return ctDone;
    }
    public void setCtDone(boolean ctDone) {
        this.ctDone = ctDone;
    }
    public boolean isRtgDone() {
        return rtgDone;
    }
    public void setRtgDone(boolean rtgDone) {
        this.rtgDone = rtgDone;
    }
    public boolean isCtPositive() {
        return ctPositive;
    }
    public void setCtPositive(boolean ctPositive) {
        this.ctPositive = ctPositive;
    }
    public boolean isRtgPositive() {
        return rtgPositive;
    }
    public void setRtgPositive(boolean rtgPositive) {
        this.rtgPositive = rtgPositive;
    }
    public boolean isBiopsyDone() {
        return biopsyDone;
    }
    public void setBiopsyDone(boolean biopsyDone) {
        this.biopsyDone = biopsyDone;
    }
    public boolean isBiopsyPositive() {
        return biopsyPositive;
    }
    public void setBiopsyPositive(boolean biopsyPositive) {
        this.biopsyPositive = biopsyPositive;
    }
    public boolean isSpirometryDone() {
        return spirometryDone;
    }
    public void setSpirometryDone(boolean spirometryDone) {
        this.spirometryDone = spirometryDone;
    }
    public boolean isDlcoDone() {
        return dlcoDone;
    }
    public void setDlcoDone(boolean dlcoDone) {
        this.dlcoDone = dlcoDone;
    }
    public boolean isSpiroergometryDone() {
        return spiroergometryDone;
    }
    public void setSpiroergometryDone(boolean spiroergometryDone) {
        this.spiroergometryDone = spiroergometryDone;
    }
    public boolean isSpirometryBadResult() {
        return spirometryBadResult;
    }
    public void setSpirometryBadResult(boolean spirometryBadResult) {
        this.spirometryBadResult = spirometryBadResult;
    }
    public boolean isDlcoBadResult() {
        return dlcoBadResult;
    }
    public void setDlcoBadResult(boolean dlcoBadResult) {
        this.dlcoBadResult = dlcoBadResult;
    }
    public boolean isSpiroergometryBadResult() {
        return spiroergometryBadResult;
    }
    public void setSpiroergometryBadResult(boolean spiroergometryBadResult) {
        this.spiroergometryBadResult = spiroergometryBadResult;
    }
    public boolean isGeneticTestDone() {
        return geneticTestDone;
    }
    public void setGeneticTestDone(boolean geneticTestDone) {
        this.geneticTestDone = geneticTestDone;
    }
    public boolean isGeneticTestBadResult() {
        return geneticTestBadResult;
    }
    public void setGeneticTestBadResult(boolean geneticTestBadResult) {
        this.geneticTestBadResult = geneticTestBadResult;
    }
    public Diagnosis getDiagnosis() {
        return diagnosis;
    }
    public void setDiagnosis(Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
    }
    public Therapy getTherapy() {
        return therapy;
    }
    public void setTherapy(Therapy therapy) {
        this.therapy = therapy;
    }
    public ExaminationProcess() {
    }
    public ExaminationProcess(Patient patient, Set<Examination> examinations, boolean ctDone, boolean rtgDone,
            boolean ctPositive, boolean rtgPositive, boolean biopsyDone, boolean biopsyPositive,
            boolean spirometryDone, boolean dlcoDone, boolean spiroergometryDone, boolean spirometryBadResult,
            boolean dlcoBadResult, boolean spiroergometryBadResult, boolean geneticTestDone,
            boolean geneticTestBadResult, Diagnosis diagnosis, Therapy therapy) {
        this.patient = patient;
        this.examinations = examinations;
        this.ctDone = ctDone;
        this.rtgDone = rtgDone;
        this.ctPositive = ctPositive;
        this.rtgPositive = rtgPositive;
        this.biopsyDone = biopsyDone;
        this.biopsyPositive = biopsyPositive;
        this.spirometryDone = spirometryDone;
        this.dlcoDone = dlcoDone;
        this.spiroergometryDone = spiroergometryDone;
        this.spirometryBadResult = spirometryBadResult;
        this.dlcoBadResult = dlcoBadResult;
        this.spiroergometryBadResult = spiroergometryBadResult;
        this.geneticTestDone = geneticTestDone;
        this.geneticTestBadResult = geneticTestBadResult;
        this.diagnosis = diagnosis;
        this.therapy = therapy;
    }

    
}