package com.ftn.sbnz.model.models.examinations;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class ExaminationType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<TestResult> testResults = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<TestResult> getTestResults() {
        return testResults;
    }

    public void setTestResults(Set<TestResult> testResults) {
        this.testResults = testResults;
    }

    public ExaminationType() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExaminationType(Integer id, String name, Set<TestResult> testResults) {
        this.id = id;
        this.name = name;
        this.testResults = testResults;
    }

    

    
}
