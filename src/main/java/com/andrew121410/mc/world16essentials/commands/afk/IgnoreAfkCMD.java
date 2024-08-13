package com.andrew121410.mc.world16essentials.commands.afk;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.objects.AfkObject;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IgnoreAfkCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public IgnoreAfkCMD(World16Essentials getPlugin) {
        this.plugin = getPlugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("ignoreafk").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("world16.ignoreafk")) {
            api.sendPermissionErrorMessage(sender);
            return true;
        }

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Translate.miniMessage("<red>Consoles can do /ignoreafk <player>."));
                return true;
            }

            handleIgnoreAfk(sender, null);
        } else if (args.length == 1) { // /ignoreafk <player>
            if (!sender.hasPermission("world16.ignoreafk.other")) {
                api.sendPermissionErrorMessage(sender);
                return true;
            }

            Player target = this.plugin.getServer().getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage(Translate.miniMessage("<red>Player not found."));
                return true;
            }
            handleIgnoreAfk(sender, target);
        } else {
            sender.sendMessage(Translate.miniMessage("<red>Usage: /ignoreafk <optional:player>"));
        }
        return true;
    }

    private void handleIgnoreAfk(CommandSender sender, Player target) {
        AfkObject afkObject;
        if (target == null) {
            Player player = (Player) sender;
            afkObject = this.plugin.getMemoryHolder().getAfkMap().get(player.getUniqueId());
        } else {
            afkObject = this.plugin.getMemoryHolder().getAfkMap().get(target.getUniqueId());
        }

        // Should never be null. But just in case.
        if (afkObject == null) {
            sender.sendMessage(Translate.miniMessage("<red>Something went wrong."));
            return;
        }

        if (afkObject.isIgnore()) {
            afkObject.setIgnore(false);

            if (target != null) {
                target.sendMessage(Translate.miniMessage("<green>Your AFK Status is no longer being ignored."));
                sender.sendMessage(Translate.miniMessage("<green>" + target.getName() + " AFK Status is no longer being ignored."));
            } else {
                sender.sendMessage(Translate.miniMessage("<green>Your AFK Status is no longer being ignored."));
            }
        } else {
            afkObject.setIgnore(true);

            if (target != null) {
                target.sendMessage(Translate.miniMessage("<red>Your AFK Status is now being ignored."));
                sender.sendMessage(Translate.miniMessage("<red>" + target.getName() + " AFK Status is now being ignored."));
            } else {
                sender.sendMessage(Translate.miniMessage("<red>Your AFK Status is now being ignored."));
            }
        }
    }
}