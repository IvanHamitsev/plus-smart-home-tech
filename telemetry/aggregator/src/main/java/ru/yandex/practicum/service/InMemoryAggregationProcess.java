package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryAggregationProcess implements AggregationProcess {
    Map<String, SensorsSnapshotAvro> storage = new HashMap<>();

    public Optional<SensorsSnapshotAvro> pushSensorEvent(SensorEventAvro measure) {

        if (null == measure) {
            log.info("Aggregator get empty SensorEventAvro");
            return Optional.empty();
        }

        String hubId = measure.getHubId();

        SensorsSnapshotAvro snapshot;
        if (!storage.containsKey(hubId)) {
            log.info("No hubId {} find in storage", hubId);
            snapshot = SensorsSnapshotAvro.newBuilder()
                    .setHubId(hubId)
                    .setTimestamp(Instant.now())
                    .setSensorsState(new HashMap<>())
                    .build();
        } else {
            snapshot = storage.get(hubId);
            log.info("Find snapshot for hubId {} in storage. Map.size = {}", hubId, snapshot.getSensorsState().size());
        }

        var currentSensors = snapshot.getSensorsState();
        var currentStateOfSensor = currentSensors.get(measure.getId());

        // если вообще такой сенсор был
        if (null != currentStateOfSensor) {
            // если полученый замер не свежее старого
            if (measure.getTimestamp().isBefore(currentStateOfSensor.getTimestamp())) {
                log.info("Time in stored Snapshot is newer then received");
                return Optional.empty();
            }
            // если данные в замере идентичны старым данным, не нужно обновлять время замера?
            if (Objects.equals(currentStateOfSensor.getData(), measure.getPayload())) {
                log.info("Data in stored Snapshot is equal to new measure");
                return Optional.empty();
            }
            currentStateOfSensor.setData(measure.getPayload());
        } else {
            currentStateOfSensor = SensorStateAvro.newBuilder()
                    .setTimestamp(measure.getTimestamp())
                    .setData(measure.getPayload())
                    .build();
        }

        currentSensors.put(measure.getId(), currentStateOfSensor);
        snapshot.setSensorsState(currentSensors);
        // Какое время изменения снапшота ставить, текущее или время замера measure.getTimestamp() ?
        snapshot.setTimestamp(Instant.now());

        //в хранилище добавить/обновить снапшот
        storage.put(hubId, snapshot);

        log.info("Ready to send updated snapshot. Hub={}, Map.size={}, payload={} ",
                snapshot.getHubId(),
                snapshot.getSensorsState().size(),
                measure.getPayload()
        );

        return Optional.of(snapshot);
    }

    private boolean specialEquals(Object arg1, Object arg2) {
        if (!arg1.getClass().equals(arg2.getClass())) {
            return false;
        }
        // с помощью механизма рефлексии пройти по всем полям класса arg1
        var filds = arg1.getClass().getDeclaredFields();

        try {
            for (Field field : filds) {
                field.setAccessible(true);
                var f1 = field.get(arg1);
                var f2 = field.get(arg2);
                if (!f1.equals(f2)) {
                    return false;
                }
            }
        } catch (IllegalAccessException e) {
            return false;
        }
        return true;
    }
}
