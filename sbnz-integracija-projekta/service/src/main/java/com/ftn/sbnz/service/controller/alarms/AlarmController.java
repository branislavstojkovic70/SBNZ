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
import com.ftn.sbnz.service.service.alarms.AlarmService;

@Controller
@RequestMapping("/api/alarms")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @PreAuthorize("hasRole('ROLE_Patient')")
    @GetMapping("/{id}")
    public ResponseEntity<Alarm> getAlarmById(HttpServletRequest request, @PathVariable Integer id) {
        Alarm alarm = alarmService.getAlarmById(id);
        if (alarm != null) {
            if (alarm.getPatient().getId() == getCurrentUserId(request)) {
                return ResponseEntity.ok(alarm);
            }
            else{
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
            List<Alarm> alarms = alarmService.getAlarmsByPatientId(patientId);
            if (alarms != null) {
                return ResponseEntity.ok(alarms);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }
        else{
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
    }

    public Integer getCurrentUserId(HttpServletRequest request) {
        return (Integer) request.getAttribute("userId");
    }
    
}