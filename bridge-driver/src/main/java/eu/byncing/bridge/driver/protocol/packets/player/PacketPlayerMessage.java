package eu.byncing.bridge.driver.protocol.packets.player;

import eu.byncing.net.api.protocol.IPacketBuffer;
import eu.byncing.net.api.protocol.Packet;

import java.util.UUID;

public class PacketPlayerMessage extends Packet {

    private UUID uniqueId;

    private String message;

    public PacketPlayerMessage() {
        super();
    }

    public PacketPlayerMessage(UUID uniqueId, String message) {
        this.uniqueId = uniqueId;
        this.message = message;
    }

    @Override
    public void write(IPacketBuffer buffer) {
        buffer.write("uniqueId", uniqueId);
        buffer.write("message", message);
    }

    @Override
    public void read(IPacketBuffer buffer) {
        uniqueId = buffer.read("uniqueId", UUID.class);
        message = buffer.read("message", String.class);
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getMessage() {
        return message;
    }
}