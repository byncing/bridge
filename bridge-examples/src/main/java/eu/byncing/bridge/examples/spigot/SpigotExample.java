package eu.byncing.bridge.examples.spigot;

import eu.byncing.bridge.driver.BridgeDriver;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotExample extends JavaPlugin {

    @Override
    public void onEnable() {
        BridgeDriver.getInstance().getEventManager().register(new SpigotListener());
    }
}