package com.ftn.sbnz.model.models.examinations;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Diagnosis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private boolean isTumorDetected;
    private TumorType tumorType;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "tnm_id", referencedColumnName = "id")
    TNMKlassification tnmKlassification;

    

    public Integer getId() {
        return id;
    }



    public void setId(Integer id) {
        this.id = id;
    }



    public boolean isTumorDetected() {
        return isTumorDetected;
    }



    public void setTumorDetected(boolean isTumorDetected) {
        this.isTumorDetected = isTumorDetected;
    }



    public TumorType getTumorType() {
        return tumorType;
    }



    public void setTumorType(TumorType tumorType) {
        this.tumorType = tumorType;
    }



    public TNMKlassification getTnmKlassification() {
        return tnmKlassification;
    }



    public void setTnmKlassification(TNMKlassification tnmKlassification) {
        this.tnmKlassification = tnmKlassification;
    }


    public Diagnosis() {
    }

    public Diagnosis(Integer id, boolean isTumorDetected, TumorType tumorType, TNMKlassification tnmKlassification) {
        this.id = id;
        this.isTumorDetected = isTumorDetected;
        this.tumorType = tumorType;
        this.tnmKlassification = tnmKlassification;
    }
    
}
