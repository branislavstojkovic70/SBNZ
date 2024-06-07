package com.ftn.sbnz.model.models.therapy;
import java.time.LocalDateTime;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper = true)
public class ChemoTherapy extends Therapy {
    private String protocol;
    private String medicine;
    private double dose;
    private int durationInDays;
    private String adverseEffectsDescription;
    public String getProtocol() {
        return protocol;
    }
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    public String getMedicine() {
        return medicine;
    }
    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }
    public double getDose() {
        return dose;
    }
    public void setDose(double dose) {
        this.dose = dose;
    }
    public int getDurationInDays() {
        return durationInDays;
    }
    public void setDurationInDays(int durationInDays) {
        this.durationInDays = durationInDays;
    }
    public String getAdverseEffectsDescription() {
        return adverseEffectsDescription;
    }
    public void setAdverseEffectsDescription(String adverseEffectsDescription) {
        this.adverseEffectsDescription = adverseEffectsDescription;
    }
    public ChemoTherapy(Integer id, LocalDateTime strDateTime, LocalDateTime enDateTime, String description,
            TherapyType therapyType, TherapyState therapyState, String protocol, String medicine, double dose,
            int durationInDays, String adverseEffectsDescription) {
        super(id, strDateTime, enDateTime, description, therapyType, therapyState);
        this.protocol = protocol;
        this.medicine = medicine;
        this.dose = dose;
        this.durationInDays = durationInDays;
        this.adverseEffectsDescription = adverseEffectsDescription;
    }
    public ChemoTherapy() {
        super();
    }
    
    
}
