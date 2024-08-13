package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RenderInfoCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public RenderInfoCMD(World16Essentials plugin) {
        this.plugin = plugin;

        this.api = this.plugin.getApi();
        this.plugin.getCommand("renderinfo").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("world16.renderinfo")) {
            api.sendPermissionErrorMessage(sender);
            return true;
        }

        String serverRenderDistance = String.valueOf(sender.getServer().getViewDistance());

        sender.sendMessage(Translate.miniMessage("<gradient:blue:green>Server Render Distance: <white:blue>" + serverRenderDistance));
        return true;
    }
}
