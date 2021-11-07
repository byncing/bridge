package eu.byncing.bridge.driver.event.service;

import eu.byncing.bridge.driver.event.IEvent;
import eu.byncing.bridge.driver.service.IBridgeService;

public class ServiceLogoutEvent implements IEvent {

    private final IBridgeService service;

    public ServiceLogoutEvent(IBridgeService service) {
        this.service = service;
    }

    public IBridgeService getService() {
        return service;
    }
}