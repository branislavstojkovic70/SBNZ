package com.ftn.sbnz.service.controller.examination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.ftn.sbnz.model.models.examinations.Diagnosis;
import com.ftn.sbnz.service.service.examination.DiagnosisService;
@Controller
@RequestMapping("/api/diagnoses")
public class DiagnosisController {

    @Autowired
    private DiagnosisService diagnosisService;

    @PreAuthorize("hasAnyRole('ROLE_Patient', 'ROLE_Doctor')")
    @GetMapping("/{id}")
    public ResponseEntity<Diagnosis> getDiagnosisById(@PathVariable Integer id) {
        Diagnosis diagnosis = diagnosisService.getDiagnosisById(id);
        if (diagnosis != null) {
            return ResponseEntity.ok(diagnosis);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
