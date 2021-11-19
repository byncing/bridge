package eu.byncing.bridge.driver;

import eu.byncing.bridge.driver.event.IEventManager;
import eu.byncing.bridge.driver.player.PlayerManager;
import eu.byncing.bridge.driver.protocol.PacketManager;
import eu.byncing.bridge.driver.service.IBridgeService;
import eu.byncing.bridge.driver.service.ServiceManager;
import eu.byncing.scheduler.Scheduler;

public class BridgeDriver implements IBridgeDriver {

    private static final IBridgeDriver INSTANCE = new BridgeDriver();

    private IBridgeService internalService;

    private final Scheduler scheduler = new Scheduler();

    private final PacketManager packetManager = new PacketManager();
    private final ServiceManager serviceManager = new ServiceManager();
    private IEventManager eventManager;
    private final PlayerManager playerManager = new PlayerManager();

    public static IBridgeDriver getInstance() {
        return BridgeDriver.INSTANCE;
    }

    @Override
    public IBridgeService getInternalService() {
        return internalService;
    }

    public void setInternalService(IBridgeService internalService) {
        this.internalService = internalService;
    }

    @Override
    public Scheduler getScheduler() {
        return scheduler;
    }

    @Override
    public PacketManager getPacketManager() {
        return packetManager;
    }

    @Override
    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    @Override
    public IEventManager getEventManager() {
        return eventManager;
    }

    public void setEventManager(IEventManager eventManager) {
        this.eventManager = eventManager;
    }

    @Override
    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}