package com.ftn.sbnz.service.service.alarms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.sbnz.model.models.alarms.Alarm;
import com.ftn.sbnz.service.repository.alarms.AlarmRepository;
@Service
public class AlarmService {
    @Autowired
    private AlarmRepository alarmRepository;

    public List<Alarm> getAlarmsByPatientId(Integer patientId) {
        return alarmRepository.findByPatientId(patientId);
    }

    public Alarm getAlarmById(Integer id) {
        return alarmRepository.findById(id).orElse(null);
    }
}
