package eu.byncing.bridge.plugin.bungee;

import eu.byncing.bridge.driver.BridgeDriver;
import eu.byncing.bridge.plugin.bungee.commands.BridgeCommand;
import eu.byncing.bridge.plugin.bungee.listener.BridgeListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

public class BridgeBungee extends Plugin {

    private static BridgeBungee instance;

    private BridgeServer server;

    @Override
    public void onEnable() {
        instance = this;
        server = new BridgeServer();
        ProxyServer proxy = ProxyServer.getInstance();
        proxy.getPluginManager().registerCommand(this, new BridgeCommand(server));
        proxy.getPluginManager().registerListener(this, new BridgeListener(server));

        BridgeDriver.getInstance().getScheduler().runTimer(() -> ProxyServer.getInstance().getPlayers().forEach(player -> {
            String[] strings = server.getConfig().getTabStorage().update(player.getUniqueId());
            player.setTabHeader(new TextComponent(strings[0]), new TextComponent(strings[1]));
        }), 1000, 1000);
    }

    @Override
    public void onDisable() {
        if (server != null && server.isConnected()) server.close();
    }

    public static BridgeBungee getInstance() {
        return instance;
    }

    public BridgeServer getServer() {
        return server;
    }
}