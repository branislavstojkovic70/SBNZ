package com.ftn.sbnz.service.dtos.request;

import java.util.ArrayList;

import com.ftn.sbnz.model.models.examinations.Examination;
import com.ftn.sbnz.model.models.users.Patient;

public class DetermineDiagnosisRequest {
    private Patient patient;
    private ArrayList<Examination> examinations;
    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    public ArrayList<Examination> getExaminations() {
        return examinations;
    }
    public void setExaminations(ArrayList<Examination> examinations) {
        this.examinations = examinations;
    }
    public DetermineDiagnosisRequest() {
    }
    public DetermineDiagnosisRequest(Patient patient, ArrayList<Examination> examinations) {
        this.patient = patient;
        this.examinations = examinations;
    }
   
}
