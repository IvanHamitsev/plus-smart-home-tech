package ru.yandex.practicum.model;

import java.util.Objects;

public enum ConditionOperationType {
    EQUALS {
        @Override
        public boolean perform(Integer constant, Integer variable) {
            return Objects.equals(constant, variable);
        }
    },
	LOWER_THAN {
        @Override
        public boolean perform(Integer constant, Integer variable) {
            return constant > variable;
        }
    },
    GREATER_THAN {
        @Override
        public boolean perform(Integer constant, Integer variable) {
            return constant < variable;
        }
    };
    public abstract boolean perform(Integer constant, Integer variable);
}
