package com.ftn.sbnz.service.controller.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.ftn.sbnz.model.models.examinations.Examination;
import com.ftn.sbnz.model.models.users.Doctor;
import com.ftn.sbnz.model.models.users.Patient;
import com.ftn.sbnz.service.service.users.DoctorService;

@Controller
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PreAuthorize("hasRole('ROLE_Doctor')")
    @GetMapping("/{doctorId}/scheduled-examinations")
    public ResponseEntity<List<Examination>> getScheduledExaminations(@PathVariable Integer doctorId) {
        List<Examination> scheduledExaminations = doctorService.getScheduledExaminationsByDoctorId(doctorId);
        return ResponseEntity.ok(scheduledExaminations);
    }

    @PreAuthorize("hasRole('ROLE_Doctor')")
    @GetMapping("/{doctorId}/completed-examinations")
    public ResponseEntity<List<Examination>> getCompletedExaminations(@PathVariable Integer doctorId) {
        List<Examination> completedExaminations = doctorService.getCompletedExaminationsByDoctorId(doctorId);
        return ResponseEntity.ok(completedExaminations);
    }

    @PreAuthorize("hasRole('ROLE_Doctor')")
    @GetMapping("/{doctorId}/my-patients")
    public ResponseEntity<List<Patient>> getPatientsByDoctorId(@PathVariable Integer doctorId) {
        List<Patient> patients = doctorService.getPatientsByDoctorId(doctorId);
        return ResponseEntity.ok(patients);
    }

    @PreAuthorize("hasAnyRole('ROLE_Doctor', 'ROLE_Admin')")
    @GetMapping("/{doctorId}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Integer doctorId) {
        return doctorService.findDoctorById(doctorId)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
