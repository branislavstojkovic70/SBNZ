package com.ftn.sbnz.service.repository.examination;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ftn.sbnz.model.models.examinations.Symptom;

public interface SymptomRepository extends JpaRepository<Symptom, Integer>{

    @Transactional
    Optional<Symptom> findByName(String name);

}
