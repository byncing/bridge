package eu.byncing.bridge.examples.spigot;

import eu.byncing.bridge.driver.event.BridgeHandler;
import eu.byncing.bridge.driver.event.IBridgeListener;
import eu.byncing.bridge.driver.event.player.PlayerConnectEvent;
import eu.byncing.bridge.driver.player.IBridgePlayer;
import eu.byncing.bridge.driver.service.IBridgeService;

public class SpigotListener implements IBridgeListener {

    @BridgeHandler(internal = true)
    public void handle(PlayerConnectEvent event) {
        IBridgePlayer player = event.getPlayer();
        IBridgeService service = event.getService();

        player.sendMessage(player.getService().getName());
        player.sendMessage(service.getName());
    }
}