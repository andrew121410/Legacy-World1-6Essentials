package com.andrew121410.mc.world16essentials.commands.home;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class CloneHomesCMD implements CommandExecutor {

    private final Map<UUID, Map<String, UnlinkedWorldLocation>> homesMap;

    private final World16Essentials plugin;
    private final API api;

    public CloneHomesCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.homesMap = this.plugin.getMemoryHolder().getHomesMap();

        this.plugin.getCommand("clonehomes").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.clonehomes")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Translate.color("&cUsage: /clonehomes <player>"));
            return true;
        } else if (args.length == 1) {
            OfflinePlayer offlinePlayer = this.plugin.getServer().getOfflinePlayer(args[0]);
            if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) {
                player.sendMessage(Translate.color("&cSeems like that player never existed!"));
                return true;
            }

            Map<String, UnlinkedWorldLocation> otherHomes = this.plugin.getHomeManager().loadHomes(offlinePlayer.getUniqueId());
            this.plugin.getHomeManager().add(player, otherHomes);

            player.sendMessage(Translate.color("&6All of " + offlinePlayer.getName() + "'s homes have been cloned to you!"));
        }
        return true;
    }
}