package eu.byncing.bridge.driver.protocol.packets.player;

import eu.byncing.net.api.protocol.IPacketBuffer;
import eu.byncing.net.api.protocol.Packet;

import java.util.UUID;

public class PacketPlayerDisconnect extends Packet {

    private UUID uniqueId;

    private String name;

    private String service;

    public PacketPlayerDisconnect() {
        super();
    }

    public PacketPlayerDisconnect(UUID uniqueId, String name, String service) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.service = service;
    }

    @Override
    public void write(IPacketBuffer buffer) {
        buffer.write("uniqueId", uniqueId);
        buffer.write("name", name);
        buffer.write("service", service);
    }

    @Override
    public void read(IPacketBuffer buffer) {
        uniqueId = buffer.read("uniqueId", UUID.class);
        name = buffer.read("name", String.class);
        service = buffer.read("service", String.class);
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getName() {
        return name;
    }

    public String getService() {
        return service;
    }
}