package com.ftn.sbnz.service.repository.users;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ftn.sbnz.model.models.users.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer>{
    public Patient findOneById(Integer id);
    Optional<Patient> findOneByEmail(String email);
    public List<Patient> findAll();
}
