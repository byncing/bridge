package eu.byncing.bridge.plugin.bungee;

import eu.byncing.bridge.driver.event.service.ServiceLogoutEvent;
import eu.byncing.bridge.driver.protocol.packets.service.PacketServiceLogout;
import eu.byncing.bridge.driver.service.IBridgeService;
import eu.byncing.net.api.channel.ChannelHandler;
import eu.byncing.net.api.channel.IChannel;
import eu.byncing.net.api.protocol.Packet;

public class ServerHandler extends ChannelHandler {

    private final BridgeServer server;

    public ServerHandler(BridgeServer server) {
        this.server = server;
    }

    @Override
    public void handleConnected(IChannel channel) {
        server.sendMessage("Channel" + channel.getRemoteAddress() + " has connected.");
    }

    @Override
    public void handlePacket(IChannel channel, Packet packet) {
        server.getPackets().handle(channel, packet);
    }

    @Override
    public void handleDisconnected(IChannel channel) {
        IBridgeService service = server.getServices().getService(channel);
        if (service == null) {
            server.sendMessage("Channel" + channel.getRemoteAddress() + " has disconnected.");
            return;
        }
        server.getServices().getServices().remove(service);
        server.sendPacket(new PacketServiceLogout(service.getName(), service.getMotd(), service.getOnlineCount(), service.getMaxCount()));
        server.sendMessage("Channel" + channel.getRemoteAddress() + " Service " + service.getName() + " has logout.");
        server.getEvents().call(new ServiceLogoutEvent(service));
    }

    @Override
    public void handleException(Exception exception) {
    }
}