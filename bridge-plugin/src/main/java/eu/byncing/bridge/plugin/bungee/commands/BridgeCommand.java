package eu.byncing.bridge.plugin.bungee.commands;

import eu.byncing.bridge.driver.BridgeDriver;
import eu.byncing.bridge.driver.BridgeUtil;
import eu.byncing.bridge.driver.player.IBridgePlayer;
import eu.byncing.bridge.driver.service.IBridgeService;
import eu.byncing.bridge.plugin.bungee.BridgeServer;
import eu.byncing.bridge.plugin.bungee.config.BridgeConfig;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.HashSet;
import java.util.List;

public class BridgeCommand extends Command implements TabExecutor {

    private final BridgeServer server;

    public BridgeCommand(BridgeServer server) {
        super("bridge", server.getConfig().getData().commandBypass);
        this.server = server;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        BridgeDriver instance = BridgeDriver.getInstance();
        if (args.length == 0) {
            server.sendMessage(sender, "§7/bridge <reload>");
            server.sendMessage(sender, "§7/bridge <maintenance> <true, false>");
            server.sendMessage(sender, "§7/bridge <maxCount> <value>");
            server.sendMessage(sender, "§7/bridge <whitelist> <add, remove> <name>");
            server.sendMessage(sender, "§7/bridge <services>");
            server.sendMessage(sender, "§7/bridge <service> <name>");
            server.sendMessage(sender, "§7/bridge <players>");
            server.sendMessage(sender, "§7/bridge <player> <name> <info>");
            server.sendMessage(sender, "§7/bridge <player> <name> <kick> <reason>");
            server.sendMessage(sender, "§7/bridge <player> <name> <message> <value>");
            return;
        }

        if (args[0].equalsIgnoreCase("whitelist")) {
            if (args.length == 1) {
                server.sendMessage(sender, "§cArgument 1 is missing!");
                return;
            }
            if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove")) {
                if (args.length == 2) {
                    server.sendMessage(sender, "§cArgument 2 is missing!");
                    return;
                }

                BridgeConfig config = server.getConfig();
                if (args[1].equalsIgnoreCase("add")) {

                    if (config.getData().isWhitelist(args[2])) {
                        server.sendMessage(sender, "§cPlayer " + args[2] + " is already whitelisted!");
                        return;
                    }

                    config.getData().whitelist.add(args[2]);
                    server.sendMessage(sender, "§7Player " + args[2] + " was added to the whitelist!");
                } else if (args[1].equalsIgnoreCase("remove")) {

                    if (!config.getData().isWhitelist(args[2])) {
                        server.sendMessage(sender, "§cPlayer " + args[2] + " is not on the whitelist!");
                        return;
                    }

                    config.getData().whitelist.remove(args[2]);
                    server.sendMessage(sender, "§7Player " + args[2] + " was removed from the whitelist!");
                    IBridgePlayer player = instance.getPlayerManager().getPlayer(args[2]);
                    if (player == null) return;
                    if (!player.hasPermission(config.getData().connectionBypass))
                        player.kick(config.getData().maintenanceMessage);
                }
                config.save();
            }
        }

