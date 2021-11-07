package eu.byncing.bridge.plugin.spigot;

import eu.byncing.bridge.driver.BridgeUtil;
import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerDisconnect;
import eu.byncing.bridge.driver.protocol.packets.player.PacketPlayerUpdate;
import eu.byncing.bridge.driver.protocol.packets.service.PacketServiceUpdate;
import eu.byncing.bridge.driver.service.IBridgeService;
import eu.byncing.bridge.plugin.spigot.listener.BridgeListener;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public class BridgeSpigot extends JavaPlugin {

    private static BridgeSpigot instance;

    private BridgeClient client;

    @Override
    public void onEnable() {
        instance = this;
        Server server = this.getServer();
        client = new BridgeClient(server);
        server.getPluginManager().registerEvents(new BridgeListener(client), this);
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            if (client != null && client.isConnected()) {
                IBridgeService service = client.getServices().getService(client.getInternalServiceUUID());
                if (service != null) {
                    client.sendPacket(new PacketServiceUpdate(service.getUniqueId(), service.getName(), BridgeUtil.builder(server.getMotd()).replace("§", "").buildIndex(0), server.getOnlinePlayers().size(), server.getMaxPlayers()));
                    Bukkit.getOnlinePlayers().forEach(player -> client.sendPacket(new PacketPlayerUpdate(player.getUniqueId(), player.getName(), service.getUniqueId())));
                }
            }
        }, 20, 20);
    }

    @Override
    public void onDisable() {
        if (client != null && client.isConnected()) {
            IBridgeService service = client.getServices().getService(client.getInternalServiceUUID());
            service.getPlayers().forEach(player -> service.sendPacket(new PacketPlayerDisconnect(player.getUniqueId(), player.getName(), player.getService().getUniqueId())));
            client.close();
        }
    }

    public static BridgeSpigot getInstance() {
        return instance;
    }

    public BridgeClient getClient() {
        return client;
    }
}