package ru.yandex.practicum.service;

import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public interface SnapshotProcess {
    public DeviceActionRequest pushSnapshot(SensorsSnapshotAvro event);
}
