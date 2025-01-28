package com.spectrasonic.multiWarp.command;

import com.spectrasonic.multiWarp.Main;
import com.spectrasonic.multiWarp.Utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("NullableProblems")
public class MultiWarpCommand implements CommandExecutor {

    private final Main plugin;

    public MultiWarpCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            MessageUtils.sendMessage(sender, "&cUsage: /multiwarp <create|add|delete|deletegroup|tp|version|reload>");
            return false;
        }

        String action = args[0].toLowerCase();

        // Commands that can be executed by both console and players
        switch (action) {
            case "version":
                return handleVersionCommand(sender);
            case "reload":
                return handleReloadCommand(sender);
            case "tp":
                return handleTeleportCommand(sender, args);
            case "deletegroup":
                return handleDeleteGroupCommand(sender, args);
        }

        // Commands that require a player
        if (!(sender instanceof Player player)) {
            MessageUtils.sendMessage(sender, "&cThis command requires a player location. It cannot be run from console.");
            return false;
        }

        // Player-only commands that need location data
        return switch (action) {
            case "create" -> handleCreateCommand(player, args);
            case "add" -> handleAddCommand(player, args);
            case "delete" -> handleDeleteCommand(player, args);
            default -> {
                MessageUtils.sendMessage(sender, "&cUnknown command. Use &e/multiwarp &c<create|add|delete|deletegroup|tp|version|reload>");
                yield false;
            }
        };
    }

    private boolean handleVersionCommand(CommandSender sender) {
        String pluginVersion = plugin.getDescription().getVersion();
        String pluginAuthor = String.valueOf(plugin.getDescription().getAuthors());

        MessageUtils.sendMessage(sender, "MultiWarp Version: &d" + pluginVersion);
        MessageUtils.sendMessage(sender, "Developed by: &c" + pluginAuthor);
        return true;
    }

    private boolean handleReloadCommand(CommandSender sender) {
        plugin.reloadPlugin();
        MessageUtils.sendMessage(sender, "&aMultiWarp plugin reloaded.");
        return true;
    }

    private boolean handleTeleportCommand(CommandSender sender, String[] args) {
        if (args.length != 2) {
            MessageUtils.sendMessage(sender, "&eUsage: /multiwarp tp <group>");
            return false;
        }

        String groupName = args[1];
        List<Player> playersToTeleport = plugin.getServer().getOnlinePlayers().stream()
                .filter(p -> !p.hasPermission("multiwarp.teleport.bypass"))
                .collect(Collectors.toList());

        if (playersToTeleport.isEmpty()) {
            MessageUtils.sendMessage(sender, "&cNo players available for teleportation.");
            return false;
        }

        plugin.getWarpManager().teleportPlayersToGroup(playersToTeleport, groupName);
        MessageUtils.sendMessage(sender, "&aTeleported players without bypass permission to warps in group " + groupName);
        return true;
    }

    private boolean handleDeleteGroupCommand(CommandSender sender, String[] args) {
        if (args.length != 2) {
            MessageUtils.sendMessage(sender, "&cUsage: /multiwarp deletegroup <group>");
            return false;
        }

        String groupName = args[1];
        if (plugin.getWarpManager().deleteWarpGroup(groupName)) {
            MessageUtils.sendMessage(sender, "&e" + groupName + " &chas been deleted.");
            return true;
        }

        MessageUtils.sendMessage(sender, "&cFailed to delete warp group " + groupName);
        return false;
    }

    private boolean handleCreateCommand(Player player, String[] args) {
        if (args.length != 2) {
            MessageUtils.sendMessage(player, "&cUsage: /multiwarp create <group>");
            return false;
        }

        String groupName = args[1];
        Location loc = player.getLocation();
        plugin.getWarpManager().createWarpGroup(groupName, loc);
        MessageUtils.sendMessage(player, "&aCreated multiwarp group &b" + groupName);
        return true;
    }

    private boolean handleAddCommand(Player player, String[] args) {
        if (args.length != 2) {
            MessageUtils.sendMessage(player, "&cUsage: /multiwarp add <group>");
            return false;
        }

        String groupName = args[1];
        Location loc = player.getLocation();
        plugin.getWarpManager().addWarpToGroup(groupName, loc);
        MessageUtils.sendMessage(player, "&aAdded a new warp point to group &b" + groupName);
        return true;
    }

    private boolean handleDeleteCommand(Player player, String[] args) {
        if (args.length != 3) {
            MessageUtils.sendMessage(player, "&cUsage: /multiwarp delete <group> <index>");
            return false;
        }

        String groupName = args[1];
        try {
            int index = Integer.parseInt(args[2]);
            if (plugin.getWarpManager().deleteWarp(groupName, index)) {
                MessageUtils.sendMessage(player, "&aWarp point &b" + index + "&a deleted from group &b" + groupName);
                return true;
            }
            MessageUtils.sendMessage(player, "&cFailed to delete warp point.");
            return false;
        } catch (NumberFormatException e) {
            MessageUtils.sendMessage(player, "&cThe warp index must be a number.");
            return false;
        }
    }
}