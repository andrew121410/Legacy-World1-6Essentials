package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class EChestCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public EChestCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("echest").setExecutor(this);
        this.plugin.getCommand("echest").setTabCompleter((sender, command, alias, args) -> {
            if (args.length == 1) {
                return TabUtils.getContainsString(args[0], Arrays.asList("view", "open", "help"));
            }
            return null;
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only Players Can Use This Command.");
                return true;
            }

            if (!player.hasPermission("world16.echest")) {
                api.sendPermissionErrorMessage(player);
                return true;
            }

            openEChest(player, null);
            return true;
        } else if (args.length == 2) {
            String subcommand = args[0].toLowerCase();
            String playerName = args[1];

            Player target = this.plugin.getServer().getPlayer(playerName);
            if (target == null) {
                sender.sendMessage(Translate.miniMessage("<red>Player is not online."));
                return true;
            }

            // Handle "/echest view <player>"
            if (subcommand.equals("view")) {
                if (!sender.hasPermission("world16.echest.view")) {
                    api.sendPermissionErrorMessage(sender);
                    return true;
                }

                viewEChest(sender, target);
                return true;
            }

            // Handle "/echest open <player>"
            if (subcommand.equals("open")) {
                if (!sender.hasPermission("world16.echest.open")) {
                    api.sendPermissionErrorMessage(sender);
                    return true;
                }

                openEChest(target, sender);
                return true;
            }
        }
        showHelp(sender);
        return true;
    }

    private void openEChest(Player target, CommandSender sender) {
        target.openInventory(target.getEnderChest());
        target.playSound(target.getLocation(), Sound.BLOCK_ENDERCHEST_OPEN, 10.0f, 1.0f);
        target.sendMessage(Translate.miniMessage("<dark_purple>Opening Ender Chest..."));
        if (sender != null) {
            sender.sendMessage(Translate.miniMessage("<dark_purple>Opening Ender Chest for <white>" + target.getName()));
        }
    }

    private void viewEChest(CommandSender sender, Player target) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Translate.miniMessage("<red>Only players can view Ender Chests."));
            return;
        }

        player.openInventory(target.getEnderChest());
        player.playSound(player.getLocation(), Sound.BLOCK_ENDERCHEST_OPEN, 10.0f, 1.0f);
        player.sendMessage(Translate.miniMessage("<dark_purple>Viewing <white>" + target.getName() + "'s <dark_purple>Ender Chest..."));
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage(Translate.miniMessage("""
                <gold>==== Ender Chest Help ====
                <yellow>/echest</yellow> - Opens your own Ender Chest.
                <yellow>/echest view <player></yellow> - View another player's Ender Chest.
                <yellow>/echest open <player></yellow> - Open another player's Ender Chest for them.
                <yellow>/echest help</yellow> - Shows this help menu.
                """));
    }
}