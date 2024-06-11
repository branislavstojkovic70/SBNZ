package com.ftn.sbnz.model.models.examinations;

import com.ftn.sbnz.model.models.users.Patient;

public class UpdatedExamination {
    private Patient patient;
    private Examination examination;
    
    public UpdatedExamination(Patient patient, Examination examination) {
        this.patient = patient;
        this.examination = examination;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Examination getExamination() {
        return examination;
    }

    public void setExamination(Examination examination) {
        this.examination = examination;
    }
}
