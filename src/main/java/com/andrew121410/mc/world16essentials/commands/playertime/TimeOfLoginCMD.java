package com.andrew121410.mc.world16essentials.commands.playertime;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.CommandExecutor;

public class TimeOfLoginCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public TimeOfLoginCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();
        this.plugin.getCommand("timeoflogin").setExecutor(this);
    }

    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof org.bukkit.entity.Player player)) {
                sender.sendMessage("This command can only be run by a player.");
                return false;
            }

            if (!player.hasPermission("world16.timeoflogin")) {
                this.plugin.getApi().sendPermissionErrorMessage(player);
                return true;
            }

            player.sendMessage(Translate.color("&aTime of login of &6" + player.getName() + "&a is &6" + this.api.getTimeSinceLogin(player)));
            return true;
        } else if (args.length == 1) {
            if (!sender.hasPermission("world16.timeoflogin.other")) {
                this.plugin.getApi().sendPermissionErrorMessage(sender);
                return true;
            }

            org.bukkit.entity.Player target = this.plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(Translate.color("&cPlayer not found."));
                return true;
            }
            sender.sendMessage(Translate.color("&aTime of login of &6" + target.getName() + "&a is &6" + this.api.getTimeSinceLogin(target)));
        }
        return true;
    }
}
