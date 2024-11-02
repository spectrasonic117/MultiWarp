package com.spectrasonic.multiWarp.manager;

import com.spectrasonic.multiWarp.Main;
import com.spectrasonic.multiWarp.Utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class WarpManager {

    private final Main plugin;
    private final Map<String, List<Location>> warps;  // Warp groups with list of warps

    public WarpManager(Main plugin) {
        this.plugin = plugin;
        this.warps = new HashMap<>();
        loadAllWarps();
    }

    // Create a new warp group and add the first warp
    public void createWarpGroup(String groupName, Location location) {
        warps.putIfAbsent(groupName, new ArrayList<>());
        addWarpToGroup(groupName, location);
    }

    // Add a warp to a group
    public void addWarpToGroup(String groupName, Location location) {
        List<Location> group = warps.get(groupName);
        if (group != null) {
            group.add(location);
            saveWarp(groupName);
        }
    }

    // Delete a warp by index
    public boolean deleteWarp(String groupName, int index) {
        List<Location> group = warps.get(groupName);
        if (group != null && index < group.size()) {
            group.remove(index);
            saveWarp(groupName);
            return true;
        }
        return false;
    }

    // Delete an entire warp group
    public boolean deleteWarpGroup(String groupName) {
        if (warps.containsKey(groupName)) {
            warps.remove(groupName);
            File file = new File(plugin.getDataFolder(), "warps/" + groupName + ".yml");
            if (file.exists()) {
                file.delete();  // Delete the corresponding file
            }
            return true;
        }
        return false;
    }

    // Teleport players to the next warp in the group
    public void teleportPlayersToGroup(List<Player> players, String groupName) {
        List<Location> group = warps.get(groupName);
        if (group != null && !group.isEmpty()) {
            for (int i = 0; i < players.size(); i++) {
                players.get(i).teleport(group.get(i % group.size()));
            }
        }
    }

    // Load all warp data from files
    public void loadAllWarps() {
        warps.clear();  // Clear existing warps
        File warpFolder = new File(plugin.getDataFolder(), "warps");
        if (warpFolder.exists() && warpFolder.isDirectory()) {
            for (File file : Objects.requireNonNull(warpFolder.listFiles())) {
                if (file.getName().endsWith(".yml")) {
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                    String groupName = file.getName().replace(".yml", "");
                    List<Location> locations = (List<Location>) config.getList("warps");
                    if (locations != null) {
                        warps.put(groupName, locations);
                    }
                }
            }
        }
    }

    // Save a specific group's warps to a file
    private void saveWarp(String groupName) {
        File file = new File(plugin.getDataFolder(), "warps/" + groupName + ".yml");
        YamlConfiguration config = new YamlConfiguration();
        config.set("warps", warps.get(groupName));
        try {
            config.save(file);
        } catch (IOException e) {
            MessageUtils.sendMessage(plugin.getServer().getConsoleSender(), "&cFailed to save warps for group " + groupName);
        }
    }

    // Save all warps
    public void saveAllWarps() {
        for (String group : warps.keySet()) {
            saveWarp(group);
        }
    }

    public List<String> getWarpGroups() {
        return new ArrayList<>(warps.keySet());  // warps is the Map<String, List<Location>> storing the groups
    }
}
