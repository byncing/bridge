package eu.byncing.bridge.driver.event.player;

import eu.byncing.bridge.driver.event.IEvent;
import eu.byncing.bridge.driver.player.IBridgePlayer;

public class PlayerUpdateEvent implements IEvent {

    private final IBridgePlayer player;

    public PlayerUpdateEvent(IBridgePlayer player) {
        this.player = player;
    }

    public IBridgePlayer getPlayer() {
        return player;
    }
}