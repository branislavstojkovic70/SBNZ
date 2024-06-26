package com.ftn.sbnz.model.models.examinations;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String measureUnit;
    private double value;
    private String description;
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
    public String getMeasureUnit() {
        return measureUnit;
    }
    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }
    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public TestResult() {
    }
    public TestResult(Integer id, String name, String measureUnit, double value, String description) {
        this.id = id;
        this.name = name;
        this.measureUnit = measureUnit;
        this.value = value;
        this.description = description;
    }

    
}

