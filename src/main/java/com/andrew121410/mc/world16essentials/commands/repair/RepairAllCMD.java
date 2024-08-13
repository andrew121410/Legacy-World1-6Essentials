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

public class RepairAllCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public RepairAllCMD(World16Essentials getPlugin) {
        this.plugin = getPlugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("repairall").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only Players Can Use This Command.");
                return true;
            }

            if (!player.hasPermission("world16.repair.all")) {
                api.sendPermissionErrorMessage(player);
                return true;
            }

            repairAllItems(player, null);
            return true;
        } else if (args.length == 1) {
            if (!sender.hasPermission("world16.repair.all.other")) {
                api.sendPermissionErrorMessage(sender);
                return true;
            }

            Player target = plugin.getServer().getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                sender.sendMessage(Translate.color("&cThat player is not online."));
                return true;
            }

            repairAllItems(target, sender);
            return true;
        } else {
            sender.sendMessage(Translate.color("&cUsage: for yourself do /repairall OR /repairall <Player>"));
        }
        return true;
    }

    private void repairAllItems(Player target, CommandSender commandSender) {
        for (ItemStack itemStack : target.getInventory()) {
            if (itemStack == null || itemStack.getType() == Material.AIR) continue;
            InventoryUtils.repairItem(itemStack);
        }

        target.sendMessage(Translate.miniMessage("<gold>All of your items have been repaired."));
        if (commandSender != null) {
            commandSender.sendMessage(Translate.miniMessage("<gold>You have repaired all of <reset><green>" + target.getName() + "<reset><gold>'s items."));
        }
    }
}
