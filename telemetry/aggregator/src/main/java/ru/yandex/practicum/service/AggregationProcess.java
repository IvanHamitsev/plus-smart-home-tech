package ru.yandex.practicum.service;

import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Optional;

public interface AggregationProcess {
    public Optional<SensorsSnapshotAvro> pushSensorEvent(SensorEventAvro measure);
}
