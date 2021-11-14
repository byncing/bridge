package eu.byncing.bridge.examples.bungee;

import eu.byncing.bridge.driver.BridgeDriver;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeExample extends Plugin {

    @Override
    public void onEnable() {
        BridgeDriver.getInstance().getEventManager().register(new BungeeListener());
    }
}