package com.andrew121410.mc.world16essentials.commands.home;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class HomeOtherCMD implements CommandExecutor, TabExecutor {

    private World16Essentials plugin;
    private API api;

    public HomeOtherCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("homeother").setExecutor(this);
        this.plugin.getCommand("homeother").setTabCompleter(this);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return null;
        if (!player.hasPermission("world16.homeother")) return null;

        if (args.length == 1 && this.plugin.getApi().getConfigUtils().isOfflinePlayersTabCompletion()) {
            return TabUtils.getContainsString(args[0], TabUtils.getOfflinePlayerNames(this.plugin.getServer().getOfflinePlayers()));
        } else if (args.length == 2) {
            List<String> homeNames = getHomes(getPlayer(args[0])).keySet().stream().toList();
            return TabUtils.getContainsString(args[1], homeNames);
        }
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.homeother")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 2) {
            OfflinePlayer offlinePlayer = getPlayer(args[0]);
            if (!offlinePlayer.hasPlayedBefore()) {
                player.sendMessage(Translate.colorc("&cSeems like that player never existed!"));
                return true;
            }
            Map<String, UnlinkedWorldLocation> homes = getHomes(offlinePlayer);
            String home = args[1];
            UnlinkedWorldLocation location = homes.getOrDefault(home, null);

            if (location == null) {
                player.sendMessage(Translate.colorc("&cThat home doesn't exist!"));
                return true;
            }

            if (!location.isWorldLoaded()) {
                player.sendMessage(Translate.colorc("&cThat home's world is not loaded!"));
                return true;
            }

            player.teleport(location);
            player.sendMessage(Translate.colorc("&6You have been teleported to " + offlinePlayer.getName() + "'s home named &c" + home));
            return true;
        } else {
            player.sendMessage(Translate.colorc("&cUsage: /homeother <player> <home>"));
        }
        return true;
    }

    private OfflinePlayer getPlayer(String name) {
        OfflinePlayer offlinePlayer = this.plugin.getServer().getOfflinePlayer(name);
        return offlinePlayer;
    }

    private Map<String, UnlinkedWorldLocation> getHomes(OfflinePlayer offlinePlayer) {
        Map<String, UnlinkedWorldLocation> homes;
        if (this.plugin.getMemoryHolder().getHomesMap().containsKey(offlinePlayer.getUniqueId())) {
            homes = this.plugin.getMemoryHolder().getHomesMap().get(offlinePlayer.getUniqueId());
        } else {
            homes = this.plugin.getHomeManager().loadHomes(offlinePlayer.getUniqueId());
        }
        return homes;
    }
}