package com.ftn.sbnz.service.controller.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.ftn.sbnz.model.models.examinations.Diagnosis;
import com.ftn.sbnz.model.models.examinations.Examination;
import com.ftn.sbnz.model.models.therapy.Therapy;
import com.ftn.sbnz.model.models.users.Patient;
import com.ftn.sbnz.service.service.users.PatientService;

@Controller
@RequestMapping("/api/patients")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientService.save(patient);
        return ResponseEntity.ok(savedPatient);
    }

    @PreAuthorize("hasRole('ROLE_Doctor')")
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.findAll();
        return ResponseEntity.ok(patients);
    }

    @PreAuthorize("hasAnyRole('ROLE_Patient', 'ROLE_Doctor', 'ROLE_Admin')")
    @GetMapping("/{patientId}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Integer patientId) {
        Patient patient = patientService.findById(patientId).get();
        return ResponseEntity.ok(patient);
    }

    @PreAuthorize("hasRole('ROLE_Patient')")
    @GetMapping("/{patientId}/examinations/scheduled")
    public ResponseEntity<List<Examination>> getScheduledExaminations(@PathVariable Integer patientId) {
        List<Examination> scheduledExaminations = patientService.getScheduledExaminationsByPatientId(patientId);
        if (scheduledExaminations != null) {
            return ResponseEntity.ok(scheduledExaminations);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_Patient')")
    @GetMapping("/{patientId}/examinations/completed")
    public ResponseEntity<List<Examination>> getCompletedExaminations(@PathVariable Integer patientId) {
        List<Examination> completedExaminations = patientService.getCompletedExaminationsByPatientId(patientId);
        if (completedExaminations != null) {
            return ResponseEntity.ok(completedExaminations);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_Patient')")
    @GetMapping("/{patientId}/diagnoses")
    public ResponseEntity<List<Diagnosis>> getDiagnoses(@PathVariable Integer patientId) {
        List<Diagnosis> diagnoses = patientService.getDiagnosesByPatientId(patientId);
        if (diagnoses != null) {
            return ResponseEntity.ok(diagnoses);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_Patient')")
    @GetMapping("/{patientId}/therapies")
    public ResponseEntity<List<Therapy>> getTherapies(@PathVariable Integer patientId) {
        List<Therapy> therapies = patientService.getTherapiesByPatientId(patientId);
        if (therapies != null) {
            return ResponseEntity.ok(therapies);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
