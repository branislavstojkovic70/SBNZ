package com.ftn.sbnz.service.repository.examination;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ftn.sbnz.model.models.examinations.Examination;
import com.ftn.sbnz.model.models.examinations.ExaminationState;

@Repository
public interface ExaminationRepository extends JpaRepository<Examination, Integer> {
    List<Examination> findByDoctorIdAndExaminationState(Integer doctorId, ExaminationState state);

}
