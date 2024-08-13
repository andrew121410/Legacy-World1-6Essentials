package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CommandBlockFindCMD implements CommandExecutor {

    private final List<String> spyCommandBlock;

    private final World16Essentials plugin;
    private final API api;

    public CommandBlockFindCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.spyCommandBlock = this.plugin.getMemoryHolder().getSpyCommandBlock();

        this.plugin.getCommand("commandblockfind").setExecutor(this);
        this.plugin.getCommand("commandblockfind").setTabCompleter((sender, command, alias, args) -> {
            if (!(sender instanceof Player player)) return null;
            if (!player.hasPermission("world16.commandblockfind")) return null;

            if (args.length == 1) {
                return Arrays.asList("add", "remove");
            } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
                return TabUtils.getContainsString(args[1], this.spyCommandBlock);
            }
            return null;
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("world16.commandblockfind")) {
            api.sendPermissionErrorMessage(sender);
            return true;
        }

        if (args.length >= 2) {
            String addOrRemove = args[0];
            String string = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

            if (addOrRemove.equalsIgnoreCase("add")) {
                spyCommandBlock.add(string);
                sender.sendMessage(Translate.color("&eWill now spy on command blocks with the command &6" + string + "&e."));
                return true;
            } else if (addOrRemove.equalsIgnoreCase("remove") || addOrRemove.equalsIgnoreCase("delete")) {
                spyCommandBlock.remove(string);
                sender.sendMessage(Translate.color("&eWill no longer spy on command blocks with the command &6" + string + "&e."));
                return true;
            }
        }
        sender.sendMessage(Translate.color("&cUsage: /commandblockfind <add/remove> <string>"));
        return true;
    }
}