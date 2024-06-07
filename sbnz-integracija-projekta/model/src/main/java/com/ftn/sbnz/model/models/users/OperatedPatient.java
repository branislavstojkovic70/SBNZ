package com.ftn.sbnz.model.models.users;

import com.ftn.sbnz.model.models.therapy.Operation;
import com.ftn.sbnz.model.models.therapy.Therapy;

public class OperatedPatient {
    private Patient patient;
    private Operation operation;
    private boolean pulmonaryResistance;
    private boolean pulmonaryHypertension;
    private String note;
    private Therapy furtherTherapy;
    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    public Operation getOperation() {
        return operation;
    }
    public void setOperation(Operation operation) {
        this.operation = operation;
    }
    public boolean isPulmonaryResistance() {
        return pulmonaryResistance;
    }
    public void setPulmonaryResistance(boolean pulmonaryResistance) {
        this.pulmonaryResistance = pulmonaryResistance;
    }
    public boolean isPulmonaryHypertension() {
        return pulmonaryHypertension;
    }
    public void setPulmonaryHypertension(boolean pulmonaryHypertension) {
        this.pulmonaryHypertension = pulmonaryHypertension;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public Therapy getFurtherTherapy() {
        return furtherTherapy;
    }
    public void setFurtherTherapy(Therapy furtherTherapy) {
        this.furtherTherapy = furtherTherapy;
    }
    public OperatedPatient(Patient patient, Operation operation, boolean pulmonaryResistance,
            boolean pulmonaryHypertension, String note, Therapy furtherTherapy) {
        this.patient = patient;
        this.operation = operation;
        this.pulmonaryResistance = pulmonaryResistance;
        this.pulmonaryHypertension = pulmonaryHypertension;
        this.note = note;
        this.furtherTherapy = furtherTherapy;
    }
    public OperatedPatient() {
    }

    
}
