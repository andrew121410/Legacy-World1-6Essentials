package com.andrew121410.mc.world16essentials.commands.home;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.sharedtabcomplete.HomeListTab;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class DelhomeCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public DelhomeCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("delhome").setExecutor(this);
        this.plugin.getCommand("delhome").setTabCompleter(new HomeListTab(this.plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.home")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        String homeName = "home";
        if (args.length == 1) {
            homeName = args[0].toLowerCase();

            if (homeName.equalsIgnoreCase("@allHomes")) {
                this.plugin.getHomeManager().deleteAllHomes(player.getUniqueId());
                player.sendMessage(Translate.color("&6Deleted All Homes."));
                return true;
            }
        }

        Map<String, UnlinkedWorldLocation> homes = this.plugin.getMemoryHolder().getHomesMap().get(player.getUniqueId());
        if (homes.containsKey(homeName)) {
            this.plugin.getHomeManager().delete(player.getUniqueId(), homeName);
            player.sendMessage(Translate.color("&6Home &c" + homeName + " &6has been deleted."));
        } else {
            player.sendMessage(Translate.color("&cThat home doesn't exist."));
        }
        return true;
    }
}