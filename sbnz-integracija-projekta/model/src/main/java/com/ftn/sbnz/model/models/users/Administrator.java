package com.ftn.sbnz.model.models.users;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper=true)
public class Administrator extends User {
    public Administrator(){
        super();
    }
}
