package com.andrew121410.mc.world16essentials.commands.playertime;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public class FirstJoinedCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public FirstJoinedCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();
        this.plugin.getCommand("firstjoined").setExecutor(this);
    }

    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only Players Can Use This Command.");
                return true;
            }

            if (!sender.hasPermission("world16.firstjoined")) {
                this.plugin.getApi().sendPermissionErrorMessage(sender);
                return true;
            }

            player.sendMessage(Translate.color("&aFirst joined of &6" + player.getName() + "&a is &6" + this.api.getTimeSinceFirstLogin(player)));
            return true;
        } else if (args.length == 1) {
            if (!sender.hasPermission("world16.firstjoined.other")) {
                this.plugin.getApi().sendPermissionErrorMessage(sender);
                return true;
            }

            OfflinePlayer target = this.plugin.getServer().getOfflinePlayer(args[0]);
            if (!target.hasPlayedBefore()) {
                sender.sendMessage(Translate.color("&cPlayer not found."));
                return true;
            }
            sender.sendMessage(Translate.color("&aFirst joined of &6" + target.getName() + "&a is &6" + this.api.getTimeSinceFirstLogin(target)));
        }
        return true;
    }
}