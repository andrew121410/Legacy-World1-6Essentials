package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public GamemodeCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("egamemode").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        GameMode gameMode = getGameModeFromAlias(label);
        if (gameMode == null) {
            sender.sendMessage(Translate.color("&cSomething went wrong!"));
            sender.sendMessage(Translate.color("&cDo not use /egamemode use /gma or /gmc or /gms or /gmsp"));
            return true;
        }
        String text = getTextFromGameMode(gameMode);

        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only Players Can Use This Command.");
                return true;
            }

            if (!player.hasPermission("world16." + label)) {
                api.sendPermissionErrorMessage(player);
                return true;
            }

            changeGamemode(player, null, text, gameMode);
            return true;
        } else if (args.length == 1) {
            if (!sender.hasPermission("world16." + label + ".other")) {
                api.sendPermissionErrorMessage(sender);
                return true;
            }

            Player target = plugin.getServer().getPlayerExact(args[0]);
            if (target != null && target.isOnline()) changeGamemode(target, sender, text, gameMode);
            return true;
        } else {
            sender.sendMessage(Translate.miniMessage("<red>Usage: <gold>/" + label + " <player?>"));
        }
        return true;
    }

    private void changeGamemode(Player target, CommandSender sender, String text, GameMode gameMode) {
        String color = target.isOp() ? "&4" : "&7";

        target.setGameMode(gameMode);
        target.sendMessage(Translate.color("&6Set game mode " + text + " for " + color + target.getDisplayName()));
        if (sender != null) {
            sender.sendMessage(Translate.color("&6Set game mode " + text + " for " + color + target.getDisplayName()));
        }
    }

    private GameMode getGameModeFromAlias(String label) {
        return switch (label.toLowerCase()) {
            case "gma" -> GameMode.ADVENTURE;
            case "gmc" -> GameMode.CREATIVE;
            case "gms" -> GameMode.SURVIVAL;
            case "gmsp" -> GameMode.SPECTATOR;
            default -> null;
        };
    }

    private String getTextFromGameMode(GameMode gameMode) {
        return switch (gameMode) {
            case ADVENTURE -> "&cadventure&6";
            case CREATIVE -> "&ccreative&6";
            case SURVIVAL -> "&csurvival&6";
            case SPECTATOR -> "&cspectator&6";
        };
    }
}
