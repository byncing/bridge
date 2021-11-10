package eu.byncing.bridge.plugin.spigot;

import eu.byncing.bridge.driver.BridgeDriver;
import eu.byncing.bridge.driver.event.EventManager;
import eu.byncing.bridge.driver.player.PlayerManager;
import eu.byncing.bridge.driver.protocol.PacketManager;
import eu.byncing.bridge.driver.service.IBridgeService;
import eu.byncing.bridge.driver.service.ServiceManager;
import eu.byncing.bridge.plugin.spigot.config.BridgeConfig;
import eu.byncing.bridge.plugin.spigot.handles.player.PlayerConnectionHandler;
import eu.byncing.bridge.plugin.spigot.handles.player.PlayerHandler;
import eu.byncing.bridge.plugin.spigot.handles.player.PlayerNetConnectionHandler;
import eu.byncing.bridge.plugin.spigot.handles.service.ServiceConnectionHandler;
import eu.byncing.bridge.plugin.spigot.handles.service.ServiceHandler;
import eu.byncing.net.api.NetClient;
import eu.byncing.net.api.protocol.Packet;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.io.IOException;
import java.net.InetSocketAddress;

public class BridgeClient extends NetClient {

    private final BridgeDriver driver = BridgeDriver.getInstance();

    private final BridgeConfig config = new BridgeConfig();

    private final PacketManager packets = driver.getPacketManager();
    private final PlayerManager players = driver.getPlayerManager();
    private final EventManager events = driver.getEventManager();
    private final ServiceManager services = driver.getServiceManager();

    private Server server;

    public BridgeClient(Server server) {
        try {
            if (config.getKey().equals("none")) {
                sendMessage("§c§lPlease enter the bridge key in the plugins/bridge/config.json file!");
                return;
            }
            sendMessage("Channel/" + config.getHost() + ":" + config.getPort() + " trying to connect!");
            this.server = server;
            this.packets.register(new PlayerNetConnectionHandler(this), new PlayerConnectionHandler(this), new PlayerHandler(this));
            this.packets.register(new ServiceConnectionHandler(this), new ServiceHandler(this));
            this.init(channel -> channel.getPipeline().handle(new ClientHandler(this))).connect(new InetSocketAddress(config.getHost(), config.getPort()));
        } catch (IOException e) {
            sendMessage("Channel/" + config.getHost() + ":" + config.getPort() + " §cfailed to connect");
        }
    }

    @Override
    public void sendPacket(Packet packet) {
        if (isConnected()) super.sendPacket(packet);
    }

    public void sendMessage(Object message) {
        Bukkit.getConsoleSender().sendMessage("§7[§aBridge§7/§aClient§7] §7" + message);
    }

    public BridgeDriver getDriver() {
        return driver;
    }

    public IBridgeService getInternalService() {
        return services.getService(config.getName());
    }

    public BridgeConfig getConfig() {
        return config;
    }

    public PacketManager getPackets() {
        return packets;
    }

    public PlayerManager getPlayers() {
        return players;
    }

    public EventManager getEvents() {
        return events;
    }

    public ServiceManager getServices() {
        return services;
    }

    public Server getServer() {
        return server;
    }
}