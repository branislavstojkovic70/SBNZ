package com.ftn.sbnz.model.models.examinations;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Symptom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;
    private int intensity;
    private SymptomFrequency symptomFrequency;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getIntensity() {
        return intensity;
    }
    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }
    public SymptomFrequency getSymptomFrequency() {
        return symptomFrequency;
    }
    public void setSymptomFrequency(SymptomFrequency symptomFrequency) {
        this.symptomFrequency = symptomFrequency;
    }
    public Symptom() {
    }
    public Symptom(Integer id, String name, String description, int intensity, SymptomFrequency symptomFrequency) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.intensity = intensity;
        this.symptomFrequency = symptomFrequency;
    }

    
    
}
