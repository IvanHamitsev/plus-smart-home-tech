package commerce.shopping_store.service;

import commerce.shopping_store.grpc.telemetry.event.DeviceActionRequest;
import commerce.shopping_store.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.List;

public interface SnapshotProcess {
    public List<DeviceActionRequest> pushSnapshot(SensorsSnapshotAvro event);
}
