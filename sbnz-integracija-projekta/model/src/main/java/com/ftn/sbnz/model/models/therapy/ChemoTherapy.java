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
public class ChemoTherapy extends Therapy {
    private String protocol;
    private String medicine;
    private double dose;
    private int durationInDays;
    private String adverseEffectsDescription;
    
}
