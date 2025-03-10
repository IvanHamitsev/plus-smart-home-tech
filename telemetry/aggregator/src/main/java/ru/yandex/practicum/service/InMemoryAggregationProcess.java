package ru.yandex.practicum.service;

import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryAggregationProcess implements AggregationProcess {
    Map<String, SensorsSnapshotAvro> storage = new HashMap<>();

    public Optional<SensorsSnapshotAvro> pushSensorEvent(String hubId, SensorEventAvro measure) {
        SensorsSnapshotAvro snapshot;
        if (!storage.containsKey(hubId)) {
            snapshot = SensorsSnapshotAvro.newBuilder()
                    .setHubId(hubId)
                    .setTimestamp(Instant.now())
                    .setSensorsState(new HashMap<>())
                    .build();
        } else {
            snapshot = storage.get(hubId);
        }

        var currentSensors = snapshot.getSensorsState();
        var currentStateOfSensor = currentSensors.get(measure.getId());

        // если полученый замер не свежее старого
        if (measure.getTimestamp().isBefore(currentStateOfSensor.getTimestamp())) {
            return Optional.empty();
        }

        // если данные в замере идентичны старым данным, не нужно обновлять время замера?
        // !!! так сравнивать нельзя, надо переопределить equals !!!
        //if(currentStateOfSensor.getData().equals(measure.getPayload())) {
        if(specialEquals(currentStateOfSensor.getData(), measure.getPayload())) {
            return Optional.empty();
        }

        currentStateOfSensor.setData(measure.getPayload());
        currentSensors.put(measure.getId(), currentStateOfSensor);
        snapshot.setSensorsState(currentSensors);
        // Какое время изменения снапшота ставить, текущее или время замера measure.getTimestamp() ?
        snapshot.setTimestamp(Instant.now());

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
                if (field.get(arg1) != field.get(arg2)) {
                    return false;
                }
            }
        } catch (IllegalAccessException e) {
            return false;
        }
        return true;
    }
}
