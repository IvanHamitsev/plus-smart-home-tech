package commerce.shopping_store.service;

import commerce.shopping_store.kafka.telemetry.event.HubEventAvro;

public interface HubEventProcess {
    public void pushHubEvent(HubEventAvro event);
}
