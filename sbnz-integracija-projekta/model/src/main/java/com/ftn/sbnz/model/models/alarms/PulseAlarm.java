package com.ftn.sbnz.model.models.alarms;
import java.util.Date;

import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import com.ftn.sbnz.model.models.users.Patient;

@Role(Role.Type.EVENT)
@Timestamp("timestamp")
public class PulseAlarm {
    private Patient patient;
    private double pulse;
    private java.util.Date timestamp;
    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    public double getPulse() {
        return pulse;
    }
    public void setPulse(double pulse) {
        this.pulse = pulse;
    }
    public java.util.Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(java.util.Date timestamp) {
        this.timestamp = timestamp;
    }
    public PulseAlarm() {
    }
    public PulseAlarm(Patient patient, double pulse, Date timestamp) {
        this.patient = patient;
        this.pulse = pulse;
        this.timestamp = timestamp;
    }

    
}
