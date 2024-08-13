package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UUIDCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public UUIDCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("uuid").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only Players Can Use This Command.");
                return true;
            }

            if (!player.hasPermission("world16.uuid")) {
                api.sendPermissionErrorMessage(player);
                return true;
            }

            BaseComponent[] components = new ComponentBuilder("Your uuid is ").color(net.md_5.bungee.api.ChatColor.GREEN)
                    .append(player.getUniqueId().toString()).color(net.md_5.bungee.api.ChatColor.RESET)
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, player.getUniqueId().toString()))
                    .create();

            player.spigot().sendMessage(components);

            return true;
        } else if (args.length == 1) {
            if (!sender.hasPermission("world16.uuid.other")) {
                api.sendPermissionErrorMessage(sender);
                return true;
            }

            OfflinePlayer target = plugin.getServer().getOfflinePlayer(args[0]);

            BaseComponent[] components = new ComponentBuilder(target.getName() + "'s uuid is ").color(net.md_5.bungee.api.ChatColor.GREEN)
                    .append(target.getUniqueId().toString()).color(net.md_5.bungee.api.ChatColor.RESET)
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, target.getUniqueId().toString()))
                    .create();

            sender.spigot().sendMessage(components);

            // Send a message if that player has never joined the server.
            if (!target.hasPlayedBefore()) {
                sender.sendMessage(Translate.color("&cThe player &e" + args[0] + " &chas never joined the server."));
            }
            return true;
        } else {
            sender.sendMessage(Translate.color("&cUsage: /uuid or /uuid <player>"));
        }
        return true;
    }
}