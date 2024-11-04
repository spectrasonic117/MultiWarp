package com.spectrasonic.multiWarp;

import com.spectrasonic.multiWarp.Utils.MessageUtils;
import com.spectrasonic.multiWarp.command.MultiWarpCommand;
import com.spectrasonic.multiWarp.command.MultiWarpTabCompleter;
import com.spectrasonic.multiWarp.manager.WarpManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.Objects;


public class Main extends JavaPlugin {

    private WarpManager warpManager;


    @SuppressWarnings("ResultOfMethodCallIgnored")
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

        MessageUtils.sendStartupMessage(this);


    }

    @Override
    public void onDisable() {
        // Save all warps when disabling the plugin
        warpManager.saveAllWarps();
        MessageUtils.sendShutdownMessage(this);

    }

    public void reloadPlugin() {
        warpManager.loadAllWarps();  // Reload all warps from the files
        MessageUtils.sendMessage(getServer().getConsoleSender(), "&aMultiWarp plugin reloaded.");
    }

    public WarpManager getWarpManager() {
        return warpManager;
    }
}
