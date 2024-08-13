package com.andrew121410.mc.world16essentials.commands.warp;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.managers.WarpManager;
import com.andrew121410.mc.world16essentials.sharedtabcomplete.WarpTab;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class DelwarpCMD implements CommandExecutor {

    private final Map<String, UnlinkedWorldLocation> warpsMap;

    private final World16Essentials plugin;
    private final API api;
    private final WarpManager warpManager;

    public DelwarpCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();
        this.warpManager = this.plugin.getWarpManager();

        this.warpsMap = this.plugin.getMemoryHolder().getWarpsMap();

        this.plugin.getCommand("delwarp").setExecutor(this);
        this.plugin.getCommand("delwarp").setTabCompleter(new WarpTab(this.plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.delwarp")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Translate.color("&cUsage: &6/delwarp <Name>"));
            return true;
        } else if (args.length == 1) {
            String name = args[0].toLowerCase();

            if (!this.warpsMap.containsKey(name)) {
                player.sendMessage(Translate.color("&cThat's not a warp."));
                return true;
            }

            this.warpManager.delete(name);
            player.sendMessage(Translate.color("&eThe warp: " + name + " has been deleted."));
            return true;
        }
        return true;
    }
}