package com.ftn.sbnz.service.service.therapy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.sbnz.model.models.therapy.Therapy;
import com.ftn.sbnz.service.repository.therapy.TherapyRepository;

@Service
public class TherapyService {

    @Autowired
    private TherapyRepository therapyRepository;

    public Therapy getTherapyById(Integer id) {
        return therapyRepository.findById(id).orElse(null);
    }
}
