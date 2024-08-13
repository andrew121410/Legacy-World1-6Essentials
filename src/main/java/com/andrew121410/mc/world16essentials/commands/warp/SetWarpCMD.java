package com.andrew121410.mc.world16essentials.commands.warp;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class SetWarpCMD implements CommandExecutor {

    private final Map<String, UnlinkedWorldLocation> warpsMap;

    private final World16Essentials plugin;
    private final API api;

    public SetWarpCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.warpsMap = this.plugin.getMemoryHolder().getWarpsMap();

        this.plugin.getCommand("setwarp").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.setwarp")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 1) {
            String name = args[0].toLowerCase();
            Location location = player.getLocation();

            if (this.warpsMap.containsKey(name)) {
                player.sendMessage(Translate.miniMessage("<red>The warp <blue>" + name + " <red>already exists."));
                return true;
            }

            this.plugin.getWarpManager().add(name, location);
            player.sendMessage(Translate.miniMessage("<dark_green>Warp <blue>" + name + " <dark_green>has been set."));
            return true;
        } else {
            player.sendMessage(Translate.miniMessage("<red>Usage: <gold>/setwarp <blue><Name>"));
        }
        return true;
    }
}