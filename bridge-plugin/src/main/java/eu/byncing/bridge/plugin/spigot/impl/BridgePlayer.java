package eu.byncing.bridge.plugin.spigot.impl;

import eu.byncing.bridge.driver.BridgeDriver;
import eu.byncing.bridge.driver.player.IBridgePlayer;
import eu.byncing.bridge.driver.protocol.packets.player.*;
import eu.byncing.bridge.driver.service.IBridgeService;
import eu.byncing.net.api.protocol.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BridgePlayer implements IBridgePlayer {

    private final UUID uniqueId;
    private String name;

    private IBridgeService service;

    public BridgePlayer(UUID uniqueId, String name, IBridgeService service) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.service = service;
    }

    @Override
    public void sendPacket(Packet packet) {
        if (service == null) return;
        service.sendPacket(packet);
    }

    @Override
    public void sendMessage(String message) {
        sendPacket(new PacketPlayerMessage(uniqueId, message));
    }

    @Override
    public void kick(String reason) {
        sendPacket(new PacketPlayerKick(uniqueId, reason));
    }

    @Override
    public void connect(IBridgeService service) {
        sendPacket(new PacketPlayerServiceChange(uniqueId, service.getName()));
    }

    @Override
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        sendPacket(new PacketPlayerTitle(uniqueId, title, subtitle, fadeIn, stay, fadeOut));
    }

    @Override
    public boolean hasPermission(String permission) {
        Player player = Bukkit.getPlayer(uniqueId);
        if (player == null) return false;
        return player.hasPermission(permission);
    }

    public void update(PacketPlayerUpdate update) {
        name = update.getName();
        service = BridgeDriver.getInstance().getServiceManager().getService(update.getService());
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public IBridgeService getService() {
        return service;
    }

    public void setService(IBridgeService service) {
        this.service = service;
    }
}