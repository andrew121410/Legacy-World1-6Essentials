package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.config.CustomConfigManager;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class DebugCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final CustomConfigManager customConfigManager;
    private final API api;

    public DebugCMD(World16Essentials plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.customConfigManager = customConfigManager;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("debug1-6").setExecutor(this);
        this.plugin.getCommand("debug1-6").setTabCompleter((sender, command, s, args) -> {
            if (!(sender instanceof Player player)) return null;
            if (!player.hasPermission("world16.debug")) return null;

            if (args.length == 1) {
                return TabUtils.getContainsString(args[0], Arrays.asList("reload", "load", "unload"));
            }
            return null;
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.debug")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Translate.color("&6Please use tab complete."));
            return true;
        } else if (args[0].equalsIgnoreCase("reload")) {
            player.sendMessage(Translate.color("Reloading all configs might lag."));
            this.customConfigManager.reloadAll();
            player.sendMessage(Translate.color("All configs are reloaded."));
            return true;
        } else if (args[0].equalsIgnoreCase("load")) {
            if (args.length == 1) {
                player.sendMessage(Translate.color("Loading your data. PLEASE WAIT"));
                this.plugin.getPlayerInitializer().unload(player);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        plugin.getPlayerInitializer().load(player);
                        player.sendMessage(Translate.color("Data has been successfully loaded"));
                    }
                }.runTaskLater(plugin, 20);
                return true;
            } else if (args.length == 2 && args[1].equalsIgnoreCase("@all")) {
                player.sendMessage(Translate.color("&6Loading everyones data. PLEASE WAIT"));
                for (Player onlinePlayer : this.plugin.getServer().getOnlinePlayers()) {
                    this.plugin.getPlayerInitializer().load(onlinePlayer);
                    onlinePlayer.sendMessage(Translate.color("Your player data has been &aLOADED&r by [" + player.getDisplayName() + "]"));
                }
                player.sendMessage(Translate.color("&aEveryones player data has been loaded."));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("unload")) {
            if (args.length == 1) {
                player.sendMessage(Translate.color("Unloading your player data PLEASE WAIT."));
                this.plugin.getPlayerInitializer().unload(player);
                player.sendMessage(Translate.color("Your data has been unloaded."));
                return true;
            } else if (args.length == 2 && args[1].equalsIgnoreCase("@all")) {
                player.sendMessage(Translate.color("Unloading everyones player data PLEASE WAIT."));
                for (Player onlinePlayer : this.plugin.getServer().getOnlinePlayers()) {
                    this.plugin.getPlayerInitializer().unload(onlinePlayer);
                    onlinePlayer.sendMessage(Translate.color("Your player data has been &cUNLOADED&r by [" + player.getDisplayName() + "]"));
                }
                player.sendMessage(Translate.color("&aEveryones data has been unloaded."));
                return true;
            }
        }
        return true;
    }
}