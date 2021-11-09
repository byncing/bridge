package eu.byncing.bridge.plugin.spigot.handles.service;

import eu.byncing.bridge.driver.event.service.ServiceLoginEvent;
import eu.byncing.bridge.driver.event.service.ServiceLogoutEvent;
import eu.byncing.bridge.driver.protocol.IPacketHandler;
import eu.byncing.bridge.driver.protocol.packets.service.PacketServiceLogin;
import eu.byncing.bridge.driver.protocol.packets.service.PacketServiceLogout;
import eu.byncing.bridge.driver.service.IBridgeService;
import eu.byncing.bridge.plugin.spigot.BridgeClient;
import eu.byncing.net.api.channel.IChannel;
import eu.byncing.net.api.protocol.Packet;

public class ServiceConnectionHandler implements IPacketHandler<Packet> {

    private final BridgeClient client;

    public ServiceConnectionHandler(BridgeClient client) {
        this.client = client;
    }

    @Override
    public void handle(IChannel channel, Packet packet) {
        if (packet instanceof PacketServiceLogin) {
            PacketServiceLogin login = (PacketServiceLogin) packet;
            IBridgeService service = client.getServices().getService(login.getName());
            client.sendMessage("Channel" + channel.getRemoteAddress() + " Service " + service.getName() + " has login.");
            client.getEvents().call(new ServiceLoginEvent(service));
        }
        if (packet instanceof PacketServiceLogout) {
            PacketServiceLogout logout = (PacketServiceLogout) packet;
            IBridgeService service = client.getServices().getService(logout.getName());
            client.sendMessage("Channel" + channel.getRemoteAddress() + " Service " + logout.getName() + " has logout.");
            client.getServices().getServices().remove(service);
            client.getEvents().call(new ServiceLogoutEvent(service));
        }
    }

    @Override
    public Class<? extends Packet>[] getClasses() {
        return new Class[]{PacketServiceLogin.class, PacketServiceLogout.class};
    }
}