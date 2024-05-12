package com.ftn.sbnz.service.dtos.request;

import java.time.LocalDateTime;

import lombok.Data;
@Data
public class RegistrationRequest {
    private String ime;
    private String prezime;
    private String email;
    private String password;
    private LocalDateTime dateOfBirth;
    private String role;
}
