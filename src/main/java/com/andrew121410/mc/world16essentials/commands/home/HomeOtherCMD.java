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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return null;
        if (!player.hasPermission("world16.homeother")) return null;

        if (args.length == 1) {
            // Get the array of OfflinePlayers
            OfflinePlayer[] playersArray = this.plugin.getServer().getOfflinePlayers();

            // Filter out broken players and collect names into a list
            List<String> offlineNames = Arrays.stream(playersArray)
                    .filter(Objects::nonNull) // Filter out null OfflinePlayers
                    .filter(offlinePlayer -> offlinePlayer.getName() != null) // Filter out players with null names
                    .filter(offlinePlayer -> !offlinePlayer.getName().isEmpty()) // Filter out players with empty names
                    .filter(offlinePlayer -> !offlinePlayer.getName().equals("null")) // Filter out players with "null" as their name
                    .map(OfflinePlayer::getName) // Map to player names
                    .collect(Collectors.toList()); // Collect names into a list

            return TabUtils.getContainsString(args[0], offlineNames);
        } else if (args.length == 2) {
            List<String> homeNames = getHomes(getPlayer(args[0])).keySet().stream().toList();
            return TabUtils.getContainsString(args[1], homeNames);
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
                player.sendMessage(Translate.color("&cSeems like that player never existed!"));
                return true;
            }
            Map<String, UnlinkedWorldLocation> homes = getHomes(offlinePlayer);
            String home = args[1];
            UnlinkedWorldLocation location = homes.getOrDefault(home, null);

            if (location == null) {
                player.sendMessage(Translate.color("&cThat home doesn't exist!"));
                return true;
            }

            if (!location.isWorldLoaded()) {
                player.sendMessage(Translate.color("&cThat home's world is not loaded!"));
                return true;
            }

            player.teleport(location);
            player.sendMessage(Translate.color("&6You have been teleported to " + offlinePlayer.getName() + "'s home named &c" + home));
            return true;
        } else {
            player.sendMessage(Translate.color("&cUsage: /homeother <player> <home>"));
        }
        return true;
    }

    private OfflinePlayer getPlayer(String name) {
        OfflinePlayer offlinePlayer = this.plugin.getServer().getOfflinePlayer(name);
        if (offlinePlayer == null) {
            offlinePlayer = this.plugin.getServer().getOfflinePlayer(name);
        }
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
