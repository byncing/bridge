package eu.byncing.bridge.driver.protocol.packets.player;

import eu.byncing.bridge.driver.BridgeUtil;
import eu.byncing.net.api.protocol.IPacketBuffer;
import eu.byncing.net.api.protocol.Packet;

import java.util.UUID;

public class PacketPlayerTitle extends Packet {

    private UUID uniqueId;

    private String title, subtitle;

    private int fadeIn, stay, fadeOut;

    public PacketPlayerTitle() {
        super();
    }

    public PacketPlayerTitle(UUID uniqueId, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.uniqueId = uniqueId;
        this.title = BridgeUtil.chatEncode(true, title)[0];
        this.subtitle = BridgeUtil.chatEncode(true, subtitle)[0];
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    @Override
    public void write(IPacketBuffer buffer) {
        buffer.write("uniqueId", uniqueId);
        buffer.write("title", title);
        buffer.write("subtitle", subtitle);
        buffer.write("fadeIn", fadeIn);
        buffer.write("stay", stay);
        buffer.write("fadeOut", fadeOut);
    }

    @Override
    public void read(IPacketBuffer buffer) {
        uniqueId = buffer.read("uniqueId", UUID.class);
        title = BridgeUtil.chatDecode(true, buffer.read("title", String.class))[0];
        subtitle = BridgeUtil.chatDecode(true, buffer.read("subtitle", String.class))[0];
        fadeIn = buffer.read("fadeIn", Integer.class);
        stay = buffer.read("stay", Integer.class);
        fadeOut = buffer.read("fadeOut", Integer.class);
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public int getStay() {
        return stay;
    }

    public int getFadeOut() {
        return fadeOut;
    }
}