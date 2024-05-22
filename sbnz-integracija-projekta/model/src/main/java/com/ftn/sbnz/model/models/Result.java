package com.ftn.sbnz.model.models;

import java.util.ArrayList;
import java.util.List;

import com.ftn.sbnz.model.models.users.Patient;

public class Result {
    private String value;
    private List<String> facts = new ArrayList<String>();

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public List<String> getFacts() {
        return facts;
    }
    public void setFacts(List<String> facts) {
        this.facts = facts;
    }
    public void addFact(String fact) {
        this.facts.add(fact);
    }
    public Result() {
    }
    public Result(String value, List<String> facts) {
        this.value = value;
        this.facts = facts;
    }

    // Metod za dodavanje činjenica kao stringova na osnovu Patient objekata
    public void addFactFromPatients(Patient child, Patient parent) {
        this.facts.add(child.getIme() + " IS CHILD OF " + parent.getIme());
    }
}
