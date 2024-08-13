package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BroadcastCMD implements CommandExecutor {

    private final World16Essentials plugin;

    private final API api;

    public BroadcastCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("broadcast").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("world16.broadcast")) {
            api.sendPermissionErrorMessage(sender);
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(Translate.color("&cUsage: /broadcast <Message>"));
        } else {
            this.plugin.getServer().getOnlinePlayers().forEach(player1 -> player1.sendMessage(Translate.color("[&c&lBroadcast&r]&a {messager}").replace("{messager}", String.join(" ", args))));
        }
        return true;
    }
}