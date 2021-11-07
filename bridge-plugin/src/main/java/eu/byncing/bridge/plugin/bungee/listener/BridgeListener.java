package eu.byncing.bridge.plugin.bungee.listener;

import eu.byncing.bridge.driver.event.player.PlayerNetConnectEvent;
import eu.byncing.bridge.driver.event.player.PlayerNetDisconnectEvent;
import eu.byncing.bridge.driver.player.IBridgePlayer;
import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerNetConnect;
import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerNetDisconnect;
import eu.byncing.bridge.plugin.bungee.BridgeBungee;
import eu.byncing.bridge.plugin.bungee.BridgeServer;
import eu.byncing.bridge.plugin.bungee.config.BridgeConfig;
import eu.byncing.bridge.plugin.bungee.config.BridgeData;
import eu.byncing.bridge.plugin.bungee.impl.BridgePlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

public class BridgeListener implements Listener {

    private final BridgeServer server;

    public BridgeListener(BridgeServer server) {
        this.server = server;
    }

    @EventHandler
    public void handleLogin(PostLoginEvent event) {
        ProxiedPlayer proxiedPlayer = event.getPlayer();
        IBridgePlayer player = new BridgePlayer(proxiedPlayer.getUniqueId(), proxiedPlayer.getName(), null);
        server.sendMessage("Player " + player.getName() + " has connected.");
        server.getPlayers().getPlayers().add(player);
        server.getEvents().call(new PlayerNetConnectEvent(player));
        server.sendPacket(new PacketPlayerNetConnect(player.getUniqueId(), player.getName()));

        int onlineCount = ProxyServer.getInstance().getOnlineCount();
        BridgeData info = server.getConfig().getData();

        if (info.maintenance) {
            if (info.isWhitelist(player.getName())) return;
            if (!proxiedPlayer.hasPermission(info.commandBypass)) {
                proxiedPlayer.disconnect(new TextComponent(server.getConfig().getData().maintenanceMessage));
                return;
            }
        }
        if (onlineCount > info.maxCount) {
            if (!proxiedPlayer.hasPermission(info.connectionBypass)) {
                proxiedPlayer.disconnect(new TextComponent(server.getConfig().getData().fullMessage));
            }
        }
    }

    @EventHandler
    public void handlePing(ProxyPingEvent event) {
        BridgeConfig config = server.getConfig();
        ServerPing response = event.getResponse();
        BridgeConfig.MotdStorage motd = config.getMotdStorage();
        int onlineCount = ProxyServer.getInstance().getOnlineCount();
        int maxCount = config.getData().maxCount;
        if (motd.getName() != null) {
            response.getVersion().setProtocol(999);
            response.getVersion().setName(motd.getName());
        }
        response.setDescriptionComponent(new TextComponent(motd.getMotd()));
        response.setPlayers(new ServerPing.Players(maxCount, onlineCount, motd.getInfo()));
        event.setResponse(response);
    }

    @EventHandler
    public void handleDisconnect(PlayerDisconnectEvent event) {
        PendingConnection connection = event.getPlayer().getPendingConnection();
        ProxyServer.getInstance().getScheduler().schedule(BridgeBungee.getInstance(), () -> {
            IBridgePlayer player = server.getPlayers().getPlayer(connection.getUniqueId());
            server.sendMessage("Player " + player.getName() + " has disconnected.");
            server.getPlayers().getPlayers().remove(player);
            server.getEvents().call(new PlayerNetDisconnectEvent(player));
            server.sendPacket(new PacketPlayerNetDisconnect(player.getUniqueId(), player.getName()));
        }, 50, TimeUnit.MILLISECONDS);
    }
}