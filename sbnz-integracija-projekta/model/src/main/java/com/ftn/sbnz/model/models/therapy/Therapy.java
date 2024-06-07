package com.ftn.sbnz.model.models.therapy;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.TableGenerator;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Therapy {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "therapy_generator")
    @TableGenerator(name = "therapy_generator", table = "id_generator", pkColumnName = "gen_name", valueColumnName = "gen_val", pkColumnValue = "therapy_gen", initialValue = 1000, allocationSize = 50)
    private Integer id;

    private LocalDateTime strDateTime;
    private LocalDateTime enDateTime;
    private String description;
    private TherapyType therapyType;
    private TherapyState therapyState;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public LocalDateTime getStrDateTime() {
        return strDateTime;
    }
    public void setStrDateTime(LocalDateTime strDateTime) {
        this.strDateTime = strDateTime;
    }
    public LocalDateTime getEnDateTime() {
        return enDateTime;
    }
    public void setEnDateTime(LocalDateTime enDateTime) {
        this.enDateTime = enDateTime;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public TherapyType getTherapyType() {
        return therapyType;
    }
    public void setTherapyType(TherapyType therapyType) {
        this.therapyType = therapyType;
    }
    public TherapyState getTherapyState() {
        return therapyState;
    }
    public void setTherapyState(TherapyState therapyState) {
        this.therapyState = therapyState;
    }
    public Therapy() {
    }
    public Therapy(Integer id, LocalDateTime strDateTime, LocalDateTime enDateTime, String description,
            TherapyType therapyType, TherapyState therapyState) {
        this.id = id;
        this.strDateTime = strDateTime;
        this.enDateTime = enDateTime;
        this.description = description;
        this.therapyType = therapyType;
        this.therapyState = therapyState;
    }

    
}
