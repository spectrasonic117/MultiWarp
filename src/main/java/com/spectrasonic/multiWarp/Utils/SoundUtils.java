package com.spectrasonic.multiWarp.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class SoundUtils {

    private SoundUtils() {
        // Private constructor to prevent instantiation
    }

    public static void playerSound(Player player, Sound sound, float volume, float pitch) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public static void broadcastPlayerSound(Sound sound, float volume, float pitch) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public static void sendStartupSound(JavaPlugin plugin) {
        broadcastPlayerSound(Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
    }

    public static void sendShutdownSound(JavaPlugin plugin) {
        broadcastPlayerSound(Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
    }
}