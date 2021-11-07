package eu.byncing.bridge.driver.protocol.packets.service;

import eu.byncing.net.api.protocol.IPacketBuffer;
import eu.byncing.net.api.protocol.Packet;

public class PacketServiceAuth extends Packet {

    private String key, name, motd;

    private int onlineCount, maxCount;

    public PacketServiceAuth() {
        super();
    }

    public PacketServiceAuth(String key, String name, String motd, int onlineCount, int maxCount) {
        this.key = key;
        this.name = name;
        this.motd = motd;
        this.onlineCount = onlineCount;
        this.maxCount = maxCount;
    }

    @Override
    public void write(IPacketBuffer buffer) {
        buffer.write("key", key);
        buffer.write("name", name);
        buffer.write("motd", motd);
        buffer.write("onlineCount", onlineCount);
        buffer.write("maxCount", maxCount);
    }

    @Override
    public void read(IPacketBuffer buffer) {
        key = buffer.read("key", String.class);
        name = buffer.read("name", String.class);
        motd = buffer.read("motd", String.class);
        onlineCount = buffer.read("onlineCount", Integer.class);
        maxCount = buffer.read("maxCount", Integer.class);
    }

    public String getKey() {
        return key;
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