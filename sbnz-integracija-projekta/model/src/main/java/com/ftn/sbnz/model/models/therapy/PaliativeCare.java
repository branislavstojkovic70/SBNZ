package com.ftn.sbnz.model.models.therapy;

import java.time.LocalDateTime;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper = true)
public class PaliativeCare extends Therapy {
    private String description;
    private String applicationMethod;
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getApplicationMethod() {
        return applicationMethod;
    }
    public void setApplicationMethod(String applicationMethod) {
        this.applicationMethod = applicationMethod;
    }
    public PaliativeCare(Integer id, LocalDateTime strDateTime, LocalDateTime enDateTime, String description,
            TherapyType therapyType, TherapyState therapyState, String description2, String applicationMethod) {
        super(id, strDateTime, enDateTime, description, therapyType, therapyState);
        this.description = description2;
        this.applicationMethod = applicationMethod;
    }
    public PaliativeCare() {
        super();
    }


}
