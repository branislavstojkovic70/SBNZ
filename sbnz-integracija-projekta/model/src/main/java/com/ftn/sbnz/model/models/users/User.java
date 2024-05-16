package com.ftn.sbnz.model.models.users;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String ime;
    private String prezime;
    private String email;  
    @Lob
    private String password;
    private LocalDateTime dateOfBirth;
    private Role role;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getIme() {
        return ime;
    }
    public void setIme(String ime) {
        this.ime = ime;
    }
    public String getPrezime() {
        return prezime;
    }
    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
    public User() {
    }
    public User(Integer id, String ime, String prezime, String email, String password, LocalDateTime dateOfBirth,
            Role role) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
    }
    
}
