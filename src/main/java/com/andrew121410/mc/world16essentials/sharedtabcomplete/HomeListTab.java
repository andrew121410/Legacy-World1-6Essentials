package com.andrew121410.mc.world16essentials.sharedtabcomplete;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class HomeListTab implements TabCompleter {

    private final Map<UUID, Map<String, UnlinkedWorldLocation>> rawHomesMap;

    private final World16Essentials plugin;

    public HomeListTab(World16Essentials plugin) {
        this.plugin = plugin;
        this.rawHomesMap = this.plugin.getMemoryHolder().getHomesMap();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alies, String[] args) {
        if (!(sender instanceof Player player)) return null;
        if (!player.hasPermission("world16.home")) return null;

        if (args.length == 1) {
            if (rawHomesMap.get(player.getUniqueId()) == null) {
                player.kickPlayer("[HomeTabComplete] You where not in the memory so NPE was caused.");
                return null;
            }
            Set<String> homeSet = rawHomesMap.get(player.getUniqueId()).keySet();
            String[] homeString = homeSet.toArray(new String[0]);
            return TabUtils.getContainsString(args[0], Arrays.asList(homeString));
        }
        return null;
    }
}