        if (args[0].equalsIgnoreCase("reload")) {
            server.getConfig().reload();
            server.sendMessage(sender, "§7Bridge has reloaded!");
        }
        if (args[0].equalsIgnoreCase("service")) {
            if (args.length == 1) {
                server.sendMessage(sender, "§cArgument 1 is missing!");
                return;
            }
            IBridgeService service = server.getServices().getService(args[1]);
            if (service == null) {
                server.sendMessage(sender, "§cService " + args[1] + " is offline!");
                return;
            }
            server.sendMessage(sender, "§7Service: §a" + service.getName());
            server.sendMessage(sender, "§7- UUID: " + service.getUniqueId());
            server.sendMessage(sender, "§7- Motd: " + service.getMotd());
            server.sendMessage(sender, "§7- OnlineCount: " + service.getOnlineCount());
            server.sendMessage(sender, "§7- MaxCount: " + service.getMaxCount());
            server.sendMessage(sender, "§7- Players: §a" + service.getPlayers().size());
            service.getPlayers().forEach(player -> {
                server.sendMessage(sender, "§7- Name: " + player.getName());
                server.sendMessage(sender, "§7- UUID: " + player.getUniqueId());
            });

        }
        if (args[0].equalsIgnoreCase("player")) {
            if (args.length == 1) {
                server.sendMessage(sender, "§cArgument 1 is missing!");
                return;
            }
            IBridgePlayer player = server.getPlayers().getPlayer(args[1]);
            if (player == null) {
                server.sendMessage(sender, "§cPlayer " + args[1] + " is offline!");
                return;
            }
            if (args.length == 2) {
                server.sendMessage(sender, "§cArgument 2 is missing!");
                return;
            }
            if (args[2].equalsIgnoreCase("info")) {
                server.sendMessage(sender, "§7- Player: §a" + player.getName());
                server.sendMessage(sender, "§7- UUID: " + player.getUniqueId());
                server.sendMessage(sender, "§7- Service: §a" + player.getService().getName());
                IBridgeService service = player.getService();
                server.sendMessage(sender, "§7- UUID: " + service.getUniqueId());
                server.sendMessage(sender, "§7- Motd: " + service.getMotd());
                server.sendMessage(sender, "§7- OnlineCount: " + service.getOnlineCount());
                server.sendMessage(sender, "§7- MaxCount: " + service.getMaxCount());
                server.sendMessage(sender, "§7- Players: " + service.getPlayers().size());
            }
            if (args[2].equalsIgnoreCase("kick") || args[2].equalsIgnoreCase("message")) {
                if (args.length == 3) {
                    server.sendMessage(sender, "§cArgument 3 is missing!");
                    return;
                }
                if (args[2].equalsIgnoreCase("kick")) player.kick(args[3].replace(" ", "_"));
                if (args[2].equalsIgnoreCase("message")) player.sendMessage(args[3].replace(" ", "_"));
            }
        }
        if (args[0].equalsIgnoreCase("maxCount") || args[0].equalsIgnoreCase("maintenance")) {
            if (args.length == 1) {
                server.sendMessage(sender, "§cArgument 1 is missing!");
                return;
            }
            BridgeConfig config = server.getConfig();
            if (args[0].equalsIgnoreCase("maintenance")) {
                if (args[1].equals("true") || args[1].equals("false")) {
                    config.getData().maintenance = Boolean.parseBoolean(args[1]);
                    config.save();

                    instance.getPlayerManager().getPlayers().forEach(player -> {
                        if (!player.hasPermission(config.getData().connectionBypass)) {
                            player.kick(config.getData().maintenanceMessage);
                        }
                    });
                    server.sendMessage(sender, "§7The maintenance was changed to " + args[1]);
                } else server.sendMessage(sender, "§cArgument 1 is no a boolean, try (true, false)");
            }
            if (args[0].equalsIgnoreCase("maxCount")) {
                if (BridgeUtil.isNumber(args[1])) {
                    config.getData().maxCount = Integer.parseInt(args[1]);
                    config.save();
                    server.sendMessage(sender, "§7The maximum number of players was changed to " + args[1]);
                } else server.sendMessage(sender, "§cArgument 1 is no a integer!");
            }
        }
        if (args[0].equalsIgnoreCase("players")) {
            List<IBridgePlayer> players = instance.getPlayerManager().getPlayers();
            server.sendMessage(sender, "§7List all players: " + players.size());
            players.forEach(player -> {
                server.sendMessage(sender, "§7- Player: §a" + player.getName());
                server.sendMessage(sender, "§7- §7UUID: " + player.getUniqueId());
                server.sendMessage(sender, "§7- §7Service: " + player.getService().getName());
            });
        }
        if (args[0].equalsIgnoreCase("services")) {
            List<IBridgeService> services = instance.getServiceManager().getServices();
            server.sendMessage(sender, "§7List all services: " + services.size());
            services.forEach(service -> {
                server.sendMessage(sender, "§7- Service: §a" + service.getName());
                server.sendMessage(sender, "§7- Motd: " + service.getMotd());
                server.sendMessage(sender, "§7- OnlineCount: " + service.getOnlineCount());
                server.sendMessage(sender, "§7- MaxCount: " + service.getMaxCount());
                server.sendMessage(sender, "§7- Players: §a" + service.getPlayers().size());
                service.getPlayers().forEach(player -> server.sendMessage(sender, "  §7- §o" + player.getName()));
            });
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return new HashSet<>();
    }
}