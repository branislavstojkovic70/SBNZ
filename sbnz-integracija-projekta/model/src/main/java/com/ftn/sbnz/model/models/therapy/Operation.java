package com.ftn.sbnz.model.models.therapy;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper = true)
public class Operation extends Therapy{
    private String description;
    private LocalDateTime scheduledFor;
    private String outcome;
    private OperationType operationType;
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDateTime getScheduledFor() {
        return scheduledFor;
    }
    public void setScheduledFor(LocalDateTime scheduledFor) {
        this.scheduledFor = scheduledFor;
    }
    public String getOutcome() {
        return outcome;
    }
    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
    public OperationType getOperationType() {
        return operationType;
    }
    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }
    public Operation() {
        super();
    }
    public Operation(Integer id, LocalDateTime strDateTime, LocalDateTime enDateTime, String description,
            TherapyType therapyType, TherapyState therapyState, String description2, LocalDateTime scheduledFor,
            String outcome, OperationType operationType) {
        super(id, strDateTime, enDateTime, description, therapyType, therapyState);
        description = description2;
        this.scheduledFor = scheduledFor;
        this.outcome = outcome;
        this.operationType = operationType;
    }

    
}
