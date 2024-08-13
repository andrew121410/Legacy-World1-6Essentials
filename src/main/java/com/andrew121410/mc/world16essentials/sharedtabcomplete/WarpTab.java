package com.andrew121410.mc.world16essentials.sharedtabcomplete;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WarpTab implements TabCompleter {

    private final Map<String, UnlinkedWorldLocation> warpsMap;

    private final World16Essentials plugin;

    public WarpTab(World16Essentials plugin) {
        this.plugin = plugin;
        this.warpsMap = this.plugin.getMemoryHolder().getWarpsMap();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alies, String[] args) {
        if (!(sender instanceof Player player)) return null;
        if (!player.hasPermission("world16.warp")) return null;

        List<String> warpNames = new ArrayList<>(this.warpsMap.keySet());

        // Remove the ones that player doesn't have permission for.
        warpNames.removeIf(s -> !player.hasPermission("world16.warp." + s));

        if (args.length == 1) return TabUtils.getContainsString(args[0], warpNames);
        return null;
    }
}
