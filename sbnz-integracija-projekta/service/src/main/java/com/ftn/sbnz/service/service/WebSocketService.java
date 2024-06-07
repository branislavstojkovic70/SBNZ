package com.ftn.sbnz.service.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.ftn.sbnz.model.models.alarms.Alarm;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendAlarm(Alarm alarm) {
        Alarm safeAlarm = new Alarm(alarm.getId(), alarm.getPatient(), HtmlUtils.htmlEscape(alarm.getDescription()), alarm.getTime());
        System.out.println("Oglasio se alarm");
        messagingTemplate.convertAndSend("/topic/alarms/" + alarm.getPatient().getId(), safeAlarm);
    }
}
