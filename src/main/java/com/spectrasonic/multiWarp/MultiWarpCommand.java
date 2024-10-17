package com.spectrasonic.multiWarp;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import java.util.List;

public class MultiWarpCommand implements CommandExecutor {
    private static final String prefix = ChatColor.AQUA + "[" + ChatColor.RED + "MultiWarp" + ChatColor.AQUA + "]" + ChatColor.RESET + " ";
    private static final String UsagePrefix = ChatColor.GREEN + "Usage:" + ChatColor.BLUE + "/multiwarp" + ChatColor.YELLOW + " ";

    private final MultiWarp plugin;

    public MultiWarpCommand(MultiWarp plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(prefix + ChatColor.RED + "This command can only be run by players.");
            return false;
        }

        if (args.length == 0) {
            player.sendMessage(prefix + UsagePrefix + "<create|add|delete|deletegroup|tp|version|reload>");
            return false;
        }

        String action = args[0];

        switch (action.toLowerCase()) {
            case "create":
                if (args.length == 2) {
                    String groupName = args[1];
                    Location loc = player.getLocation();
                    plugin.getWarpManager().createWarpGroup(groupName, loc);
                    player.sendMessage(prefix + UsagePrefix + "created with the first point.");
                } else {
                    player.sendMessage(prefix + UsagePrefix + "create <group>");
                }
                break;

            case "add":
                if (args.length == 2) {
                    String groupName = args[1];
                    Location loc = player.getLocation();
                    plugin.getWarpManager().addWarpToGroup(groupName, loc);
                    player.sendMessage(prefix + ChatColor.YELLOW + "Added a new warp point to group " + ChatColor.GREEN + groupName);
                } else {
                    player.sendMessage(prefix + UsagePrefix + "add <group>");
                }
                break;

            case "delete":
                if (args.length == 3) {
                    String groupName = args[1];
                    try {
                        int index = Integer.parseInt(args[2]);
                        if (plugin.getWarpManager().deleteWarp(groupName, index)) {
                            player.sendMessage(prefix + ChatColor.RED + "Warp point " + index + " deleted from group " + ChatColor.YELLOW + groupName);
                        } else {
                            player.sendMessage(prefix + ChatColor.RED + "Failed to delete warp point.");
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage(prefix + ChatColor.YELLOW + "The warp index must be a number.");
                    }
                } else {
                    player.sendMessage(prefix + UsagePrefix + "delete <group> <index>");
                }
                break;

            case "deletegroup":
                if (args.length == 2) {
                    String groupName = args[1];
                    if (plugin.getWarpManager().deleteWarpGroup(groupName)) {
                        player.sendMessage(prefix + ChatColor.YELLOW + groupName + ChatColor.RED + " has been deleted.");
                    } else {
                        player.sendMessage(prefix + ChatColor.RED + "Failed to delete warp group " + ChatColor.YELLOW +  groupName);
                    }
                } else {
                    player.sendMessage(prefix + UsagePrefix + "deletegroup <group>");
                }
                break;

            case "tp":
                if (args.length == 2) {
                    String groupName = args[1];
                    List<Player> players = (List<Player>) player.getWorld().getPlayers();
                    plugin.getWarpManager().teleportPlayersToGroup(players, groupName);
                    player.sendMessage(prefix +ChatColor.GREEN + "Teleported all players to warps in group " + ChatColor.YELLOW + groupName);
                } else {
                    player.sendMessage(prefix + UsagePrefix + "tp <group>");
                }
                break;

            case "reload":
                plugin.reloadPlugin();
                player.sendMessage(prefix + ChatColor.GREEN + "MultiWarp plugin reloaded.");
                break;

            case "version":
                String pluginVersion = plugin.getDescription().getVersion();
                String pluginAuthor = String.valueOf(plugin.getDescription().getAuthors());

                player.sendMessage(prefix + ChatColor.YELLOW + "MultiWarp Version: " + ChatColor.LIGHT_PURPLE + pluginVersion);
                player.sendMessage(prefix + ChatColor.YELLOW + "Developed by:" + ChatColor.RED + pluginAuthor);
                break;

            default:
                player.sendMessage(prefix + ChatColor.RED + "Unknown command." + UsagePrefix + "<create|add|delete|deletegroup|tp|version|reload>");
                break;
        }

        return true;
    }
}