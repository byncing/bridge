package eu.byncing.bridge.driver.player;

import eu.byncing.bridge.driver.command.ICommandSender;
import eu.byncing.bridge.driver.service.IBridgeService;
import eu.byncing.net.api.protocol.IPacketSender;
import eu.byncing.net.api.protocol.Packet;

import java.util.UUID;

public interface IBridgePlayer extends IPacketSender, ICommandSender {

    @Override
    void sendPacket(Packet packet);

    @Override
    void sendMessage(String message);

    void kick(String reason);

    void connect(IBridgeService service);

    void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut);

    boolean hasPermission(String permission);

    UUID getUniqueId();

    String getName();

    IBridgeService getService();
}