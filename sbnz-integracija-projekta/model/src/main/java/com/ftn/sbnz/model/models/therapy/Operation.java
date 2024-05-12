package com.ftn.sbnz.model.models.therapy;

import java.time.LocalDateTime;

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
public class Operation extends Therapy{
    private String description;
    private LocalDateTime scheduledFor;
    private String outcome;
    private OperationType operationType;
}
