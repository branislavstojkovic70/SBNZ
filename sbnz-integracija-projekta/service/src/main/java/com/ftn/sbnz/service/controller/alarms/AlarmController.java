package com.ftn.sbnz.service.controller.alarms;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.ftn.sbnz.model.models.alarms.Alarm;
import com.ftn.sbnz.service.service.SimulationService;
import com.ftn.sbnz.service.service.alarms.AlarmService;

@Controller
@RequestMapping("/api/alarms")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;
    @Autowired
    private SimulationService simulationService;

    @PreAuthorize("hasRole('ROLE_Patient')")
    @GetMapping("/{id}")
    public ResponseEntity<Alarm> getAlarmById(HttpServletRequest request, @PathVariable Integer id) {
        Alarm alarm = this.alarmService.getAlarmById(id);
        if (alarm != null) {
            if (alarm.getPatient().getId() == getCurrentUserId(request)) {
                return ResponseEntity.ok(alarm);
            } else {
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PreAuthorize("hasRole('ROLE_Patient')")
    @GetMapping("/{patientId}/alarms-for-patient")
    public ResponseEntity<List<Alarm>> getAlarms(HttpServletRequest request, @PathVariable Integer patientId) {
        if (patientId == getCurrentUserId(request)) {
            List<Alarm> alarms = this.alarmService.getAlarmsByPatientId(patientId);
            if (alarms != null) {
                return ResponseEntity.ok(alarms);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
    }

    @PreAuthorize("hasRole('ROLE_Patient')")
    @PutMapping("/simulate-parent-has-cancer")
    public ResponseEntity<?> simulateAlarmForParent(HttpServletRequest request) {
        try {
            Integer childId = getCurrentUserId(request);
            this.simulationService.simulateAlarmForParentServiceMethod(childId);
            return ResponseEntity.ok("Simulation completed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during the simulation: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_Patient')")
    @PutMapping("/simulate-hypoxia")
    public ResponseEntity<?> simulateHypoxia(HttpServletRequest request) {
        try {
            Integer userId = getCurrentUserId(request);
            this.simulationService.testTemplateHypoxia(userId);

            return ResponseEntity.ok("Simulation completed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during the simulation: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_Patient')")
    @PutMapping("/simulate-symptom-aggravation")
    public ResponseEntity<?> simulateSymptomAggravation(HttpServletRequest request) {
        try {
            Integer userId = getCurrentUserId(request);
            this.simulationService.testTemplateSymptomAggravation(userId);

            return ResponseEntity.ok("Simulation completed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during the simulation: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_Patient')")
    @PutMapping("/simulate-symptom-aggravation-after-operation")
    public ResponseEntity<?> simulateForward2(HttpServletRequest request) {
        try {
            Integer userId = getCurrentUserId(request);
            this.simulationService.testForward2(userId);

            return ResponseEntity.ok("Simulation completed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during the simulation: " + e.getMessage());
        }
    }

    public Integer getCurrentUserId(HttpServletRequest request) {
        return (Integer) request.getAttribute("userId");
    }

}