package eu.byncing.bridge.driver.event.player;

import eu.byncing.bridge.driver.event.IEvent;
import eu.byncing.bridge.driver.player.IBridgePlayer;

public class PlayerNetDisconnectEvent implements IEvent {

    private final IBridgePlayer player;

    public PlayerNetDisconnectEvent(IBridgePlayer player) {
        this.player = player;
    }

    public IBridgePlayer getPlayer() {
        return player;
    }
}