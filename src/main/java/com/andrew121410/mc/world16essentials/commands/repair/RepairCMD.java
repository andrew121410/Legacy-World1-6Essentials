package com.andrew121410.mc.world16essentials.commands.repair;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RepairCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public RepairCMD(World16Essentials getPlugin) {
        this.plugin = getPlugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("repair").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only Players Can Use This Command.");
                return true;
            }

            if (!player.hasPermission("world16.repair")) {
                api.sendPermissionErrorMessage(player);
                return true;
            }

            repairItem(player, null);
            return true;
        } else if (args.length == 1) {
            if (!sender.hasPermission("world16.repair.other")) {
                api.sendPermissionErrorMessage(sender);
                return true;
            }

            Player target = plugin.getServer().getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                sender.sendMessage(Translate.color("&cThat player is not online."));
                return true;
            }

            repairItem(target, sender);
            return true;
        } else {
            sender.sendMessage(Translate.color("&cUsage: for yourself do /repair OR /repair <Player>"));
        }
        return true;
    }

    private void repairItem(Player target, CommandSender sender) {
        if (target.getInventory().getItemInMainHand().getType() == Material.AIR) {
            target.sendMessage(Translate.color("&cYou must be holding an item to repair it."));
            if (sender != null) sender.sendMessage(Translate.color("&cThat player isn't holding an item."));
            return;
        }

        ItemStack itemStack = target.getInventory().getItemInMainHand();
        if (InventoryUtils.repairItem(itemStack)) {
            target.sendMessage(Translate.color("&6Your item has been repaired."));
            if (sender != null) sender.sendMessage(Translate.color("&6That player's item has been repaired."));
        } else {
            target.sendMessage(Translate.color("&cYour item couldn't be repaired."));
            if (sender != null) sender.sendMessage(Translate.color("&cThat player's item couldn't be repaired."));
        }
    }
}
