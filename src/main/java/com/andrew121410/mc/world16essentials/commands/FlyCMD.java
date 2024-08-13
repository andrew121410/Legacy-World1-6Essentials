package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public FlyCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("fly").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only Players Can Use This Command.");
                return true;
            }

            if (!player.hasPermission("world16.fly")) {
                api.sendPermissionErrorMessage(player);
                return true;
            }

            doFly(player, null);
            return true;
        } else if (args.length == 1) {
            if (!sender.hasPermission("world16.fly.other")) {
                api.sendPermissionErrorMessage(sender);
                return true;
            }

            Player target = plugin.getServer().getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                sender.sendMessage(Translate.color("&cThat player is not online."));
                return true;
            }

            doFly(target, sender);
            return true;
        }
        return true;
    }

    private void doFly(Player target, CommandSender sender) {
        String color = target.isOp() ? "&4" : "&7";
        if (!target.getAllowFlight()) {
            target.setAllowFlight(true);
            target.sendMessage(Translate.color("&6Set fly mode &cenabled&6 for " + color + target.getName()));
            if (sender != null) {
                sender.sendMessage(Translate.color("&6Set fly mode &cenabled&6 for " + color + target.getName()));
            }
        } else if (target.getAllowFlight()) {
            target.setAllowFlight(false);
            target.sendMessage(Translate.color("&6Set fly mode &cdisabled&6 for " + color + target.getName()));
            if (sender != null) {
                sender.sendMessage(Translate.color("&6Set fly mode &cdisabled&6 for " + color + target.getName()));
            }
        }
    }
}