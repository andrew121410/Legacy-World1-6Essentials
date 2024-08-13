package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class HealCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public HealCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("heal").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only Players Can Use This Command.");
                return true;
            }

            if (!player.hasPermission("world16.heal")) {
                api.sendPermissionErrorMessage(player);
                return true;
            }

            doHeal(player, null);
            return true;
        } else if (args.length == 1) {
            if (!sender.hasPermission("world16.heal.other")) {
                api.sendPermissionErrorMessage(sender);
                return true;
            }

            Player target = plugin.getServer().getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                sender.sendMessage(Translate.color("&cThat player is not online."));
                return true;
            }

            doHeal(target, sender);
            return true;
        } else {
            sender.sendMessage(Translate.color("&cUsage: for yourself do /heal OR /heal <Player>"));
        }
        return true;
    }

    private void doHeal(Player target, CommandSender sender) {
        target.setHealth(20.0D);
        target.setFoodLevel(20);
        target.setFireTicks(0);
        for (PotionEffect effect : target.getActivePotionEffects()) target.removePotionEffect(effect.getType());

        target.sendMessage(Translate.color("&6You have been healed."));
        if (sender != null) {
            String color = target.isOp() ? "&4" : "&7";
            sender.sendMessage(Translate.color("&6You have healed " + color + target.getName()));
        }
    }
}
