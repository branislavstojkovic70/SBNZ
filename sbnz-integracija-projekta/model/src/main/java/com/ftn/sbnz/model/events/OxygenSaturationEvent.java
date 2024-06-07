package com.ftn.sbnz.model.events;

import java.util.Date;

import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import com.ftn.sbnz.model.models.users.Patient;

@Role(Role.Type.EVENT)
@Timestamp("timestamp")
public class OxygenSaturationEvent {
    private Patient patient;
    private double saturation;
    private java.util.Date timestamp;
    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    public double getSaturation() {
        return saturation;
    }
    public void setSaturation(double saturation) {
        this.saturation = saturation;
    }
    public java.util.Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(java.util.Date timestamp) {
        this.timestamp = timestamp;
    }
    public OxygenSaturationEvent() {
    }
    public OxygenSaturationEvent(Patient patient, double saturation, Date timestamp) {
        this.patient = patient;
        this.saturation = saturation;
        this.timestamp = timestamp;
    }
}
