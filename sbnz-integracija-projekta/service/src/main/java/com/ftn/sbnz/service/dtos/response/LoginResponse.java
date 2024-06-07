package com.ftn.sbnz.service.dtos.response;

public class LoginResponse {
    private String email;
    private Integer id;
    private String role;
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
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public LoginResponse(String email, Integer id, String role, String token) {
        this.email = email;
        this.id = id;
        this.role = role;
        this.token = token;
    }
    

    
}
