package com.ftn.sbnz.service.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ftn.sbnz.model.models.users.Administrator;

public interface AdminRepository extends JpaRepository<Administrator, Integer>{
    
}
