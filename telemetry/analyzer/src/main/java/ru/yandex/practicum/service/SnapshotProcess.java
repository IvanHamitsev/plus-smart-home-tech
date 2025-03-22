package ru.yandex.practicum.service;

import ru.yandex.practicum.grpc.telemetry.event.DeviceActionMessageProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public interface SnapshotProcess {
    public DeviceActionMessageProto pushSnapshot(SensorsSnapshotAvro event);
}
