package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class KeepSpawnLoaded implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public KeepSpawnLoaded(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("keepspawnloaded").setExecutor(this);
        this.plugin.getCommand("keepspawnloaded").setTabCompleter((commandSender, command, s, strings) -> {
            if (!(commandSender instanceof Player player)) return null;
            if (!player.hasPermission("world16.keepspawnloaded")) return null;

            if (strings.length == 1) {
                return Arrays.asList("true", "false");
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

        if (!player.hasPermission("world16.keepspawnloaded")) {
            this.api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 1) {
            String string = args[0];
            if (string.equalsIgnoreCase("true")) {

                // Set the world to keep the spawn chunks loaded, and save it to the config.
                player.getWorld().setKeepSpawnInMemory(true);
                this.plugin.getCustomConfigManager().getShitYml().getConfig().set("Worlds." + player.getWorld().getName() + ".ShouldKeepSpawnInMemory", "true");
                this.plugin.getCustomConfigManager().getShitYml().saveConfig();

                player.sendMessage(Translate.color("&aSpawn chunks will stay loaded if nobody is in them now."));
                return true;
            } else if (string.equalsIgnoreCase("false")) {

                // Set the world to not keep the spawn chunks loaded, and save it to the config.
                player.getWorld().setKeepSpawnInMemory(false);
                this.plugin.getCustomConfigManager().getShitYml().getConfig().set("Worlds." + player.getWorld().getName() + ".ShouldKeepSpawnInMemory", "false");
                this.plugin.getCustomConfigManager().getShitYml().saveConfig();

                player.sendMessage(Translate.color("&cSpawn chunks will not stay loaded if nobody is in them now."));
                return true;
            }
        }

        player.sendMessage(Translate.color("&6Current keep spawn loaded value: <reset>" + player.getWorld().getKeepSpawnInMemory()));
        player.sendMessage(Translate.color("&cUsage: &6/keepspawnloaded <true/false>"));
        return true;
    }
}