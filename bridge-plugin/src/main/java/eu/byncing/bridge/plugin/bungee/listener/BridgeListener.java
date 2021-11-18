package eu.byncing.bridge.plugin.bungee.listener;

import eu.byncing.bridge.driver.BridgeDriver;
import eu.byncing.bridge.driver.event.player.PlayerNetConnectEvent;
import eu.byncing.bridge.driver.event.player.PlayerNetDisconnectEvent;
import eu.byncing.bridge.driver.player.IBridgePlayer;
import eu.byncing.bridge.driver.player.PlayerAddress;
import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerNetConnect;
import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerNetDisconnect;
import eu.byncing.bridge.driver.service.IBridgeService;
import eu.byncing.bridge.plugin.bungee.BridgeServer;
import eu.byncing.bridge.plugin.bungee.config.BridgeConfig;
import eu.byncing.bridge.plugin.bungee.config.BridgeData;
import eu.byncing.bridge.plugin.bungee.impl.BridgePlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class BridgeListener implements Listener {

    private final BridgeServer server;

    public BridgeListener(BridgeServer server) {
        this.server = server;
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void handle(ServerConnectEvent event) {
        server.getDriver().getScheduler().runDelay(() -> {
            if (event.getReason() == ServerConnectEvent.Reason.JOIN_PROXY) {
                ProxiedPlayer proxiedPlayer = event.getPlayer();
                BridgeData data = server.getConfig().getData();
                IBridgeService service = server.getServices().getService(event.getTarget().getName());

                if (data.fallback != null) service = server.getServices().getService(data.fallback);

                if (service == null) {
                    event.setCancelled(true);
                    proxiedPlayer.disconnect(new TextComponent(data.serviceOffline.replace("%service%", (data.fallback == null ? event.getTarget().getName() : data.fallback))));
                    return;
                }

                ServerInfo info = ProxyServer.getInstance().getServerInfo(service.getName());
                if (event.getTarget() != info) event.setTarget(info);
                if (data.fallback != null) event.setTarget(event.getTarget());

                String[] s = proxiedPlayer.getSocketAddress().toString().substring(1).split(":");
                IBridgePlayer player = new BridgePlayer(proxiedPlayer.getUniqueId(), proxiedPlayer.getName(), new PlayerAddress(s[0], Integer.parseInt(s[1])), service);

                String[] strings = server.getConfig().getTabStorage().update(player.getUniqueId());
                proxiedPlayer.setTabHeader(new TextComponent(strings[0]), new TextComponent(strings[1]));

                server.sendMessage("Player " + player.getName() + " has connected.");
                server.getPlayers().getPlayers().add(player);
                server.getEvents().call(new PlayerNetConnectEvent(player, service));
                server.sendPacket(new PacketPlayerNetConnect(player.getUniqueId(), player.getName(), service.getName(), player.getAddress()));

                int onlineCount = ProxyServer.getInstance().getOnlineCount();

                if (data.maintenance) {
                    boolean permission = proxiedPlayer.hasPermission(data.connectionBypass);
                    if (permission) return;
                    if (!data.isWhitelist(proxiedPlayer.getName())) {
                        proxiedPlayer.disconnect(new TextComponent(data.maintenanceMessage));
                    }
                }

                if (onlineCount > data.maxCount) {
                    if (!proxiedPlayer.hasPermission(data.connectionBypass)) {
                        proxiedPlayer.disconnect(new TextComponent(data.fullMessage));
                    }
                }
            }
        }, 100);
    }

    @EventHandler
    public void handleDisconnect(PlayerDisconnectEvent event) {
        PendingConnection connection = event.getPlayer().getPendingConnection();
        BridgeDriver.getInstance().getScheduler().runDelay(() -> {
            IBridgePlayer player = server.getPlayers().getPlayer(connection.getUniqueId());
            if (player == null) return;
            server.sendMessage("Player " + player.getName() + " has disconnected.");
            server.getPlayers().getPlayers().remove(player);
            server.getEvents().call(new PlayerNetDisconnectEvent(player));
            server.sendPacket(new PacketPlayerNetDisconnect(player.getUniqueId(), player.getName()));
        }, 50);
    }
}