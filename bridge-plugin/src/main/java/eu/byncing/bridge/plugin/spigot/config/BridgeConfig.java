package eu.byncing.bridge.plugin.spigot.config;

import eu.byncing.bridge.driver.json.JsonFile;
import eu.byncing.bridge.driver.BridgeUtil;

public class BridgeConfig {

    private final JsonFile file = new JsonFile(BridgeUtil.STATIC_GSON, "plugins/bridge/bridge.json");

    public BridgeConfig() {
        if (!this.file.exists()) {
            this.file.create();
            this.file.append("name", "InternalService");
            this.file.append("host", "127.0.0.1");
            this.file.append("port", 3000);
            this.file.append("key", "none");
            this.file.save();
        }
        this.file.load();
    }

    public String getName() {
        return file.get("name", String.class);
    }

    public String getHost() {
        return file.get("host", String.class);
    }

    public Integer getPort() {
        return file.get("port", Integer.class);
    }

    public String getKey() {
        return file.get("key", String.class);
    }
}