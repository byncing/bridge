package eu.byncing.bridge.driver.protocol.packets.player;

import eu.byncing.net.api.protocol.IPacketBuffer;
import eu.byncing.net.api.protocol.Packet;

import java.util.UUID;

public class PacketPlayerUpdate extends Packet {

    private UUID uniqueId;

    private String name;

    private UUID serviceUniqueId;

    public PacketPlayerUpdate() {
        super();
    }

    public PacketPlayerUpdate(UUID uniqueId, String name, UUID serviceUniqueId) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.serviceUniqueId = serviceUniqueId;
    }

    @Override
    public void write(IPacketBuffer buffer) {
        buffer.write("uniqueId", uniqueId);
        buffer.write("name", name);
        buffer.write("serviceUniqueId", serviceUniqueId);
    }

    @Override
    public void read(IPacketBuffer buffer) {
        uniqueId = buffer.read("uniqueId", UUID.class);
        name = buffer.read("name", String.class);
        serviceUniqueId = buffer.read("serviceUniqueId", UUID.class);
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getName() {
        return name;
    }

    public UUID getServiceUniqueId() {
        return serviceUniqueId;
    }
}