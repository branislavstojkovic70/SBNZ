package com.ftn.sbnz.service.service.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.sbnz.model.models.users.Administrator;
import com.ftn.sbnz.service.repository.users.AdminRepository;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
    
    public Administrator save(Administrator admin) {
        Administrator administrator = adminRepository.save(admin);
        return administrator;
    }
}
