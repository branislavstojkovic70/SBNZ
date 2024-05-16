package com.ftn.sbnz.model.models.therapy;

import java.time.LocalDateTime;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper = true)
public class RadioTherapy extends Therapy{
    private String rayType;
    private double medicineDose;
    private String applicationRegion;
    public String getRayType() {
        return rayType;
    }
    public void setRayType(String rayType) {
        this.rayType = rayType;
    }
    public double getMedicineDose() {
        return medicineDose;
    }
    public void setMedicineDose(double medicineDose) {
        this.medicineDose = medicineDose;
    }
    public String getApplicationRegion() {
        return applicationRegion;
    }
    public void setApplicationRegion(String applicationRegion) {
        this.applicationRegion = applicationRegion;
    }
    public RadioTherapy(Integer id, LocalDateTime strDateTime, LocalDateTime enDateTime, String description,
            TherapyType therapyType, TherapyState therapyState, String rayType, double medicineDose,
            String applicationRegion) {
        super(id, strDateTime, enDateTime, description, therapyType, therapyState);
        this.rayType = rayType;
        this.medicineDose = medicineDose;
        this.applicationRegion = applicationRegion;
    }
    public RadioTherapy() {
        super();
    }
    
}
