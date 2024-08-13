package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.player.PlayerUtils;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import com.andrew121410.mc.world16utils.utils.Utils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SpawnMobCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public SpawnMobCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("spawnmob").setExecutor(this);
        this.plugin.getCommand("spawnmob").setTabCompleter((sender, command, s, args) -> {
            if (!sender.hasPermission("world16.spawnmob")) return null;

            if (args.length == 1) {
                return TabUtils.getContainsString(args[0], Arrays.stream(EntityType.values()).map(EntityType::name).toList());
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

        if (!player.hasPermission("world16.spawnmob")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length >= 1) {
            Location location = PlayerUtils.getBlockPlayerIsLookingAt(player).getLocation().add(0, 1, 0);

            int amount = 1;
            if (args.length == 2) {
                amount = Utils.asIntegerOrElse(args[1], 1);
            }

            EntityType entityType = null;
            try {
                entityType = EntityType.valueOf(args[0].toUpperCase());
            } catch (IllegalArgumentException ignored) {
            }

            if (entityType == null) {
                player.sendMessage(Translate.color("&cInvalid Entity Type."));
                return true;
            }

            if (amount > api.getConfigUtils().getSpawnMobCap()) {
                player.sendMessage(Translate.color("&cYou can only spawn &e" + api.getConfigUtils().getSpawnMobCap() + " &centities at a time."));
                return true;
            }

            for (int i = 0; i < amount; i++) {
                player.getWorld().spawnEntity(location, entityType);
            }

            player.sendMessage(Translate.color("&aSpawned &e" + amount + " &a" + entityType.name() + "&a."));
        } else {
            player.sendMessage(Translate.color("&cUsage: &6/spawnmob <Entity> <Amount>"));
        }
        return true;
    }
}
