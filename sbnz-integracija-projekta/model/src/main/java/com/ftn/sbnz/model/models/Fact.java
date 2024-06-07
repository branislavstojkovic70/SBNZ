package com.ftn.sbnz.model.models;

import org.kie.api.definition.type.Position;
import com.ftn.sbnz.model.models.users.Patient;

public class Fact {
 
    @Position(0)
    private Patient child;

    @Position(1)
    private Patient parent;

    public Patient getChild() {
        return child;
    }

    public void setChild(Patient child) {
        this.child = child;
    }

    public Patient getParent() {
        return parent;
    }

    public void setParent(Patient parent) {
        this.parent = parent;
    }

    public Fact() {
    }

    public Fact(Patient child, Patient parent) {
        this.child = child;
        this.parent = parent;
    }
}
