package eu.byncing.bridge.plugin.spigot.handles.player;

import eu.byncing.bridge.driver.event.player.PlayerConnectEvent;
import eu.byncing.bridge.driver.event.player.PlayerDisconnectEvent;
import eu.byncing.bridge.driver.protocol.IPacketHandler;
import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerConnect;
import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerDisconnect;
import eu.byncing.bridge.plugin.BridgeService;
import eu.byncing.bridge.plugin.spigot.BridgeClient;
import eu.byncing.bridge.plugin.spigot.impl.BridgePlayer;
import eu.byncing.net.api.channel.IChannel;
import eu.byncing.net.api.protocol.Packet;

public class PlayerConnectionHandler implements IPacketHandler<Packet> {

    private final BridgeClient client;

    public PlayerConnectionHandler(BridgeClient client) {
        this.client = client;
    }

    @Override
    public void handle(IChannel channel, Packet packet) {
        if (packet instanceof PacketPlayerConnect) {
            PacketPlayerConnect connect = (PacketPlayerConnect) packet;
            BridgeService service = (BridgeService) client.getServices().getService(connect.getServiceUniqueId());
            BridgePlayer player = (BridgePlayer) client.getPlayers().getPlayer(connect.getUniqueId());
            service.getPlayers().add(player);
            player.setService(service);
            client.sendMessage("Player " + player.getName() + " has connected with the " + service.getName() + " service.");
            client.getEvents().call(new PlayerConnectEvent(player, player.getService()));
        }
        if (packet instanceof PacketPlayerDisconnect) {
            PacketPlayerDisconnect disconnect = (PacketPlayerDisconnect) packet;
            BridgeService service = (BridgeService) client.getServices().getService(disconnect.getServiceUniqueId());
            BridgePlayer player = (BridgePlayer) client.getPlayers().getPlayer(disconnect.getUniqueId());
            service.getPlayers().remove(player);
            player.setService(service);
            client.sendMessage("Player " + player.getName() + " has disconnected with the " + service.getName() + " service.");
            client.getEvents().call(new PlayerDisconnectEvent(player, player.getService()));
        }
    }

    @Override
    public Class<? extends Packet>[] getClasses() {
        return new Class[]{PacketPlayerConnect.class, PacketPlayerDisconnect.class};
    }
}