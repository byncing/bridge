package eu.byncing.bridge.driver.protocol.packets.player;

import eu.byncing.net.api.protocol.IPacketBuffer;
import eu.byncing.net.api.protocol.Packet;

import java.util.UUID;

public class PacketPlayerServiceChange extends Packet {

    private UUID uniqueId;

    private String service;

    public PacketPlayerServiceChange() {
        super();
    }

    public PacketPlayerServiceChange(UUID uniqueId, String service) {
        this.uniqueId = uniqueId;
        this.service = service;
    }

    @Override
    public void write(IPacketBuffer buffer) {
        buffer.write("uniqueId", uniqueId);
        buffer.write("service", service);
    }

    @Override
    public void read(IPacketBuffer buffer) {
        uniqueId = buffer.read("uniqueId", UUID.class);
        service = buffer.read("service", String.class);
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getService() {
        return service;
    }
}