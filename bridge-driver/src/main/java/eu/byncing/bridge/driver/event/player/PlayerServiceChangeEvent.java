package eu.byncing.bridge.driver.event.player;

import eu.byncing.bridge.driver.event.IEvent;
import eu.byncing.bridge.driver.player.IBridgePlayer;
import eu.byncing.bridge.driver.service.IBridgeService;

public class PlayerServiceChangeEvent implements IEvent {

    private final IBridgeService service;
    private final IBridgePlayer player;

    public PlayerServiceChangeEvent(IBridgeService service, IBridgePlayer player) {
        this.service = service;
        this.player = player;
    }

    public IBridgeService getService() {
        return service;
    }

    public IBridgePlayer getPlayer() {
        return player;
    }
}