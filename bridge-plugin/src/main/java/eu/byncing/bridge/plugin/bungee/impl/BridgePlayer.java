package eu.byncing.bridge.plugin.bungee.impl;

import eu.byncing.bridge.driver.BridgeDriver;
import eu.byncing.bridge.driver.player.IBridgePlayer;
import eu.byncing.bridge.driver.player.PlayerAddress;
import eu.byncing.bridge.driver.protocol.packets.player.*;
import eu.byncing.bridge.driver.service.IBridgeService;
import eu.byncing.net.api.protocol.Packet;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.InetSocketAddress;
import java.util.UUID;

public class BridgePlayer implements IBridgePlayer {

    private final UUID uniqueId;
    private String name;

    private final PlayerAddress address;

    private int ping;

    private IBridgeService service;

    public BridgePlayer(UUID uniqueId, String name, PlayerAddress address, IBridgeService service) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.address = address;
        this.service = service;
    }

    @Override
    public void sendPacket(Packet packet) {
        if (service == null) return;
        service.sendPacket(packet);
    }

    @Override
    public void sendMessage(String message) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uniqueId);
        if (player == null) return;
        player.sendMessage(new TextComponent(message));

        sendPacket(new PacketPlayerMessage(uniqueId, message));
    }

    @Override
    public void kick(String reason) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uniqueId);
        if (player == null) return;
        player.disconnect(new TextComponent(reason));

        sendPacket(new PacketPlayerKick(uniqueId, reason));
    }

    @Override
    public void connect(IBridgeService service) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uniqueId);
        ServerInfo info = ProxyServer.getInstance().getServerInfo(service.getName());
        if (info == null) return;
        player.connect(info);
        sendPacket(new PacketPlayerServiceChange(uniqueId, service.getName()));
    }

    @Override
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uniqueId);
        if (player == null) return;
        Title packet = ProxyServer.getInstance().createTitle();
        packet.title(new TextComponent(title));
        packet.subTitle(new TextComponent(subtitle));
        packet.fadeIn(fadeIn);
        packet.stay(stay);
        packet.fadeOut(fadeOut);
        packet.send(player);

        sendPacket(new PacketPlayerTitle(uniqueId, title, subtitle, fadeIn, stay, fadeOut));
    }

    @Override
    public boolean hasPermission(String permission) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uniqueId);
        if (player == null) return true;
        return !player.hasPermission(permission);
    }

    public void update(PacketPlayerUpdate update) {
        name = update.getName();
        service = BridgeDriver.getInstance().getServiceManager().getService(update.getService());
        ping = update.getPing();
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
    public PlayerAddress getAddress() {
        return address;
    }

    @Override
    public int getPing() {
        return ping;
    }

    @Override
    public IBridgeService getService() {
        return service;
    }

    public void setService(IBridgeService service) {
        this.service = service;
    }
}