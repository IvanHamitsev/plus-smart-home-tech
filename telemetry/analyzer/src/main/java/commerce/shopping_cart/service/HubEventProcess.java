package commerce.shopping_cart.service;

import commerce.shopping_cart.kafka.telemetry.event.HubEventAvro;

public interface HubEventProcess {
    public void pushHubEvent(HubEventAvro event);
}
