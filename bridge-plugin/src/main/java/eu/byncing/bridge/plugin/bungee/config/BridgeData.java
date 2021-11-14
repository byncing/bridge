package eu.byncing.bridge.plugin.bungee.config;

import java.util.ArrayList;
import java.util.List;

public class BridgeData {

    public String name = "InternalBridge";
    public String fallback = null;
    public List<String> whitelist = new ArrayList<>();
    public boolean maintenance = true;
    public int maxCount = 55;

    public TabList tabList = new TabList();
    public Motd motd = new Motd();

    public String fullMessage = "§cThe network is full!";
    public String serviceOffline = "§cService %service% is offline!";
    public String maintenanceMessage = "§cThe network is in maintenance!";
    public String commandBypass = "bridge.command.bypass", connectionBypass = "bridge.connection.bypass";

    public boolean isWhitelist(String name) {
        return whitelist.stream().anyMatch(s -> s.equals(name));
    }

    public static class Motd {

        public String[] lines = new String[]{"         §aBridge §7» §7software by §a§obyncing", "             §fA synchronized system"};

        public String[] info = new String[]{};

        public String name = null;

        public MaintenanceMotd maintenanceMotd = new MaintenanceMotd();

        public static class MaintenanceMotd {

            public String[] lines = new String[]{"         §aBridge §7» §7software by §a§obyncing", "             §fA synchronized system"};

            public String[] info = new String[]{};

            public String name = "§7» §c§oMaintenance";
        }
    }

    public static class TabList {

        public String header = "\n §a%bridge% §7» §a§o%onlineCount%§7/§f§o%maxCount%§7 \n", footer = "\n §fA synchronized system \n";

        public String getHeader() {
            return header;
        }

        public String getFooter() {
            return footer;
        }
    }
}