package com.andrew121410.mc.world16essentials.commands.home;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public SetHomeCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("sethome").setExecutor(this);
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

        int currentHomes = this.plugin.getMemoryHolder().getHomesMap().get(player.getUniqueId()).size();
        int maxHomes = this.plugin.getHomeManager().getMaximumHomeCount(player);
        if (!player.isOp() && currentHomes >= maxHomes && maxHomes != -1) {
            player.sendMessage(Translate.color("&cYou have reached the maximum amount of homes."));
            player.sendMessage(Translate.color("&6The maximum amount of homes is &c" + maxHomes));
            return true;
        }

        String homeName = "home";
        if (args.length == 1) {
            homeName = args[0].toLowerCase();
        }

        this.plugin.getHomeManager().add(player, homeName, new UnlinkedWorldLocation(player.getLocation()));

        player.sendMessage(Translate.color("&6New Home: &c" + homeName + "&6 set to your current location."));
        return true;
    }
}