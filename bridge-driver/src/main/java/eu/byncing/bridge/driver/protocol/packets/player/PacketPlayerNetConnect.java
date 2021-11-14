package eu.byncing.bridge.driver.protocol.packets.player;

import eu.byncing.bridge.driver.player.PlayerAddress;
import eu.byncing.net.api.protocol.IPacketBuffer;
import eu.byncing.net.api.protocol.Packet;

import java.util.UUID;

public class PacketPlayerNetConnect extends Packet {

    private UUID uniqueId;

    private String name, service;

    private PlayerAddress address;

    public PacketPlayerNetConnect() {
        super();
    }

    public PacketPlayerNetConnect(UUID uniqueId, String name, String service, PlayerAddress address) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.service = service;
        this.address = address;
    }

    @Override
    public void write(IPacketBuffer buffer) {
        buffer.write("uniqueId", uniqueId);
        buffer.write("name", name);
        buffer.write("service", service);
        buffer.write("address", address);
    }

    @Override
    public void read(IPacketBuffer buffer) {
        uniqueId = buffer.read("uniqueId", UUID.class);
        name = buffer.read("name", String.class);
        service = buffer.read("service", String.class);
        address = buffer.read("address", PlayerAddress.class);
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

    public PlayerAddress getAddress() {
        return address;
    }
}