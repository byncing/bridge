package eu.byncing.bridge.plugin.bungee.impl;

import eu.byncing.bridge.driver.BridgeDriver;
import eu.byncing.bridge.driver.BridgeUtil;
import eu.byncing.bridge.driver.player.IBridgePlayer;
import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerKick;
import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerMessage;
import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerTitle;
import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerUpdate;
import eu.byncing.bridge.driver.service.IBridgeService;
import eu.byncing.net.api.protocol.Packet;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
        service.sendPacket(packet);
    }

    @Override
    public void sendMessage(String message) {
        if (service == null) return;
        sendPacket(new PacketPlayerMessage(uniqueId, BridgeUtil.builder(message).replace("&", "§", "Â").buildIndex(0)));
    }

    @Override
    public void kick(String reason) {
        sendPacket(new PacketPlayerKick(uniqueId, BridgeUtil.builder(reason).replace("&", "§", "Â").buildIndex(0)));
    }

    @Override
    public void connect(IBridgeService service) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uniqueId);
        ServerInfo info = ProxyServer.getInstance().getServerInfo(service.getName());
        if (info == null) return;
        player.connect(info);
    }

    @Override
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uniqueId);
        if (player == null && service == null) return;
        String[] strings = BridgeUtil.builder(title, subtitle).replace("&", "§", "Â").build();
        Title packet = ProxyServer.getInstance().createTitle();
        packet.title(new TextComponent(title));
        packet.subTitle(new TextComponent(subtitle));
        packet.fadeIn(fadeIn);
        packet.stay(stay);
        packet.fadeOut(fadeOut);
        packet.send(player);

        sendPacket(new PacketPlayerTitle(uniqueId, strings[0], strings[1], fadeIn, stay, fadeOut));
    }

    @Override
    public boolean hasPermission(String permission) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uniqueId);
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