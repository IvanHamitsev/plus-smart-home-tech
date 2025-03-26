package ru.yandex.practicum.service;

import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.List;

public interface SnapshotProcess {
    public List<DeviceActionRequest> pushSnapshot(SensorsSnapshotAvro event);
}
