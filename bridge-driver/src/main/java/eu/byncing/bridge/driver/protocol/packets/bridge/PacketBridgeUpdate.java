package eu.byncing.bridge.driver.protocol.packets.bridge;

import eu.byncing.net.api.protocol.IPacketBuffer;
import eu.byncing.net.api.protocol.Packet;

public class PacketBridgeUpdate extends Packet {

    private String name, motd;

    private boolean maintenance;

    private int onlineCount, maxCount;

    public PacketBridgeUpdate() {
        super();
    }

    public PacketBridgeUpdate(String name, String motd, boolean maintenance, int onlineCount, int maxCount) {
        this.name = name;
        this.motd = motd;
        this.maintenance = maintenance;
        this.onlineCount = onlineCount;
        this.maxCount = maxCount;
    }

    @Override
    public void write(IPacketBuffer buffer) {
        buffer.write("name", name);
        buffer.write("motd", motd);
        buffer.write("maintenance", maintenance);
        buffer.write("onlineCount", onlineCount);
        buffer.write("maxCount", maxCount);
    }

    @Override
    public void read(IPacketBuffer buffer) {
        name = buffer.read("name", String.class);
        motd = buffer.read("motd", String.class);
        maintenance = buffer.read("maintenance", Boolean.class);
        onlineCount = buffer.read("onlineCount", Integer.class);
        maxCount = buffer.read("maxCount", Integer.class);
    }

    public String getName() {
        return name;
    }

    public String getMotd() {
        return motd;
    }

    public boolean isMaintenance() {
        return maintenance;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public int getMaxCount() {
        return maxCount;
    }
}