package eu.byncing.bridge.plugin.bungee.handles;

import eu.byncing.bridge.driver.event.service.ServiceLoginEvent;
import eu.byncing.bridge.driver.event.service.ServiceUpdateEvent;
import eu.byncing.bridge.driver.protocol.IPacketHandler;
import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerUpdate;
import eu.byncing.bridge.driver.protocol.packets.service.PacketServiceAuth;
import eu.byncing.bridge.driver.protocol.packets.service.PacketServiceAuthState;
import eu.byncing.bridge.driver.protocol.packets.service.PacketServiceLogin;
import eu.byncing.bridge.driver.protocol.packets.service.PacketServiceUpdate;
import eu.byncing.bridge.driver.service.IBridgeService;
import eu.byncing.bridge.plugin.bungee.BridgeServer;
import eu.byncing.bridge.plugin.BridgeService;
import eu.byncing.net.api.channel.IChannel;
import eu.byncing.net.api.protocol.Packet;

import java.io.IOException;
import java.util.UUID;

public class ServiceHandler implements IPacketHandler<Packet> {

    private final BridgeServer server;

    public ServiceHandler(BridgeServer server) {
        this.server = server;
    }

    @Override
    public void handle(IChannel channel, Packet packet) {
        if (packet instanceof PacketServiceAuth) {
            try {
                PacketServiceAuth auth = (PacketServiceAuth) packet;
                if (auth.getKey().equals(server.getConfig().getKey())) {
                    IBridgeService service = new BridgeService(channel, UUID.randomUUID(), auth.getName(), auth.getMotd(), auth.getMaxCount());
                    server.sendMessage("Channel" + channel.getRemoteAddress() + " Service " + service.getName() + " has login.");
                    server.getServices().getServices().add(service);
                    server.getEvents().call(new ServiceLoginEvent(service));
                    channel.sendPacket(new PacketServiceAuthState(PacketServiceAuthState.State.ACCEPT, service.getUniqueId(), service.getName(), service.getMotd(), service.getOnlineCount(), service.getMaxCount()));
                    server.getServices().getServices().forEach(service1 -> server.sendPacket(new PacketServiceUpdate(service1.getUniqueId(), service1.getName(), service1.getMotd(), service1.getOnlineCount(), service1.getMaxCount())));
                    server.getPlayers().getPlayers().forEach(player -> {
                        if (player.getService() != null) {
                            server.sendPacket(new PacketPlayerUpdate(player.getUniqueId(), player.getName(), player.getService().getUniqueId()));
                        }
                    });
                    server.sendPacket(new PacketServiceLogin(service.getUniqueId(), service.getName(), service.getMotd(), service.getOnlineCount(), service.getMaxCount()));

                } else channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (packet instanceof PacketServiceUpdate) {
            PacketServiceUpdate update = (PacketServiceUpdate) packet;
            BridgeService service = (BridgeService) server.getServices().getService(update.getUniqueId());
            service.update(update);
            server.getEvents().call(new ServiceUpdateEvent(service));
            server.sendPacket(update);
        }
    }

    @Override
    public Class<? extends Packet>[] getClasses() {
        return new Class[]{PacketServiceAuth.class, PacketServiceUpdate.class};
    }
}