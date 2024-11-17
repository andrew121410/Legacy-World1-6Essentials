package com.andrew121410.mc.world16essentials.commands.spawn;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.config.CustomConfigManager;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.config.CustomYmlManager;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    private final CustomYmlManager shitYml;

    public SpawnCMD(World16Essentials plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.shitYml = customConfigManager.getShitYml();
        this.api = this.plugin.getApi();

        this.plugin.getCommand("spawn").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        UnlinkedWorldLocation spawn = this.api.getLocationFromFile(this.shitYml, "Spawn.default");
        if (spawn == null) {
            Location defaultSpawn = this.plugin.getServer().getWorlds().getFirst().getSpawnLocation();
            UnlinkedWorldLocation unlinkedWorldLocation = new UnlinkedWorldLocation(defaultSpawn);
            this.api.setLocationToFile(this.shitYml, "Spawn.default", unlinkedWorldLocation);
            spawn = unlinkedWorldLocation;
        }

        if (!player.hasPermission("world16.spawn")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (!spawn.isWorldLoaded()) {
            player.sendMessage(Translate.miniMessage("<red>The spawn location is not loaded."));
            player.sendMessage(Translate.miniMessage("<yellow>This happens if the world is not loaded."));
            return true;
        }

        if (args.length == 0) {
            player.teleport(spawn);
            player.sendMessage(Translate.color("&6Teleporting..."));
            return true;
        } else if (args.length == 1) {
            if (!player.hasPermission("world16.spawn.other")) {
                api.sendPermissionErrorMessage(player);
                return true;
            }
            Player target = plugin.getServer().getPlayerExact(args[0]);
            if (target != null && target.isOnline()) {
                target.teleport(spawn);
                target.sendMessage(Translate.color("&6Teleporting..."));
            }
            return true;
        } else {
            player.sendMessage(Translate.color("&cUsage: for yourself do /spawn OR /spawn <Player>"));
        }
        return true;
    }
}