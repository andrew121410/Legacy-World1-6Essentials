package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.objects.SavedInventoryObject;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SaveInventoryCMD implements CommandExecutor {

    private final Map<UUID, Set<String>> savedInventoryMap;

    private final World16Essentials plugin;
    private final API api;

    public SaveInventoryCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();
        this.savedInventoryMap = this.plugin.getMemoryHolder().getSavedInventoryMap();

        this.plugin.getCommand("saveinventory").setExecutor(this);
        this.plugin.getCommand("saveinventory").setTabCompleter((sender, command, alias, args) -> {
            if (!(sender instanceof Player player)) return null;
            if (!player.hasPermission("world16.saveinventory")) return null;

            Set<String> savedInventories = this.savedInventoryMap.get(player.getUniqueId());

            if (args.length == 1) {
                return TabUtils.getContainsString(args[0], Arrays.asList("save", "load", "delete"));
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("load") || args[0].equalsIgnoreCase("delete")) {
                    return TabUtils.getContainsString(args[1], savedInventories.stream().toList());
                }
            }
            return null;
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.saveinventory")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }
        Set<String> savedInventorySet = this.savedInventoryMap.get(player.getUniqueId());

        if (args.length == 2) {
            String action = args[0];
            String name = args[1];

            if (action.equalsIgnoreCase("save")) {
                SavedInventoryObject savedInventoryObject = SavedInventoryObject.create(player, name);

                this.plugin.getSavedInventoriesManager().save(player.getUniqueId(), savedInventoryObject);
                player.sendMessage(Translate.miniMessage("<green>Inventory saved as <gold>" + name.toLowerCase()));
                return true;
            } else if (action.equalsIgnoreCase("load")) {
                if (!savedInventorySet.contains(name)) {
                    player.sendMessage(Translate.miniMessage("<red>Unable to find the inventory <gold>" + name.toLowerCase()));
                    return true;
                }

                SavedInventoryObject savedInventoryObject = this.plugin.getSavedInventoriesManager().load(player.getUniqueId(), name);
                savedInventoryObject.give(player);
                player.sendMessage(Translate.miniMessage("<green>Inventory loaded as <gold>" + name));
                return true;
            } else if (action.equalsIgnoreCase("delete")) {
                if (!savedInventorySet.contains(name)) {
                    player.sendMessage(Translate.miniMessage("<red>Unable to find the inventory <gold>" + name.toLowerCase()));
                    return true;
                }

                this.plugin.getSavedInventoriesManager().delete(player.getUniqueId(), name);
                player.sendMessage(Translate.miniMessage("<green>Inventory deleted as <gold>" + name));
                return true;
            }
        } else {
            player.sendMessage(Translate.miniMessage("<red>Usage: <gold>/saveinventory <save/load/delete> <name>"));
        }
        return true;
    }
}
