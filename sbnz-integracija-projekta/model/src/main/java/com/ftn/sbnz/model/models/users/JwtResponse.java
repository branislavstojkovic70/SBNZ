package com.ftn.sbnz.model.models.users;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String accessToken;

    private String refreshToken;
    private int expiresIn;
    private final String type = "Bearer";
    private Integer id;
    private String email;
    private String details;
    private List<String> roles;

}
