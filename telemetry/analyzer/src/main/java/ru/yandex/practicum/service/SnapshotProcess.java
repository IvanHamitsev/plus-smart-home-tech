package ru.yandex.practicum.service;

import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public interface SnapshotProcess {
    public void pushSnapshot(SensorsSnapshotAvro event,
                             HubRouterControllerGrpc.HubRouterControllerBlockingStub grpcClient);
}
