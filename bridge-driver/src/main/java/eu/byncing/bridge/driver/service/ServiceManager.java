package eu.byncing.bridge.driver.service;

import eu.byncing.net.api.channel.IChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServiceManager {

    private final List<IBridgeService> services = new ArrayList<>();

    public IBridgeService getService(IChannel channel) {
        return services.stream().filter(service -> service.getChannel().equals(channel)).findFirst().orElse(null);
    }

    public IBridgeService getService(UUID uniqueId) {
        return services.stream().filter(service -> service.getUniqueId().equals(uniqueId)).findFirst().orElse(null);
    }

    public IBridgeService getService(String name) {
        return services.stream().filter(service -> {
            String serviceName = service.getName().toLowerCase();
            return serviceName.equalsIgnoreCase(name.toLowerCase());
        }).findFirst().orElse(null);
    }

    public List<IBridgeService> getServices() {
        return services;
    }
}