package com.andrew121410.mc.world16essentials.commands.back;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class BackCMD implements CommandExecutor {

    private final Map<UUID, Map<BackEnum, UnlinkedWorldLocation>> backMap;

    private final World16Essentials plugin;
    private final API api;

    public BackCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();
        this.backMap = this.plugin.getMemoryHolder().getBackMap();

        this.plugin.getCommand("back").setExecutor(this);
        this.plugin.getCommand("back").setTabCompleter((sender, command, s, args) -> {
            if (!(sender instanceof Player player)) return null;
            if (!player.hasPermission("world16.back")) return null;

            if (args.length == 1) {
                return TabUtils.getContainsString(args[0], Arrays.asList("death", "tp"));
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

        if (!player.hasPermission("world16.back")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }
        Map<BackEnum, UnlinkedWorldLocation> playerBackMap = this.backMap.get(player.getUniqueId());

        if (args.length == 1 && args[0].equalsIgnoreCase("death")) {
            if (!player.hasPermission("world16.back.death")) {
                api.sendPermissionErrorMessage(player);
                return true;
            }

            UnlinkedWorldLocation deathLocation = playerBackMap.get(BackEnum.DEATH);
            if (deathLocation == null) {
                player.sendMessage(Translate.color("&4No death back location was found..."));
                return true;
            }
//            if (!deathLocation.isWorldLoaded()) {
//                player.sendMessage(Translate.miniMessage("<red>World is not loaded..."));
//                return true;
//            }

            player.teleport(deathLocation);
            player.sendMessage(Translate.color("&6Teleporting..."));
        } else if (args.length == 1 && args[0].equalsIgnoreCase("tp")) {
            if (!player.hasPermission("world16.back.tp")) {
                api.sendPermissionErrorMessage(player);
                return true;
            }
            UnlinkedWorldLocation tpLocation = playerBackMap.get(BackEnum.TELEPORT);
            if (tpLocation == null) {
                player.sendMessage(Translate.color("&4No tp back location was found..."));
                return true;
            }
//            if (!tpLocation.isWorldLoaded()) {
//                player.sendMessage(Translate.miniMessage("<red>World is not loaded..."));
//                return true;
//            }
            player.teleport(tpLocation);
            player.sendMessage(Translate.color("&6Teleporting..."));
        } else {
            player.sendMessage(Translate.color("&6/back death"));
            player.sendMessage(Translate.color("&6/back tp"));
        }
        return true;
    }
}