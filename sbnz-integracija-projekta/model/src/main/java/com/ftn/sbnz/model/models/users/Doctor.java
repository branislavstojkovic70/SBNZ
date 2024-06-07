package com.ftn.sbnz.model.models.users;

import java.time.LocalDateTime;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper = true)
public class Doctor extends User {
    private String specialization;

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Doctor() {
        super();
    }
    public Doctor(Integer id, String ime, String prezime, String email, String password, LocalDateTime dateOfBirth,
            Role role, String specialization) {
        super(id, ime, prezime, email, password, dateOfBirth, role);
        this.specialization = specialization;
    }

    
}
