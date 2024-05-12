package com.ftn.sbnz.model.models.therapy;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class RadioTherapy extends Therapy{
    private String rayType;
    private double medicineDose;
    private String applicationRegion;
}
