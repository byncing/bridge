package eu.byncing.bridge.driver.protocol.packets.service;

import eu.byncing.net.api.protocol.IPacketBuffer;
import eu.byncing.net.api.protocol.Packet;

import java.util.UUID;

public class PacketServiceLogin extends Packet {

    private UUID uniqueId;
    private String name, motd;

    private int onlineCount, maxCount;

    public PacketServiceLogin() {
        super();
    }

    public PacketServiceLogin(UUID uniqueId, String name, String motd, int onlineCount, int maxCount) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.motd = motd;
        this.onlineCount = onlineCount;
        this.maxCount = maxCount;
    }

    @Override
    public void write(IPacketBuffer buffer) {
        buffer.write("uniqueId", uniqueId);
        buffer.write("name", name);
        buffer.write("motd", motd);
        buffer.write("onlineCount", onlineCount);
        buffer.write("maxCount", maxCount);
    }

    @Override
    public void read(IPacketBuffer buffer) {
        uniqueId = buffer.read("uniqueId", UUID.class);
        name = buffer.read("name", String.class);
        motd = buffer.read("motd", String.class);
        onlineCount = buffer.read("onlineCount", Integer.class);
        maxCount = buffer.read("maxCount", Integer.class);
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getName() {
        return name;
    }

    public String getMotd() {
        return motd;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public int getMaxCount() {
        return maxCount;
    }
}