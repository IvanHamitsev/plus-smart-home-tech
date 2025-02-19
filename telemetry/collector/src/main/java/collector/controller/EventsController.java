package collector.controller;

import collector.dto.SensorEvent;
import collector.dto.HubEvent;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventsController {
    @PostMapping("/sensors")
    public void fromSensor(@Valid @RequestBody SensorEvent measure) {

    }

    @PostMapping("/hubs")
    public void fromHub(@Valid @RequestBody HubEvent hubAction) {

    }
}
