package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public FeedCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("feed").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only Players Can Use This Command.");
                return true;
            }

            if (!player.hasPermission("world16.feed")) {
                api.sendPermissionErrorMessage(player);
                return true;
            }

            doFeed(player, null);
            return true;
        } else if (args.length == 1) {
            if (!sender.hasPermission("world16.feed.other")) {
                api.sendPermissionErrorMessage(sender);
                return true;
            }

            Player target = plugin.getServer().getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                sender.sendMessage(Translate.color("&cThat player is not online."));
                return true;
            }

            doFeed(target, sender);
            return true;
        } else {
            sender.sendMessage(Translate.color("&cUsage: /feed or /feed <player>"));
        }
        return true;
    }

    private void doFeed(Player target, CommandSender sender) {
        target.setFoodLevel(20);
        target.sendMessage(Translate.color("&6You have been fed."));
        String color = target.isOp() ? "&4" : "&7";
        if (sender != null) {
            sender.sendMessage(Translate.color("&6You have fed " + color + target.getName()));
        }
    }
}