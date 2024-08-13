package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ColorsCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public ColorsCMD(World16Essentials getPlugin) {
        this.plugin = getPlugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("colors").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("world16.colors")) {
            api.sendPermissionErrorMessage(sender);
            return true;
        }

        sender.sendMessage(Translate.color("&ahttps://mctools.org/motd-creator"));
        return true;
    }
}
