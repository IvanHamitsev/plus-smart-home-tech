package collector.controller;


import collector.service.CollectorService;
import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import commerce.shopping_cart.grpc.telemetry.collector.CollectorControllerGrpc;
import commerce.shopping_cart.grpc.telemetry.event.HubEventProto;
import commerce.shopping_cart.grpc.telemetry.event.SensorEventProto;

@GrpcService
@RequiredArgsConstructor
public class GrpcEventsController extends CollectorControllerGrpc.CollectorControllerImplBase {

    private final CollectorService service;

    @Override
    public void collectSensorEvent(SensorEventProto inpSensorMessage, StreamObserver<com.google.protobuf.Empty> response) {
        try {
            service.sendSensor(inpSensorMessage);
            // вернуть пустой ответ
            response.onNext(Empty.getDefaultInstance());
            response.onCompleted();
        } catch (RuntimeException e) {
            response.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
            ));
        }
    }

    @Override
    public void collectHubEvent(HubEventProto inpHubMessage, StreamObserver<com.google.protobuf.Empty> response) {
        try {
            service.sendHub(inpHubMessage);
            response.onNext(Empty.getDefaultInstance());
            response.onCompleted();
        } catch (RuntimeException e) {
            response.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
            ));
        }
    }
}
