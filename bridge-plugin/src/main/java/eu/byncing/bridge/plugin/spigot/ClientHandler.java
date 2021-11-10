package eu.byncing.bridge.plugin.spigot;

import eu.byncing.bridge.driver.protocol.packets.service.PacketServiceAuth;
import eu.byncing.bridge.plugin.spigot.config.BridgeConfig;
import eu.byncing.net.api.channel.ChannelHandler;
import eu.byncing.net.api.channel.IChannel;
import eu.byncing.net.api.protocol.Packet;
import org.bukkit.Server;

public class ClientHandler extends ChannelHandler {

    private final BridgeClient client;

    public ClientHandler(BridgeClient client) {
        this.client = client;
    }

    @Override
    public void handleConnected(IChannel channel) {
        client.sendMessage("Channel" + channel.getRemoteAddress() + " has connected.");
        BridgeConfig config = client.getConfig();
        Server server = client.getServer();
        channel.sendPacket(new PacketServiceAuth(config.getKey(), config.getName(), server.getMotd(), server.getOnlinePlayers().size(), server.getMaxPlayers()));
    }

    @Override
    public void handlePacket(IChannel channel, Packet packet) {
        client.getPackets().handle(channel, packet);
    }

    @Override
    public void handleDisconnected(IChannel channel) {
        client.sendMessage("Channel" + channel.getRemoteAddress() + " has disconnected.");
    }

    @Override
    public void handleException(Exception exception) {
    }
}