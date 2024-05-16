package com.ftn.sbnz.model.models.examinations;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TNMKlassification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private double tKlassification;
    private double nKlassification;
    private double mKlassification;
    private LocalDateTime date;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public double gettKlassification() {
        return tKlassification;
    }
    public void settKlassification(double tKlassification) {
        this.tKlassification = tKlassification;
    }
    public double getnKlassification() {
        return nKlassification;
    }
    public void setnKlassification(double nKlassification) {
        this.nKlassification = nKlassification;
    }
    public double getmKlassification() {
        return mKlassification;
    }
    public void setmKlassification(double mKlassification) {
        this.mKlassification = mKlassification;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public TNMKlassification() {
    }
    public TNMKlassification(Integer id, double tKlassification, double nKlassification, double mKlassification,
            LocalDateTime date) {
        this.id = id;
        this.tKlassification = tKlassification;
        this.nKlassification = nKlassification;
        this.mKlassification = mKlassification;
        this.date = date;
    }

    
}
