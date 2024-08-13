package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SudoCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public SudoCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("sudo").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("world16.sudo")) {
            api.sendPermissionErrorMessage(sender);
            return true;
        }

        if (args.length >= 2) {
            Player target = this.plugin.getServer().getPlayer(args[0]);
            if (target != null && target.isOnline()) sudoCommand(target, sender, args);
            else sender.sendMessage(Translate.miniMessage("<dark_red>Player is not online!"));
            return true;
        } else {
            sender.sendMessage(Translate.miniMessage("<red>Usage: <gold>/sudo <player> <command>"));
        }
        return true;
    }

    private boolean sudoCommand(Player target, CommandSender sender, String[] args) {
        String[] commandArray = Arrays.copyOfRange(args, 1, args.length);

        // Remove the slash if it's there.
        if (commandArray[0].contains("/")) commandArray[0] = commandArray[0].replace("/", "");
        String command = String.join(" ", commandArray);

        this.plugin.getServer().dispatchCommand(target, command);
        sender.sendMessage(Translate.miniMessage("<dark_aqua>Command <gold>" + command + " <dark_aqua>has been executed as <gold>" + target.getName()));
        return true;
    }
}
