package com.ftn.sbnz.service.controller.therapy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.ftn.sbnz.model.models.therapy.Therapy;
import com.ftn.sbnz.service.service.therapy.TherapyService;

@Controller
@RequestMapping("/api/therapies")
public class TherapyController {

    @Autowired
    private TherapyService therapyService;

    @PreAuthorize("hasAnyRole('ROLE_Patient', 'ROLE_Doctor')")
    @GetMapping("/{id}")
    public ResponseEntity<Therapy> getTherapyById(@PathVariable Integer id) {
        Therapy therapy = therapyService.getTherapyById(id);
        if (therapy != null) {
            return ResponseEntity.ok(therapy);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
