package com.ftn.sbnz.service.repository.therapy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ftn.sbnz.model.models.therapy.Therapy;

@Repository
public interface TherapyRepository extends JpaRepository<Therapy, Integer>{

}
