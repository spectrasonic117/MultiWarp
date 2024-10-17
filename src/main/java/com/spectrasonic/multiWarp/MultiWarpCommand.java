package com.spectrasonic.multiWarp;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MultiWarpCommand implements CommandExecutor {

    private final MultiWarp plugin;

    public MultiWarpCommand(MultiWarp plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by players.");
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("Usage: /multiwarp <create|add|delete|deletegroup|tp|version|reload>");
            return false;
        }

        String action = args[0];

        switch (action.toLowerCase()) {
            case "create":
                if (args.length == 2) {
                    String groupName = args[1];
                    Location loc = player.getLocation();
                    plugin.getWarpManager().createWarpGroup(groupName, loc);
                    player.sendMessage("Warp group " + groupName + " created with the first point.");
                } else {
                    player.sendMessage("Usage: /multiwarp create <group>");
                }
                break;

            case "add":
                if (args.length == 2) {
                    String groupName = args[1];
                    Location loc = player.getLocation();
                    plugin.getWarpManager().addWarpToGroup(groupName, loc);
                    player.sendMessage("Added a new warp point to group " + groupName);
                } else {
                    player.sendMessage("Usage: /multiwarp add <group>");
                }
                break;

            case "delete":
                if (args.length == 3) {
                    String groupName = args[1];
                    try {
                        int index = Integer.parseInt(args[2]);
                        if (plugin.getWarpManager().deleteWarp(groupName, index)) {
                            player.sendMessage("Warp point " + index + " deleted from group " + groupName);
                        } else {
                            player.sendMessage("Failed to delete warp point.");
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage("The warp index must be a number.");
                    }
                } else {
                    player.sendMessage("Usage: /multiwarp delete <group> <index>");
                }
                break;

            case "deletegroup":
                if (args.length == 2) {
                    String groupName = args[1];
                    if (plugin.getWarpManager().deleteWarpGroup(groupName)) {
                        player.sendMessage("Warp group " + groupName + " has been deleted.");
                    } else {
                        player.sendMessage("Failed to delete warp group " + groupName);
                    }
                } else {
                    player.sendMessage("Usage: /multiwarp deletegroup <group>");
                }
                break;

            case "tp":
                if (args.length == 2) {
                    String groupName = args[1];
                    List<Player> players = (List<Player>) player.getWorld().getPlayers();
                    plugin.getWarpManager().teleportPlayersToGroup(players, groupName);
                    player.sendMessage("Teleported all players to warps in group " + groupName);
                } else {
                    player.sendMessage("Usage: /multiwarp tp <group>");
                }
                break;

            case "reload":
                plugin.reloadPlugin();
                player.sendMessage("MultiWarp plugin reloaded.");
                break;

            case "version":
                player.sendMessage("MultiWarp Plugin version 1.0");
                break;

            default:
                player.sendMessage("Unknown command. Usage: /multiwarp <create|add|delete|deletegroup|tp|version|reload>");
                break;
        }

        return true;
    }
}