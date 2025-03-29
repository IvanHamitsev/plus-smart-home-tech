CREATE TABLE IF NOT EXISTS sensor (
    id VARCHAR PRIMARY KEY,
    hub_id VARCHAR,
	UNIQUE(id, hub_id)
);

CREATE TABLE IF NOT EXISTS scenario (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    hub_id VARCHAR,
    name VARCHAR,
    UNIQUE(hub_id, name)
);

CREATE TABLE IF NOT EXISTS condition (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    type VARCHAR,
    operation VARCHAR,
    condition_value INTEGER,
	sensor_id VARCHAR REFERENCES sensor(id)
);

CREATE TABLE IF NOT EXISTS action (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    type VARCHAR,
    action_value INTEGER,
	sensor_id VARCHAR REFERENCES sensor(id)
);

CREATE TABLE IF NOT EXISTS scenario_condition (
    scenario_id BIGINT REFERENCES scenario(id),
    condition_id BIGINT REFERENCES condition(id),
    PRIMARY KEY (scenario_id, condition_id)
);

CREATE TABLE IF NOT EXISTS scenario_action (
    scenario_id BIGINT REFERENCES scenario(id),
    action_id BIGINT REFERENCES action(id),
    PRIMARY KEY (scenario_id, action_id)
);
