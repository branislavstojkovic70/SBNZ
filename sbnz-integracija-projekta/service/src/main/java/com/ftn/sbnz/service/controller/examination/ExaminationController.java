package com.ftn.sbnz.service.controller.examination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.ftn.sbnz.model.models.examinations.Examination;
import com.ftn.sbnz.service.service.examination.ExaminationService;

@Controller
@RequestMapping("/api/examinations")
public class ExaminationController {

    @Autowired
    private ExaminationService examinationService;

    @PreAuthorize("hasAnyRole('ROLE_Patient', 'ROLE_Doctor')")
    @GetMapping("/{id}")
    public ResponseEntity<Examination> getExaminationById(@PathVariable Integer id) {
        Examination examination = examinationService.getExaminationById(id);
        if (examination != null) {
            return ResponseEntity.ok(examination);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PreAuthorize("hasRole('ROLE_Doctor')")
    @PostMapping("/patient/{patientId}")
    public ResponseEntity<Examination> addExaminationForPatient(@PathVariable Integer patientId, @RequestBody Examination examination) {
        Examination createdExamination = examinationService.addExaminationForPatient(patientId, examination);
        if (createdExamination != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdExamination);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PreAuthorize("hasRole('ROLE_Doctor')")
    @PutMapping("/{id}")
    public ResponseEntity<Examination> updateExamination(@PathVariable Integer id, @RequestBody Examination updatedExamination) {
        Examination updatedExaminationResult = examinationService.updateExamination(id, updatedExamination);
        if (updatedExaminationResult != null) {
            return ResponseEntity.ok(updatedExaminationResult);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
