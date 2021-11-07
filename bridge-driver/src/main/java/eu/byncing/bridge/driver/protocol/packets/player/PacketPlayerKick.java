package eu.byncing.bridge.driver.protocol.packets.player;

import eu.byncing.net.api.protocol.IPacketBuffer;
import eu.byncing.net.api.protocol.Packet;

import java.util.UUID;

public class PacketPlayerKick extends Packet {

    private UUID uniqueId;

    private String reason;

    public PacketPlayerKick() {
        super();
    }

    public PacketPlayerKick(UUID uniqueId, String reason) {
        this.uniqueId = uniqueId;
        this.reason = reason;
    }

    @Override
    public void write(IPacketBuffer buffer) {
        buffer.write("uniqueId", uniqueId);
        buffer.write("reason", reason);
    }

    @Override
    public void read(IPacketBuffer buffer) {
        uniqueId = buffer.read("uniqueId", UUID.class);
        reason = buffer.read("reason", String.class);
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getReason() {
        return reason;
    }
}