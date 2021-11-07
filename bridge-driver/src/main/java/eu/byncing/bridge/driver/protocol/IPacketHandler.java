package eu.byncing.bridge.driver.protocol;

import eu.byncing.net.api.channel.IChannel;
import eu.byncing.net.api.protocol.Packet;

public interface IPacketHandler<P extends Packet> {

    void handle(IChannel channel, P packet);

    Class<? extends Packet>[] getClasses();
}