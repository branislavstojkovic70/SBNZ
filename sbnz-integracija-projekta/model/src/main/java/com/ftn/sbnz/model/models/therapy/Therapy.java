package com.ftn.sbnz.model.models.therapy;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.TableGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
