package eu.byncing.bridge.driver;

import eu.byncing.bridge.driver.event.EventManager;
import eu.byncing.bridge.driver.player.PlayerManager;
import eu.byncing.bridge.driver.protocol.PacketManager;
import eu.byncing.bridge.driver.service.ServiceManager;

public class BridgeDriver {

    private static final BridgeDriver INSTANCE = new BridgeDriver();

    private final PacketManager packetManager = new PacketManager();
    private final ServiceManager serviceManager = new ServiceManager();
    private final EventManager eventManager = new EventManager();
    private final PlayerManager playerManager = new PlayerManager();

    public static BridgeDriver getInstance() {
        return BridgeDriver.INSTANCE;
    }

    public PacketManager getPacketManager() {
        return packetManager;
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}