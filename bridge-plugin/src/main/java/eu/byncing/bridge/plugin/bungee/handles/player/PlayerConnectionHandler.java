package eu.byncing.bridge.plugin.bungee.handles.player;

import eu.byncing.bridge.driver.event.player.PlayerConnectEvent;
import eu.byncing.bridge.driver.event.player.PlayerDisconnectEvent;
import eu.byncing.bridge.driver.protocol.IPacketHandler;
import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerConnect;
import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerDisconnect;
import eu.byncing.bridge.plugin.BridgeService;
import eu.byncing.bridge.plugin.bungee.BridgeServer;
import eu.byncing.bridge.plugin.bungee.impl.BridgePlayer;
import eu.byncing.net.api.channel.IChannel;
import eu.byncing.net.api.protocol.Packet;

public class PlayerConnectionHandler implements IPacketHandler<Packet> {

    private final BridgeServer server;

    public PlayerConnectionHandler(BridgeServer server) {
        this.server = server;
    }

    @Override
    public void handle(IChannel channel, Packet packet) {
        if (packet instanceof PacketPlayerConnect) {
            PacketPlayerConnect connect = (PacketPlayerConnect) packet;
            BridgePlayer player = (BridgePlayer) server.getPlayers().getPlayer(connect.getUniqueId());
            BridgeService service = (BridgeService) server.getServices().getService(connect.getService());
            service.getPlayers().add(player);
            player.setService(service);
            server.sendPacket(new PacketPlayerConnect(player.getUniqueId(), player.getName(), service.getName()));
            server.sendMessage("Player " + player.getName() + " has connected with the " + service.getName() + " service.");
            server.getEvents().call(new PlayerConnectEvent(player, service));
        }
        if (packet instanceof PacketPlayerDisconnect) {
            PacketPlayerDisconnect disconnect = (PacketPlayerDisconnect) packet;
            BridgePlayer player = (BridgePlayer) server.getPlayers().getPlayer(disconnect.getUniqueId());
            BridgeService service = (BridgeService) server.getServices().getService(disconnect.getService());
            service.getPlayers().remove(player);
            player.setService(service);
            server.sendMessage("Player " + player.getName() + " has disconnected with the " + service.getName() + " service.");
            server.getEvents().call(new PlayerDisconnectEvent(player, service));
            server.sendPacket(new PacketPlayerDisconnect(player.getUniqueId(), player.getName(), service.getName()));
        }
    }

    @Override
    public Class<? extends Packet>[] getClasses() {
        return new Class[]{PacketPlayerConnect.class, PacketPlayerDisconnect.class};
    }
}