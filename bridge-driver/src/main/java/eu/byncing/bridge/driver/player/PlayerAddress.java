package eu.byncing.bridge.driver.player;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class PlayerAddress {

    private final String host;
    private final int port;

    public PlayerAddress(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public InetSocketAddress getInetSocketAddress() {
        return new InetSocketAddress(host, port);
    }

    public InetAddress getAddress() {
        return getInetSocketAddress().getAddress();
    }

    @Override
    public String toString() {
        return "/" + host + ":" + port;
    }
}