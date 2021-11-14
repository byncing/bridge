package eu.byncing.bridge.plugin.bungee;

import eu.byncing.bridge.driver.BridgeDriver;
import eu.byncing.bridge.plugin.EventManager;
import eu.byncing.bridge.driver.player.PlayerManager;
import eu.byncing.bridge.driver.protocol.PacketManager;
import eu.byncing.bridge.driver.service.IBridgeService;
import eu.byncing.bridge.driver.service.ServiceManager;
import eu.byncing.bridge.plugin.bungee.config.BridgeConfig;
import eu.byncing.bridge.plugin.bungee.handles.ServiceHandler;
import eu.byncing.bridge.plugin.bungee.handles.player.PlayerConnectionHandler;
import eu.byncing.bridge.plugin.bungee.handles.player.PlayerHandler;
import eu.byncing.net.api.NetOption;
import eu.byncing.net.api.NetServer;
import eu.byncing.net.api.protocol.Packet;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

public class BridgeServer extends NetServer {

    private final BridgeDriver driver = (BridgeDriver) BridgeDriver.getInstance();

    private final BridgeConfig config = new BridgeConfig();

    private final PacketManager packets = driver.getPacketManager();
    private final PlayerManager players = driver.getPlayerManager();
    private EventManager events;
    private final ServiceManager services = driver.getServiceManager();

    public BridgeServer() {
        try {
            this.packets.register(new ServiceHandler(this), new PlayerConnectionHandler(this), new PlayerHandler(this));
            this.driver.setEventManager(new EventManager());
            this.events = (EventManager) driver.getEventManager();
            this.option(NetOption.BUFFER_SIZE, 2024).init(channel -> channel.getPipeline().handle(new ServerHandler(this))).bind(new InetSocketAddress(config.getPort()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPacket(Packet packet) {
        List<IBridgeService> services = this.services.getServices();
        for (int i = services.size() - 1; i >= 0; i--) {
            IBridgeService service = services.get(i);
            if (service.getChannel().isConnected()) service.sendPacket(packet);
        }
    }

    public void sendMessage(Object message) {
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§7[§aBridge§7/§aServer§7] §7" + message));
    }

    public void sendMessage(CommandSender sender, Object message) {
        sender.sendMessage(TextComponent.fromLegacyText("§7[§aBridge§7/§aServer§7] §7" + message));
    }

    public BridgeDriver getDriver() {
        return driver;
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
}