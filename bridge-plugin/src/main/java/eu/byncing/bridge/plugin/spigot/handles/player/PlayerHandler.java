package eu.byncing.bridge.plugin.spigot.handles.player;

import eu.byncing.bridge.driver.event.player.PlayerKickEvent;
import eu.byncing.bridge.driver.event.player.PlayerTitleEvent;
import eu.byncing.bridge.driver.event.player.PlayerUpdateEvent;
import eu.byncing.bridge.driver.player.IBridgePlayer;
import eu.byncing.bridge.driver.protocol.IPacketHandler;
import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerKick;
import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerMessage;
import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerTitle;
import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerUpdate;
import eu.byncing.bridge.driver.service.IBridgeService;
import eu.byncing.bridge.plugin.spigot.BridgeClient;
import eu.byncing.bridge.plugin.spigot.BridgeSpigot;
import eu.byncing.bridge.plugin.spigot.impl.BridgePlayer;
import eu.byncing.net.api.channel.IChannel;
import eu.byncing.net.api.protocol.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerHandler implements IPacketHandler<Packet> {

    private final BridgeClient client;

    public PlayerHandler(BridgeClient client) {
        this.client = client;
    }

    @Override
    public void handle(IChannel channel, Packet packet) {
        if (packet instanceof PacketPlayerUpdate) {
            PacketPlayerUpdate update = (PacketPlayerUpdate) packet;
            IBridgeService service = client.getServices().getService(update.getService());
            BridgePlayer player;
            if (client.getPlayers().getPlayer(update.getUniqueId()) == null) {
                player = new BridgePlayer(update.getUniqueId(), update.getName(), service);
                client.getPlayers().getPlayers().add(player);
                service.getPlayers().add(player);
                client.getEvents().call(new PlayerUpdateEvent(player));
                return;
            }
            player = (BridgePlayer) client.getPlayers().getPlayer(update.getUniqueId());
            player.update(update);
            client.getEvents().call(new PlayerUpdateEvent(player));
        }
        if (packet instanceof PacketPlayerMessage) {
            PacketPlayerMessage message = (PacketPlayerMessage) packet;
            Player player = Bukkit.getPlayer(message.getUniqueId());
            if (player == null) return;
            player.sendMessage(message.getMessage());
        }
        if (packet instanceof PacketPlayerKick) {
            PacketPlayerKick kick = (PacketPlayerKick) packet;
            IBridgePlayer bridgePlayer = client.getPlayers().getPlayer(kick.getUniqueId());
            Player player = Bukkit.getPlayer(kick.getUniqueId());
            if (player == null) return;
            Bukkit.getScheduler().runTask(BridgeSpigot.getInstance(), () -> player.kickPlayer(kick.getReason()));
            client.getEvents().call(new PlayerKickEvent(bridgePlayer));
        }
        if (packet instanceof PacketPlayerTitle) {
            PacketPlayerTitle title = (PacketPlayerTitle) packet;
            IBridgePlayer player = client.getPlayers().getPlayer(title.getUniqueId());
            if (player == null) return;
            client.getEvents().call(new PlayerTitleEvent(player, title.getTitle(), title.getSubtitle(), title.getFadeIn(), title.getStay(), title.getFadeOut()));
        }
    }

    @Override
    public Class<? extends Packet>[] getClasses() {
        return new Class[]{PacketPlayerUpdate.class, PacketPlayerMessage.class, PacketPlayerKick.class, PacketPlayerTitle.class};
    }
}