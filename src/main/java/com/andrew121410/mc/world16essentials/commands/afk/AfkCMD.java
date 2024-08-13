package com.andrew121410.mc.world16essentials.commands.afk;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AfkCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public AfkCMD(World16Essentials getPlugin) {
        this.plugin = getPlugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("afk").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.afk")) {
            api.sendPermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            api.doAfk(p, null, true);
            return true;
        } else if (args.length == 1) {
            if (!p.hasPermission("world16.afk.other")) {
                api.sendPermissionErrorMessage(p);
                return true;
            }
            Player target = plugin.getServer().getPlayerExact(args[0]);
            if (target != null && target.isOnline()) {
                api.doAfk(target, null, true);
            }
            return true;
        } else {
            p.sendMessage(Translate.color("&cUsage:&9 /afk &aOR &9/afk <Player>"));
        }
        return true;
    }
}