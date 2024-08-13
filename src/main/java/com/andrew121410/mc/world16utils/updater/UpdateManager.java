package com.andrew121410.mc.world16utils.updater;

import com.andrew121410.ccutils.utils.HashBasedUpdater;
import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateManager {

    private static final Map<String, HashBasedUpdater> updaters = new HashMap<>();

    public static void init() {
    }

    public static void registerUpdater(JavaPlugin javaPlugin, HashBasedUpdater updater, boolean silent) {
        updaters.putIfAbsent(javaPlugin.getName(), updater);

        if (!silent) {
            javaPlugin.getServer().getScheduler().runTaskAsynchronously(javaPlugin, () -> {
                if (updater.shouldUpdate()) {
                    javaPlugin.getLogger().info(javaPlugin.getName() + " has an update available!");
                    javaPlugin.getLogger().info("You can update it by running: /world1-6utils update " + javaPlugin.getName());
                }
            });
        }
    }

    public static void registerUpdater(JavaPlugin javaPlugin, HashBasedUpdater updater) {
        UpdateManager.registerUpdater(javaPlugin, updater, false);
    }

    public static void update(CommandSender sender, String pluginName) {
        HashBasedUpdater updater = UpdateManager.getUpdater(pluginName);
        if (updater == null) {
            sender.sendMessage(Translate.color("&eThere is no updater for " + pluginName + "."));
            return;
        }

        sender.sendMessage(Translate.miniMessage("<rainbow>" + pluginName + " -> <yellow>Checking for updates..."));
        World16Essentials.getPlugin().getServer().getScheduler().runTaskAsynchronously(World16Essentials.getPlugin(), () -> {
            if (updater.shouldUpdate()) {
                sender.sendMessage(Translate.miniMessage("<rainbow>" + pluginName + " -> <red>Updating..."));
                sender.sendMessage(Translate.miniMessage("<rainbow>" + pluginName + " -> <green>" + updater.update()));
            } else {
                sender.sendMessage(Translate.miniMessage("<rainbow>" + pluginName + " -> <gray>There is no update available."));
            }
        });
    }

    public static void updateAll(CommandSender sender) {
        updaters.forEach((pluginName, updater) -> update(sender, pluginName));
    }

    public static HashBasedUpdater getUpdater(JavaPlugin javaPlugin) {
        return getUpdater(javaPlugin.getName());
    }

    public static HashBasedUpdater getUpdater(String pluginName) {
        return updaters.get(pluginName);
    }

    public static List<String> getPluginNamesFromUpdaters() {
        return new ArrayList<>(updaters.keySet());
    }
}