package eu.byncing.bridge.driver;

import eu.byncing.bridge.driver.event.IEventManager;
import eu.byncing.bridge.driver.player.PlayerManager;
import eu.byncing.bridge.driver.protocol.PacketManager;
import eu.byncing.bridge.driver.service.IBridgeService;
import eu.byncing.bridge.driver.service.ServiceManager;
import eu.byncing.scheduler.Scheduler;

public interface IBridgeDriver {

    static IBridgeDriver getInstance() {
        return BridgeDriver.getInstance();
    }

    Scheduler getScheduler();

    IBridgeService getInternalService();

    PacketManager getPacketManager();

    ServiceManager getServiceManager();

    IEventManager getEventManager();

    PlayerManager getPlayerManager();
}