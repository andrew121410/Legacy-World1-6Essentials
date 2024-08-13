package com.andrew121410.mc.world16essentials.commands.msg;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class ReplyCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public ReplyCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("reply").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.msg") && !player.hasPermission("world16.emsg")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length >= 1) {
            UUID targetUUID = this.plugin.getMemoryHolder().getLastPlayerToMessageMap().get(player.getUniqueId());
            if (targetUUID == null) {
                player.sendMessage(Translate.color("&cYou have no one to reply to."));
                return true;
            }

            Player target = this.plugin.getServer().getPlayer(targetUUID);
            if (target == null && !target.isOnline()) {
                player.sendMessage(Translate.color("&cThat player is not online."));
                return true;
            }
            // Add to last player to message map, so we can use /reply command.
            addToLastPlayerToMessage(player, target);

            String messageFrom = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
            player.sendMessage(Translate.color("&2[&a{me} &6->&c {target}&2]&9 ->&r {message}").replace("{me}", "me").replace("{target}", target.getName()).replace("{message}", messageFrom));
            target.sendMessage(Translate.color("&2[&a{me} &6->&c {target}&2]&9 ->&r {message}").replace("{me}", player.getName()).replace("{target}", "me").replace("{message}", messageFrom));
        } else {
            player.sendMessage(Translate.color("&cUsage: /reply <Message>"));
        }
        return true;
    }

    private void addToLastPlayerToMessage(Player player, Player target) {
        this.plugin.getMemoryHolder().getLastPlayerToMessageMap().remove(target.getUniqueId());
        this.plugin.getMemoryHolder().getLastPlayerToMessageMap().put(target.getUniqueId(), player.getUniqueId());
    }
}