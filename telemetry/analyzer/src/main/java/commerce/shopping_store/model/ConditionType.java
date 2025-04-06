package commerce.shopping_store.model;

import commerce.shopping_store.kafka.telemetry.event.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum ConditionType {
    MOTION {
        @Override
        public List<Integer> cast(SensorStateAvro state) {
            if (state == null) return new ArrayList<>();
            if (state.getData() instanceof MotionSensorAvro sensor) {
                return Collections.singletonList(sensor.getMotion() ? 1 : 0);
            } else {
                throw new IllegalArgumentException("Class is not a MotionSensorAvro " + state.getClass());
            }
        }
    },
    LUMINOSITY {
        @Override
        public List<Integer> cast(SensorStateAvro state) {
            if (state == null) return new ArrayList<>();
            if (state.getData() instanceof LightSensorAvro sensor) {
                return Collections.singletonList(sensor.getLuminosity());
            } else {
                throw new IllegalArgumentException("Class is not a  LightSensorAvro " + state.getClass());
            }
        }
    },
    SWITCH {
        @Override
        public List<Integer> cast(SensorStateAvro state) {
            if (state == null) return new ArrayList<>();
            if (state.getData() instanceof SwitchSensorAvro sensor) {
                return Collections.singletonList(sensor.getState() ? 1 : 0);
            } else {
                throw new IllegalArgumentException("Class is not a  SwitchSensorAvro " + state.getClass());
            }
        }
    },
    TEMPERATURE {
        @Override
        public List<Integer> cast(SensorStateAvro state) {
            if (state == null) return new ArrayList<>();
            if (state.getData() instanceof TemperatureSensorAvro sensor) {
                return List.of(sensor.getTemperatureC(), sensor.getTemperatureF());
            }
            if (state.getData() instanceof ClimateSensorAvro sensor) {
                return Collections.singletonList(sensor.getTemperatureC());
            } else {
                throw new IllegalArgumentException("Class is not a TemperatureSensorAvro or ClimateSensorAvro " + state.getClass());
            }
        }
    },
    CO2LEVEL {
        @Override
        public List<Integer> cast(SensorStateAvro state) {
            if (state == null) return new ArrayList<>();
            if (state.getData() instanceof ClimateSensorAvro sensor) {
                return Collections.singletonList(sensor.getCo2Level());
            } else {
                throw new IllegalArgumentException("Class is not a ClimateSensorAvro " + state.getClass());
            }
        }
    },
    HUMIDITY {
        @Override
        public List<Integer> cast(SensorStateAvro state) {
            if (state == null) return new ArrayList<>();
            if (state.getData() instanceof ClimateSensorAvro sensor) {
                return Collections.singletonList(sensor.getHumidity());
            } else {
                throw new IllegalArgumentException("Class is not a ClimateSensorAvro " + state.getClass());
            }
        }
    };

    public abstract List<Integer> cast(SensorStateAvro state);
}
