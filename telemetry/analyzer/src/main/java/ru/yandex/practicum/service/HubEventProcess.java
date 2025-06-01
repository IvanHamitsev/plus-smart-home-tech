package ru.yandex.practicum.service;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubEventProcess {
    public void pushHubEvent(HubEventAvro event);
}
