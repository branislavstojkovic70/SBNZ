package com.ftn.sbnz.model.models.examinations;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.ftn.sbnz.model.models.therapy.Therapy;
import com.ftn.sbnz.model.models.users.Doctor;

@Entity
public class Examination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    private String note;

    private ExaminationState examinationState;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ExaminationType> examinationTypes;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Symptom> symptoms;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "diagnosis_id", referencedColumnName = "id")
    private Diagnosis diagnosis;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "therapy_id", referencedColumnName = "id")
    private Therapy therapy;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ExaminationState getExaminationState() {
        return examinationState;
    }

    public void setExaminationState(ExaminationState examinationState) {
        this.examinationState = examinationState;
    }

    public Set<ExaminationType> getExaminationTypes() {
        return examinationTypes;
    }

    public void setExaminationTypes(Set<ExaminationType> examinationTypes) {
        this.examinationTypes = examinationTypes;
    }

    public Set<Symptom> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(Set<Symptom> symptoms) {
        this.symptoms = symptoms;
    }

    public Diagnosis getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Therapy getTherapy() {
        return therapy;
    }

    public void setTherapy(Therapy therapy) {
        this.therapy = therapy;
    }

    public Examination() {
    }

    public Examination(Integer id, LocalDateTime dateTime, Doctor doctor, String note,
            ExaminationState examinationState, Set<ExaminationType> examinationTypes, Set<Symptom> symptoms,
            Diagnosis diagnosis, Therapy therapy) {
        this.id = id;
        this.dateTime = dateTime;
        this.doctor = doctor;
        this.note = note;
        this.examinationState = examinationState;
        this.examinationTypes = examinationTypes;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.therapy = therapy;
    }

    
    
}
