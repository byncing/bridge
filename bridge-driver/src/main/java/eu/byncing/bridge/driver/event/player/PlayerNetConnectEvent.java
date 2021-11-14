package eu.byncing.bridge.driver.event.player;

import eu.byncing.bridge.driver.event.IEvent;
import eu.byncing.bridge.driver.player.IBridgePlayer;
import eu.byncing.bridge.driver.service.IBridgeService;

public class PlayerNetConnectEvent implements IEvent {

    private final IBridgePlayer player;
    private final IBridgeService service;

    public PlayerNetConnectEvent(IBridgePlayer player, IBridgeService service) {
        this.player = player;
        this.service = service;
    }

    public IBridgePlayer getPlayer() {
        return player;
    }

    public IBridgeService getService() {
        return service;
    }
}