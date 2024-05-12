package com.ftn.sbnz.model.models.users;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class Administrator extends User {
    
}
