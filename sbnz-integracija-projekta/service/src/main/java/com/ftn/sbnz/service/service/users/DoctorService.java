package com.ftn.sbnz.service.service.users;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ftn.sbnz.model.models.examinations.Examination;
import com.ftn.sbnz.model.models.examinations.ExaminationState;
import com.ftn.sbnz.model.models.users.Doctor;
import com.ftn.sbnz.model.models.users.Patient;
import com.ftn.sbnz.service.repository.examination.ExaminationRepository;
import com.ftn.sbnz.service.repository.users.DoctorRepository;
import com.ftn.sbnz.service.repository.users.PatientRepository;

@Service
public class DoctorService {

    @Autowired
    private ExaminationRepository examinationRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public List<Examination> getScheduledExaminationsByDoctorId(Integer doctorId) {
        return examinationRepository.findByDoctorIdAndExaminationState(doctorId, ExaminationState.SCHEDULED);
    }

    public List<Examination> getCompletedExaminationsByDoctorId(Integer doctorId) {
        return examinationRepository.findByDoctorIdAndExaminationState(doctorId, ExaminationState.DONE);
    }

    public List<Patient> getPatientsByDoctorId(Integer doctorId) {
        return patientRepository.findAll().stream()
            .filter(patient -> patient.getExaminations().stream()
                .anyMatch(examination -> examination.getDoctor().getId().equals(doctorId)))
            .collect(Collectors.toList());
    }

    @Transactional
    public Doctor addDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Optional<Doctor> findDoctorById(Integer doctorId) {
        return doctorRepository.findById(doctorId);
    }
}
