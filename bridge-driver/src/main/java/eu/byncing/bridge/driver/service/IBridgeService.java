package eu.byncing.bridge.driver.service;

import eu.byncing.bridge.driver.player.IBridgePlayer;
import eu.byncing.net.api.channel.IChannel;
import eu.byncing.net.api.protocol.IPacketSender;
import eu.byncing.net.api.protocol.Packet;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface IBridgeService extends Closeable, IPacketSender {

    @Override
    void close() throws IOException;

    @Override
    void sendPacket(Packet packet);

    IChannel getChannel();

    UUID getUniqueId();

    String getName();

    String getMotd();

    List<IBridgePlayer> getPlayers();

    int getOnlineCount();

    int getMaxCount();
}