package commerce.shopping_cart.service;

import commerce.shopping_cart.grpc.telemetry.event.DeviceActionRequest;
import commerce.shopping_cart.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.List;

public interface SnapshotProcess {
    public List<DeviceActionRequest> pushSnapshot(SensorsSnapshotAvro event);
}
