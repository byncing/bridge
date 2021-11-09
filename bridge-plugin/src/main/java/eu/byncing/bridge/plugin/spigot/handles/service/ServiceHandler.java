package eu.byncing.bridge.plugin.spigot.handles.service;

import eu.byncing.bridge.driver.BridgeUtil;
import eu.byncing.bridge.driver.event.service.ServiceUpdateEvent;
import eu.byncing.bridge.driver.protocol.IPacketHandler;
import eu.byncing.bridge.driver.protocol.packets.service.PacketServiceAuthFailed;
import eu.byncing.bridge.driver.protocol.packets.service.PacketServiceUpdate;
import eu.byncing.bridge.plugin.BridgeService;
import eu.byncing.bridge.plugin.spigot.BridgeClient;
import eu.byncing.net.api.channel.IChannel;
import eu.byncing.net.api.protocol.Packet;

public class ServiceHandler implements IPacketHandler<Packet> {

    private final BridgeClient client;

    public ServiceHandler(BridgeClient client) {
        this.client = client;
    }

    @Override
    public void handle(IChannel channel, Packet packet) {
        if (packet instanceof PacketServiceAuthFailed) {
            PacketServiceAuthFailed failed = (PacketServiceAuthFailed) packet;
            client.sendMessage("Failed: " + BridgeUtil.builder(failed.getReason()).replace("§", "&").buildIndex(0));
        }
        if (packet instanceof PacketServiceUpdate) {
            PacketServiceUpdate update = (PacketServiceUpdate) packet;
            BridgeService service;
            if (client.getServices().getService(update.getName()) == null) {
                service = new BridgeService(channel, update.getName(), update.getMotd(), update.getOnlineCount(), update.getMaxCount());
                client.getServices().getServices().add(service);
                client.getEvents().call(new ServiceUpdateEvent(service));
                return;
            }
            service = (BridgeService) client.getServices().getService(update.getName());
            service.update(update);
            client.getEvents().call(new ServiceUpdateEvent(service));
        }
    }

    @Override
    public Class<? extends Packet>[] getClasses() {
        return new Class[]{PacketServiceUpdate.class, PacketServiceAuthFailed.class};
    }
}