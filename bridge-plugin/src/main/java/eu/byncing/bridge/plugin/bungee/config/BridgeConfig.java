package eu.byncing.bridge.plugin.bungee.config;

import eu.byncing.bridge.driver.BridgeDriver;
import eu.byncing.bridge.driver.BridgeUtil;
import eu.byncing.bridge.driver.json.JsonFile;
import eu.byncing.bridge.driver.player.IBridgePlayer;
import net.md_5.bungee.api.ServerPing;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BridgeConfig {

    private final JsonFile file = new JsonFile(BridgeUtil.STATIC_GSON, "plugins/bridge/bridge.json");

    private BridgeData data = new BridgeData();

    private TabStorage tabStorage;
    private MotdStorage motdStorage;

    public BridgeConfig() {
        if (!this.file.exists()) {
            this.file.create();
            this.file.append("port", 3000);
            this.file.append("key", BridgeUtil.generatingString(12, 48, 122));
            this.file.append("proxy", data);
            this.file.save();
        }
        this.file.load();
        this.data = file.get("proxy", BridgeData.class);
        this.tabStorage = new TabStorage(this, data.tabList);
        if (data.maintenance) this.motdStorage = new MotdStorage(data.motd.maintenanceMotd);
        else this.motdStorage = new MotdStorage(data.motd);
    }

    public void reload() {
        file.load();
        data = file.get("proxy", BridgeData.class);
        tabStorage = new TabStorage(this, data.tabList);
        if (data.maintenance) motdStorage = new MotdStorage(data.motd.maintenanceMotd);
        else motdStorage = new MotdStorage(data.motd);
    }

    public void save() {
        file.append("proxy", data);
        file.save();
        data = file.get("proxy", BridgeData.class);
        tabStorage = new TabStorage(this, data.tabList);
        if (data.maintenance) motdStorage = new MotdStorage(data.motd.maintenanceMotd);
        else motdStorage = new MotdStorage(data.motd);
    }

    public JsonFile getFile() {
        return file;
    }

    public Integer getPort() {
        return file.get("port", Integer.class);
    }

    public String getKey() {
        return file.get("key", String.class);
    }

    public BridgeData getData() {
        return data;
    }

    public TabStorage getTabStorage() {
        return tabStorage;
    }

    public MotdStorage getMotdStorage() {
        return motdStorage;
    }

    public static class MotdStorage {

        private final String motd;

        private ServerPing.PlayerInfo[] info;

        private final String name;

        public MotdStorage(BridgeData.Motd motd) {
            this.motd = motd.lines[0] + "\n" + motd.lines[1];

            List<ServerPing.PlayerInfo> infoList = new ArrayList<>();
            if (motd.info.length > 0) {
                for (int i = 0; i < motd.info.length; i++) {
                    infoList.add(new ServerPing.PlayerInfo(motd.info[i], String.valueOf(i)));
                }
                this.info = infoList.toArray(new ServerPing.PlayerInfo[0]);
            }
            this.name = motd.name;
        }

        public MotdStorage(BridgeData.Motd.MaintenanceMotd motd) {
            this.motd = motd.lines[0] + "\n" + motd.lines[1];

            List<ServerPing.PlayerInfo> infoList = new ArrayList<>();
            if (motd.info.length > 0) {
                for (int i = 0; i < motd.info.length; i++) {
                    infoList.add(new ServerPing.PlayerInfo(motd.info[i], String.valueOf(i)));
                }
                this.info = infoList.toArray(new ServerPing.PlayerInfo[0]);
            }
            this.name = motd.name;
        }

        public String getMotd() {
            return motd;
        }

        public ServerPing.PlayerInfo[] getInfo() {
            return info;
        }

        public String getName() {
            return name;
        }
    }

    public static class TabStorage {

        private final BridgeConfig config;

        private final String header, footer;

        public TabStorage(BridgeConfig config, BridgeData.TabList tabList) {
            this.config = config;
            header = tabList.getHeader();
            footer = tabList.getFooter();
        }

        public String[] update(UUID uniqueId) {
            IBridgePlayer player = BridgeDriver.getInstance().getPlayerManager().getPlayer(uniqueId);
            if (player == null) return new String[]{"", ""};
            if (player.getService() == null) return new String[]{"", ""};
            BridgeUtil.StringHelper builder = BridgeUtil.builder(header, footer);
            return builder.replace(config.data.name, "%bridge%").
                    replace(BridgeDriver.getInstance().getPlayerManager().getPlayers().size(), "%onlineCount%").
                    replace(config.data.maxCount, "%maxCount%").
                    replace(player.getName(), "%player%").
                    replace(player.getService().getName(), "%service%").build();
        }

        public String getHeader() {
            return header;
        }

        public String getFooter() {
            return footer;
        }
    }
}