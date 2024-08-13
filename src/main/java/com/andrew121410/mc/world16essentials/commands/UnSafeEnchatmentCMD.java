package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.utils.EnchantmentUtils;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import com.andrew121410.mc.world16utils.utils.Utils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UnSafeEnchatmentCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public UnSafeEnchatmentCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("unsafenchant").setExecutor(this);
        this.plugin.getCommand("unsafenchant").setTabCompleter((sender, command, s, args) -> {
            if (!sender.hasPermission("world16.unsafenchant")) return null;

            if (args.length == 1) {
                return TabUtils.getContainsString(args[0], EnchantmentUtils.getVanillaEnchantmentNames());
            }
            return null;
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.unsafenchant")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 2) {
            ItemStack mainHand = player.getInventory().getItemInMainHand();
            Enchantment enchantment = EnchantmentUtils.getByName(args[0]);
            int level = Utils.asIntegerOrElse(args[1], 0);

            if (enchantment == null) {
                player.sendMessage(Translate.color("&cThat's not a valid enchantment."));
                return true;
            }

            if (mainHand.getType() == Material.AIR) {
                player.sendMessage(Translate.color("&cYou must be holding an item to enchant."));
                return true;
            }

            mainHand.addUnsafeEnchantment(enchantment, level);
            return true;
        } else {
            player.sendMessage(Translate.color("&cUsage: &6/unsafenchant <Enchant> <Level>"));
        }
        return true;
    }
}
