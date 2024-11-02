package com.spectrasonic.multiWarp.command;

import com.spectrasonic.multiWarp.Main;
import com.spectrasonic.multiWarp.Utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class MultiWarpCommand implements CommandExecutor {

    private final Main plugin;

    public MultiWarpCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            MessageUtils.sendMessage(sender, "&cThis command can only be run by players.");
            return false;
        }

        if (args.length == 0) {
            MessageUtils.sendMessage(sender, "&cUsage: /multiwarp <create|add|delete|deletegroup|tp|version|reload>");
            return false;
        }

        String action = args[0];

        switch (action.toLowerCase()) {
            case "create":
                if (args.length == 2) {
                    String groupName = args[1];
                    Location loc = player.getLocation();
                    plugin.getWarpManager().createWarpGroup(groupName, loc);
                    MessageUtils.sendMessage(sender, "&aCreated warp group &b" + groupName);
                } else {
                    MessageUtils.sendMessage(sender, "&cUsage: /multiwarp create <group>");
                }
                break;

            case "add":
                if (args.length == 2) {
                    String groupName = args[1];
                    Location loc = player.getLocation();
                    plugin.getWarpManager().addWarpToGroup(groupName, loc);
                    MessageUtils.sendMessage(sender, "&aAdded a new warp point to group &b" + groupName);
                } else {
                    MessageUtils.sendMessage(sender, "&cUsage: /multiwarp add <group>");
                }
                break;

            case "delete":
                if (args.length == 3) {
                    String groupName = args[1];
                    try {
                        int index = Integer.parseInt(args[2]);
                        if (plugin.getWarpManager().deleteWarp(groupName, index)) {
                            MessageUtils.sendMessage(sender, "&aWarp point &b" + index + "&a deleted from group &b" + groupName);
                        } else {
                            MessageUtils.sendMessage(sender, "&cFailed to delete warp point.");
                        }
                    } catch (NumberFormatException e) {
                        MessageUtils.sendMessage(sender, "&cThe warp index must be a number.");
                    }
                } else {
                    MessageUtils.sendMessage(sender, "&cUsage: /multiwarp delete <group> <index>");
                }
                break;

            case "deletegroup":
                if (args.length == 2) {
                    String groupName = args[1];
                    if (plugin.getWarpManager().deleteWarpGroup(groupName)) {
                        MessageUtils.sendMessage(sender, "&e" + groupName + " &chas been deleted.");
                    } else {
                        MessageUtils.sendMessage(sender, "&cFailed to delete warp group " + groupName);
                    }
                } else {
                    MessageUtils.sendMessage(sender, "&cUsage: /multiwarp deletegroup <group>");
                }
                break;

            case "tp":
                if (args.length == 2) {
                    String groupName = args[1];

                    List<Player> playersToTeleport = player.getWorld().getPlayers().stream()
                            .filter(p -> !p.hasPermission("multiwarp.teleport.bypass"))
                            .collect(Collectors.toList());

                    if (playersToTeleport.isEmpty()) {
                        MessageUtils.sendMessage(sender, "&cNo players available for teleportation.");
                    } else {
                        plugin.getWarpManager().teleportPlayersToGroup(playersToTeleport, groupName);
                        MessageUtils.sendMessage(sender, "&aTeleported players without bypass permission to warps in group " + groupName);
                    }
                } else {
                    MessageUtils.sendMessage(sender, "&eUsage: /multiwarp tp <group>");
                }
                break;

            case "reload":
                plugin.reloadPlugin();
                MessageUtils.sendMessage(sender, "&aMultiWarp plugin reloaded.");
                break;

            case "version":
                String pluginVersion = plugin.getDescription().getVersion();
                String pluginAuthor = String.valueOf(plugin.getDescription().getAuthors());

                MessageUtils.sendMessage(sender, "&aMultiWarp Version: " + ChatColor.LIGHT_PURPLE + pluginVersion);
                MessageUtils.sendMessage(sender, "&aDeveloped by:" + ChatColor.RED + pluginAuthor);
                break;

            default:
                MessageUtils.sendMessage(sender, "&cUnknown command. Use &e/multiwarp &c<create|add|delete|deletegroup|tp|version|reload>");
                break;
        }

        return true;
    }
}