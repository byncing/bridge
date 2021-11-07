package eu.byncing.bridge.plugin.bungee.handles.player;

import eu.byncing.bridge.driver.BridgeUtil;
import eu.byncing.bridge.driver.event.player.PlayerKickEvent;
import eu.byncing.bridge.driver.event.player.PlayerUpdateEvent;
import eu.byncing.bridge.driver.player.IBridgePlayer;
import eu.byncing.bridge.driver.protocol.IPacketHandler;
import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerKick;
import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerMessage;
import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerUpdate;
import eu.byncing.bridge.plugin.bungee.BridgeServer;
import eu.byncing.bridge.plugin.bungee.impl.BridgePlayer;
import eu.byncing.net.api.channel.IChannel;
import eu.byncing.net.api.protocol.Packet;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerHandler implements IPacketHandler<Packet> {

    private final BridgeServer server;

    public PlayerHandler(BridgeServer server) {
        this.server = server;
    }

    @Override
    public void handle(IChannel channel, Packet packet) {
        if (packet instanceof PacketPlayerUpdate) {
            PacketPlayerUpdate update = (PacketPlayerUpdate) packet;
            BridgePlayer player = (BridgePlayer) server.getPlayers().getPlayer(update.getUniqueId());
            player.update(update);
            server.getEvents().call(new PlayerUpdateEvent(player));
            server.sendPacket(update);
        }
        if (packet instanceof PacketPlayerMessage) {
            PacketPlayerMessage message = (PacketPlayerMessage) packet;
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(message.getUniqueId());
            if (player == null) return;
            String string = BridgeUtil.builder(message.getMessage()).replace("§", "&").replace(" ", "_").buildIndex(0);
            player.sendMessage(new TextComponent(string));
        }
        if (packet instanceof PacketPlayerKick) {
            PacketPlayerKick kick = (PacketPlayerKick) packet;
            IBridgePlayer bridgePlayer = server.getPlayers().getPlayer(kick.getUniqueId());
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(kick.getUniqueId());
            if (player == null) return;
            String string = BridgeUtil.builder(kick.getReason()).replace("§", "&").replace(" ", "_").buildIndex(0);
            player.disconnect(new TextComponent(string));
            server.getEvents().call(new PlayerKickEvent(bridgePlayer));
        }
    }

    @Override
    public Class<? extends Packet>[] getClasses() {
        return new Class[]{PacketPlayerUpdate.class, PacketPlayerMessage.class, PacketPlayerKick.class};
    }
}