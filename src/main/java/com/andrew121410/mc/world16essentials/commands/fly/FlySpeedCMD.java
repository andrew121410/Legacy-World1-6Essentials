package com.andrew121410.mc.world16essentials.commands.fly;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class FlySpeedCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public FlySpeedCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("fs").setExecutor(this);
        this.plugin.getCommand("fs").setTabCompleter((commandSender, command, s, args) -> {
            if (args.length == 1) {
                return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
            }
            return null;
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only Players Can Use This Command.");
                return true;
            }

            if (!player.hasPermission("world16.fs")) {
                api.sendPermissionErrorMessage(player);
                return true;
            }

            Double theDouble = Utils.asDoubleOrElse(args[0], null);
            if (theDouble == null) {
                player.sendMessage(Translate.color("&cThat is not a valid number."));
                return true;
            }

            changeFlySpeed(player, null, theDouble);
            return true;
        } else if (args.length == 2) {
            if (!sender.hasPermission("world16.fs.other")) {
                api.sendPermissionErrorMessage(sender);
                return true;
            }

            Player target = this.plugin.getServer().getPlayer(args[0]);
            if (target == null || !target.isOnline()) {
                sender.sendMessage(Translate.color("&cThat player is not online."));
                return true;
            }

            Double theDouble = Utils.asDoubleOrElse(args[1], null);

            if (theDouble == null) {
                sender.sendMessage(Translate.color("&cThat is not a valid number."));
                return true;
            }

            changeFlySpeed(target, sender, theDouble);
            return true;
        } else {
            sender.sendMessage(Translate.color("&4Usage: &9/fs <&aNumber&9> OR /fs <&cPlayer&9> <&aNumber&9>"));
            sender.sendMessage(Translate.color("&6Remember that the default flight speed is &a1"));
        }
        return true;
    }

    private void changeFlySpeed(Player target, CommandSender sender, double speedDouble) {
        if ((speedDouble > -1) && (speedDouble < 11)) {
            float flySpeed = (float) (speedDouble / 10.0D);
            target.setFlySpeed(flySpeed);
            target.sendMessage(Translate.color("&6Your fly speed has been set to &a" + speedDouble));
            if (sender != null) {
                sender.sendMessage(Translate.color("&eYou has have set " + target.getName() + " flight speed to &a" + speedDouble));
            }
        } else {
            if (sender == null) {
                target.sendMessage(Translate.color("&cYour fly speed must be between &a0 &cand &a10"));
            } else {
                sender.sendMessage(Translate.color("&cYour fly speed must be between &a0 &cand &a10"));
            }
        }
    }
}