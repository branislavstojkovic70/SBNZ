package com.ftn.sbnz.service.controller.users;

import java.util.ArrayList;
import java.util.List;

import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.ftn.sbnz.model.models.users.Administrator;
import com.ftn.sbnz.model.models.users.Doctor;
import com.ftn.sbnz.model.models.users.Patient;
import com.ftn.sbnz.service.service.users.AdminService;
import com.ftn.sbnz.service.service.users.DoctorService;
import com.ftn.sbnz.service.service.DroolsService;

@Controller
@RequestMapping("/api/admin")
public class AdministratorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private DroolsService droolsService;

    @Autowired
    private AdminService adminService;

    @PostMapping
    public ResponseEntity<Administrator> createAdmin(@RequestBody Administrator admin) {
        Administrator administrator = adminService.save(admin);
        return ResponseEntity.ok(administrator);
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    @PostMapping("/new-doctor")
    public ResponseEntity<Doctor> addDoctor(@RequestBody Doctor doctor) {
        Doctor savedDoctor = doctorService.addDoctor(doctor);
        return ResponseEntity.ok(savedDoctor);
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }
    
    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("/high-risk-patients")
    public ResponseEntity<List<Patient>> getAllPatients() {
        QueryResults queryResults = droolsService.getAllHighRiskedPatients();
        ArrayList<Patient> highRiskPatients = new ArrayList<>();

        for (QueryResultsRow row : queryResults) {
            Patient patient = (Patient) row.get("$patient");
            highRiskPatients.add(patient);
        }
        return ResponseEntity.ok(highRiskPatients);
    }
}
