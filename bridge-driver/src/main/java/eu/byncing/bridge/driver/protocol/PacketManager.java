package eu.byncing.bridge.driver.protocol;

import eu.byncing.net.api.channel.IChannel;
import eu.byncing.net.api.protocol.Packet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacketManager {

    private final List<IPacketHandler<?>> handlers = new ArrayList<>();

    public void register(IPacketHandler<?>... handlers) {
        this.handlers.addAll(Arrays.asList(handlers));
    }

    public void unregister(IPacketHandler<?>... handlers) {
        this.handlers.removeAll(Arrays.asList(handlers));
    }

    public void handle(IChannel channel, Packet packet) {
        handlers.forEach(handle -> {
            if (Arrays.stream(handle.getClasses()).anyMatch(aClass -> aClass.equals(packet.getClass()))) {
                handle.handle(channel, newInstance(packet));
            }
        });
    }

    private <P> P newInstance(Packet packet) {
        return (P) packet;
    }

    public List<IPacketHandler<?>> getHandlers() {
        return handlers;
    }
}