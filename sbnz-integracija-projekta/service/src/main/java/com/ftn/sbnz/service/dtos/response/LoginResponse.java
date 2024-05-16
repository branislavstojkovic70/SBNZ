package com.ftn.sbnz.service.dtos.response;

public class LoginResponse {
    private String email;
    private String token;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public LoginResponse(String email, String token) {
        this.email = email;
        this.token = token;
    }

    
}
