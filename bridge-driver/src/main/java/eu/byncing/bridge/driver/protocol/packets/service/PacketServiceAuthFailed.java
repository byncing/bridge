package eu.byncing.bridge.driver.protocol.packets.service;

import eu.byncing.net.api.protocol.IPacketBuffer;
import eu.byncing.net.api.protocol.Packet;

public class PacketServiceAuthFailed extends Packet {

    private String reason;

    public PacketServiceAuthFailed() {
        super();
    }

    public PacketServiceAuthFailed(String reason) {
        this.reason = reason;
    }

    @Override
    public void write(IPacketBuffer buffer) {
        buffer.write("reason", reason);
    }

    @Override
    public void read(IPacketBuffer buffer) {
        reason = buffer.read("reason", String.class);
    }

    public String getReason() {
        return reason;
    }
}