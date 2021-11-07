package eu.byncing.bridge.plugin;

import eu.byncing.bridge.driver.BridgeUtil;
import eu.byncing.bridge.driver.player.IBridgePlayer;
import eu.byncing.bridge.driver.protocol.packets.service.PacketServiceUpdate;
import eu.byncing.bridge.driver.service.IBridgeService;
import eu.byncing.net.api.channel.IChannel;
import eu.byncing.net.api.protocol.Packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BridgeService implements IBridgeService {

    private final IChannel channel;

    private final UUID uniqueId;

    private final String name;

    private String motd;

    private final List<IBridgePlayer> players = new ArrayList<>();

    private int onlineCount, maxCount;

    public BridgeService(IChannel channel, UUID uniqueId, String name, String motd, int onlineCount, int maxCount) {
        this.channel = channel;
        this.uniqueId = uniqueId;
        this.name = name;
        this.motd = BridgeUtil.builder(motd).replace("§", "&").buildIndex(0);
        this.onlineCount = onlineCount;
        this.maxCount = maxCount;
    }

    public BridgeService(IChannel channel, UUID uniqueId, String name, String motd, int maxCount) {
        this(channel, uniqueId, name, motd, 0, maxCount);
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }

    @Override
    public void sendPacket(Packet packet) {
        channel.sendPacket(packet);
    }

    public void update(PacketServiceUpdate update) {
        motd = BridgeUtil.builder(update.getMotd()).replace("§", "&").buildIndex(0);
        onlineCount = update.getOnlineCount();
        maxCount = update.getMaxCount();
    }

    @Override
    public IChannel getChannel() {
        return channel;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getMotd() {
        return motd;
    }

    @Override
    public List<IBridgePlayer> getPlayers() {
        return players;
    }

    @Override
    public int getOnlineCount() {
        return onlineCount;
    }

    @Override
    public int getMaxCount() {
        return maxCount;
    }
}