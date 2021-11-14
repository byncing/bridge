package eu.byncing.bridge.plugin.spigot.handles.player;

import eu.byncing.bridge.driver.event.player.PlayerNetConnectEvent;
import eu.byncing.bridge.driver.event.player.PlayerNetDisconnectEvent;
import eu.byncing.bridge.driver.player.IBridgePlayer;
import eu.byncing.bridge.driver.protocol.IPacketHandler;
import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerNetConnect;
import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerNetDisconnect;
import eu.byncing.bridge.driver.service.IBridgeService;
import eu.byncing.bridge.plugin.spigot.BridgeClient;
import eu.byncing.bridge.plugin.spigot.impl.BridgePlayer;
import eu.byncing.net.api.channel.IChannel;
import eu.byncing.net.api.protocol.Packet;

public class PlayerNetConnectionHandler implements IPacketHandler<Packet> {

    private final BridgeClient client;

    public PlayerNetConnectionHandler(BridgeClient client) {
        this.client = client;
    }

    @Override
    public void handle(IChannel channel, Packet packet) {
        if (packet instanceof PacketPlayerNetConnect) {
            PacketPlayerNetConnect connect = (PacketPlayerNetConnect) packet;
            IBridgeService service = client.getServices().getService(connect.getService());
            BridgePlayer player = new BridgePlayer(connect.getUniqueId(), connect.getName(), service);
            service.getPlayers().add(player);
            player.setService(service);
            client.sendMessage("Player " + player.getName() + " has connected.");
            client.getPlayers().getPlayers().add(player);
            client.getEvents().call(new PlayerNetConnectEvent(player, service));
        }
        if (packet instanceof PacketPlayerNetDisconnect) {
            PacketPlayerNetDisconnect disconnect = (PacketPlayerNetDisconnect) packet;
            IBridgePlayer player = client.getPlayers().getPlayer(disconnect.getUniqueId());
            client.sendMessage("Player " + player.getName() + " has disconnected.");
            client.getPlayers().getPlayers().remove(player);
            if (client.isConnected()) client.getEvents().call(new PlayerNetDisconnectEvent(player));
            client.getEvents().call(new PlayerNetDisconnectEvent(player));
        }
    }

    @Override
    public Class<? extends Packet>[] getClasses() {
        return new Class[]{PacketPlayerNetConnect.class, PacketPlayerNetDisconnect.class};
    }
}