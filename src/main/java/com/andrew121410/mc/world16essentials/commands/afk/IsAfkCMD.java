package com.andrew121410.mc.world16essentials.commands.afk;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IsAfkCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public IsAfkCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("isafk").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("world16.isafk")) {
            api.sendPermissionErrorMessage(sender);
            return true;
        }

        if (args.length == 1) {
            Player playerFromArg = this.plugin.getServer().getPlayerExact(args[0]);

            if (playerFromArg == null) {
                sender.sendMessage(Translate.color("&4Unable to find the player " + args[0]));
                return true;
            }

            if (api.isAfk(playerFromArg)) {
                sender.sendMessage(Translate.color("&aThe player " + playerFromArg.getDisplayName() + " is afk!"));
            } else {
                sender.sendMessage(Translate.color("&cThe player " + playerFromArg.getDisplayName() + " is not afk!"));
            }
        } else {
            sender.sendMessage(Translate.color("&cUsage: /isafk <PlayerName>"));
        }
        return true;
    }
}