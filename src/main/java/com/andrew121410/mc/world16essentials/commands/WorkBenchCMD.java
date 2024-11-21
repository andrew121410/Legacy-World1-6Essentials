package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorkBenchCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public WorkBenchCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("workbench").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only Players Can Use This Command.");
                return true;
            }

            if (!player.hasPermission("world16.workbench")) {
                api.sendPermissionErrorMessage(player);
                return true;
            }

            openWorkbench(player, null);
            return true;
        } else if (args.length == 1) {
            if (!sender.hasPermission("world16.workbench.other")) {
                api.sendPermissionErrorMessage(sender);
                return true;
            }

            Player target = this.plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(Translate.miniMessage("<red>Player is not online."));
                return true;
            }

            openWorkbench(target, sender);
            return true;
        }
        return true;
    }

    private void openWorkbench(Player target, CommandSender sender) {
        target.openWorkbench(null, true);
        target.sendMessage(Translate.miniMessage("<gold>Opening Workbench..."));
        if (sender != null) {
            sender.sendMessage(Translate.miniMessage("<gold>Opening Workbench for <white>" + target.getName() + "<gold>..."));
        }
    }
}