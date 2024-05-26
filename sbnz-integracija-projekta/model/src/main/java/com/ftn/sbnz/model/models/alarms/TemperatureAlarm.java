package com.ftn.sbnz.model.models.alarms;

import java.util.Date;

import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import com.ftn.sbnz.model.models.users.Patient;

@Role(Role.Type.EVENT)
@Timestamp("timestamp")
public class TemperatureAlarm {
    private Patient patient;
    private double temperature;
    private java.util.Date timestamp;
    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    public double getTemperature() {
        return temperature;
    }
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
    public java.util.Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(java.util.Date timestamp) {
        this.timestamp = timestamp;
    }
    public TemperatureAlarm() {
    }
    public TemperatureAlarm(Patient patient, double temperature, Date timestamp) {
        this.patient = patient;
        this.temperature = temperature;
        this.timestamp = timestamp;
    }

    
}
