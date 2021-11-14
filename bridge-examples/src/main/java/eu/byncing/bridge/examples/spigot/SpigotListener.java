package eu.byncing.bridge.examples.spigot;

import eu.byncing.bridge.driver.event.BridgeHandler;
import eu.byncing.bridge.driver.event.IBridgeListener;
import eu.byncing.bridge.driver.event.player.PlayerConnectEvent;
import eu.byncing.bridge.driver.player.IBridgePlayer;

public class SpigotListener implements IBridgeListener {

    @BridgeHandler(internal = true, async = true)
    public void handle(PlayerConnectEvent event) {
        IBridgePlayer player = event.getPlayer();
        player.sendTitle("§7Hello, §a" + player.getName(), "", 10, 40, 10);
    }
}