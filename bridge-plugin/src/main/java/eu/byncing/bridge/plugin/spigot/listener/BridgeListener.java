package eu.byncing.bridge.plugin.spigot.listener;

import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerConnect;
import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerDisconnect;
import eu.byncing.bridge.plugin.BridgeService;
import eu.byncing.bridge.plugin.spigot.BridgeClient;
import eu.byncing.bridge.plugin.spigot.impl.BridgePlayer;
import eu.byncing.bridge.plugin.spigot.BridgeSpigot;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BridgeListener implements Listener {

    private final BridgeClient client;

    public BridgeListener(BridgeClient client) {
        this.client = client;
    }

    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(BridgeSpigot.getInstance(), () -> {
            BridgeService service = (BridgeService) client.getServices().getService(client.getInternalServiceUUID());
            BridgePlayer player = (BridgePlayer) client.getPlayers().getPlayer(event.getPlayer().getUniqueId());
            if (player == null) return;
            client.sendPacket(new PacketPlayerConnect(player.getUniqueId(), player.getName(), service.getUniqueId()));
        }, 10);
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        BridgeService service = (BridgeService) client.getServices().getService(client.getInternalServiceUUID());
        BridgePlayer player = (BridgePlayer) client.getPlayers().getPlayer(event.getPlayer().getUniqueId());
        if (player == null) return;
        client.sendPacket(new PacketPlayerDisconnect(player.getUniqueId(), player.getName(), service.getUniqueId()));
    }
}