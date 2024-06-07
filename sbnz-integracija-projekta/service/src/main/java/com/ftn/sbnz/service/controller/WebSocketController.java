package com.ftn.sbnz.service.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.sbnz.model.models.alarms.Alarm;
import com.ftn.sbnz.service.service.WebSocketService;
import com.ftn.sbnz.service.service.users.PatientService;

@RestController
@RequestMapping("/api/ws")
public class WebSocketController {

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private PatientService patientService;

    @MessageMapping("/alarm")
    public void sendAlarm() {
        webSocketService.sendAlarm(new Alarm(2, patientService.findById(3).get(), "description", LocalDateTime.now()));
    }

    @PostMapping("/send-alarm")
    public void sendAlarmViaRest() {
        webSocketService.sendAlarm(new Alarm(2, patientService.findById(3).get(), "description", LocalDateTime.now()));
    }
}
