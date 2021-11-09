package eu.byncing.bridge.driver.protocol.packets.service;

import eu.byncing.net.api.protocol.IPacketBuffer;
import eu.byncing.net.api.protocol.Packet;

public class PacketServiceUpdate extends Packet {

    private String name, motd;

    private int onlineCount, maxCount;

    public PacketServiceUpdate() {
        super();
    }

    public PacketServiceUpdate(String name, String motd, int onlineCount, int maxCount) {
        this.name = name;
        this.motd = motd;
        this.onlineCount = onlineCount;
        this.maxCount = maxCount;
    }

    @Override
    public void write(IPacketBuffer buffer) {
        buffer.write("name", name);
        buffer.write("motd", motd);
        buffer.write("onlineCount", onlineCount);
        buffer.write("maxCount", maxCount);
    }

    @Override
    public void read(IPacketBuffer buffer) {
        name = buffer.read("name", String.class);
        motd = buffer.read("motd", String.class);
        onlineCount = buffer.read("onlineCount", Integer.class);
        maxCount = buffer.read("maxCount", Integer.class);
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