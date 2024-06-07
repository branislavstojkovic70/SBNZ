package com.ftn.sbnz.service.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ftn.sbnz.model.models.users.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Integer>{

    
} 
