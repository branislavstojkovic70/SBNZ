package com.ftn.sbnz.service.service.examination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.sbnz.model.models.examinations.Diagnosis;
import com.ftn.sbnz.service.repository.examination.DiagnosisRepository;

@Service
public class DiagnosisService {

    @Autowired
    private DiagnosisRepository diagnosisRepository;

    public Diagnosis getDiagnosisById(Integer id) {
        return diagnosisRepository.findById(id).orElse(null);
    }
}