package com.spectrasonic.multiWarp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiWarpTabCompleter implements TabCompleter {

    private final MultiWarp plugin;

    public MultiWarpTabCompleter(MultiWarp plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }

        if (args.length == 1) {
            return Arrays.asList("create", "add", "delete", "deletegroup", "tp", "version", "reload");
        }

        if (args.length == 2 && (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("deletegroup"))) {
            return new ArrayList<>(plugin.getWarpManager().getWarpGroups());
        }

        return null;
    }
}