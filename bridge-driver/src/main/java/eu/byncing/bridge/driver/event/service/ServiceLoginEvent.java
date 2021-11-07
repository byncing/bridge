package eu.byncing.bridge.driver.event.service;

import eu.byncing.bridge.driver.event.IEvent;
import eu.byncing.bridge.driver.service.IBridgeService;

public class ServiceLoginEvent implements IEvent {

    private final IBridgeService service;

    public ServiceLoginEvent(IBridgeService service) {
        this.service = service;
    }

    public IBridgeService getService() {
        return service;
    }
}