package collector.controller;

import collector.dto.hub.HubEventDto;
import collector.dto.sensor.SensorEventDto;
import collector.service.CollectorService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventsController {

    private final CollectorService service;

    @PostMapping("/sensors")
    public void fromSensor(@Valid @RequestBody SensorEventDto measure) {
        service.sendSensor(measure);
    }

    @PostMapping("/hubs")
    public void fromHub(@Valid @RequestBody HubEventDto hubAction) {
        service.sendHub(hubAction);
    }
}
