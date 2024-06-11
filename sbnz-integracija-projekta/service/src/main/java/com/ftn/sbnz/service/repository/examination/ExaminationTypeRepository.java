package com.ftn.sbnz.service.repository.examination;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ftn.sbnz.model.models.examinations.ExaminationType;

public interface ExaminationTypeRepository extends JpaRepository<ExaminationType, Integer>{
    
}
