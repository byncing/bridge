package eu.byncing.bridge.examples.bungee;

import eu.byncing.bridge.driver.BridgeDriver;
import eu.byncing.bridge.driver.event.BridgeHandler;
import eu.byncing.bridge.driver.event.IBridgeListener;
import eu.byncing.bridge.driver.event.service.ServiceLoginEvent;
import eu.byncing.bridge.driver.event.service.ServiceLogoutEvent;

public class BungeeListener implements IBridgeListener {

    @BridgeHandler
    public void handle(ServiceLoginEvent event) {
        BridgeDriver.getInstance().getPlayerManager().getPlayers().forEach(player -> {
            player.sendMessage("§7[§a+§7] §a§o" + event.getService().getName());
        });
    }

    @BridgeHandler
    public void handle(ServiceLogoutEvent event) {
        BridgeDriver.getInstance().getPlayerManager().getPlayers().forEach(player -> {
            player.sendMessage("§7[§c-§7] §c§o" + event.getService().getName());
        });
    }
}