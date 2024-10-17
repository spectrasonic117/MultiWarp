package com.spectrasonic.multiWarp;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.Objects;


public class MultiWarp extends JavaPlugin {

    private WarpManager warpManager;

    private static final String divider = "------------------------------";
    private static final String prefix = ChatColor.AQUA + "[" + ChatColor.RED + "MultiWarp" + ChatColor.AQUA + "]" + ChatColor.RESET + " ";
    private final String version = getDescription().getVersion();

    @Override
    public void onEnable() {
        // Initialize WarpManager
        warpManager = new WarpManager(this);

        // Register commands
        Objects.requireNonNull(getCommand("multiwarp")).setExecutor(new MultiWarpCommand(this));
        Objects.requireNonNull(getCommand("multiwarp")).setTabCompleter(new MultiWarpTabCompleter(this));

        // Create data folder if it doesn't exist
        File warpFolder = new File(getDataFolder(), "warps");
        if (!warpFolder.exists()) {
            warpFolder.mkdirs();
        }

        // Enable
        getServer().getConsoleSender().sendMessage(divider);
        getServer().getConsoleSender().sendMessage("");
        getServer().getConsoleSender().sendMessage(prefix + ChatColor.GREEN + "MultiWarp Plugin enabled!");
        getServer().getConsoleSender().sendMessage(prefix + ChatColor.LIGHT_PURPLE + "Version: " + ChatColor.AQUA + version);
        getServer().getConsoleSender().sendMessage(prefix + ChatColor.GOLD + "Developed by " + ChatColor.RED + "Spectrasonic");
        getServer().getConsoleSender().sendMessage("");
        getServer().getConsoleSender().sendMessage(divider);
    }

    @Override
    public void onDisable() {
        // Save all warps when disabling the plugin
        warpManager.saveAllWarps();
        // Disable
        getServer().getConsoleSender().sendMessage(divider);
        getServer().getConsoleSender().sendMessage("");
        getServer().getConsoleSender().sendMessage(prefix + ChatColor.RED + "MultiWarp Plugin Disabled successfully!");
        getServer().getConsoleSender().sendMessage("");
        getServer().getConsoleSender().sendMessage(divider);

    }

    public void reloadPlugin() {
        warpManager.loadAllWarps();  // Reload all warps from the files
        getLogger().info("MultiWarp plugin reloaded.");
    }

    public WarpManager getWarpManager() {
        return warpManager;
    }
}
