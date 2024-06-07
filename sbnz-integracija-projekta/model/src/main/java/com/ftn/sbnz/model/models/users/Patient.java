package com.ftn.sbnz.model.models.users;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.ftn.sbnz.model.models.examinations.Examination;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
public class Patient extends User{
    private double bloodPressure;
    private int puls;
    private double saturationO2;
    private double bodyTemperature;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "pacijent_id")
    private Set<Examination> examinations;
    public double getBloodPressure() {
        return bloodPressure;
    }
    public void setBloodPressure(double bloodPressure) {
        this.bloodPressure = bloodPressure;
    }
    public int getPuls() {
        return puls;
    }
    public void setPuls(int puls) {
        this.puls = puls;
    }
    public double getSaturationO2() {
        return saturationO2;
    }
    public void setSaturationO2(double saturationO2) {
        this.saturationO2 = saturationO2;
    }
    public double getBodyTemperature() {
        return bodyTemperature;
    }
    public void setBodyTemperature(double bodyTemperature) {
        this.bodyTemperature = bodyTemperature;
    }
    public Set<Examination> getExaminations() {
        return examinations;
    }
    public void setExaminations(Set<Examination> examinations) {
        this.examinations = examinations;
    }
    public Patient(Integer id, String ime, String prezime, String email, String password, LocalDateTime dateOfBirth,
            Role role, double bloodPressure, int puls, double saturationO2, double bodyTemperature,
            Set<Examination> examinations) {
        super(id, ime, prezime, email, password, dateOfBirth, role);
        this.bloodPressure = bloodPressure;
        this.puls = puls;
        this.saturationO2 = saturationO2;
        this.bodyTemperature = bodyTemperature;
        this.examinations = examinations;
    }
    public Patient() {
        super();
    }

    
}
