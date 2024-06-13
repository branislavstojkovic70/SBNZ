package com.ftn.sbnz.service.repository.users;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ftn.sbnz.model.models.users.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer>{
    public Patient findOneById(Integer id);
    Optional<Patient> findOneByEmail(String email);
    public List<Patient> findAll();
    @Query("SELECT p FROM Patient p JOIN p.examinations e WHERE e.id = :examinationId")
    Patient findPatientByExaminationId(@Param("examinationId") Integer examinationId);
    @Query("SELECT p FROM Patient p JOIN p.examinations e JOIN e.therapy t WHERE t.therapyType = com.ftn.sbnz.model.models.therapy.TherapyType.OPERATION")
    List<Patient> findAllPatientsWithOperationTherapy();
}
