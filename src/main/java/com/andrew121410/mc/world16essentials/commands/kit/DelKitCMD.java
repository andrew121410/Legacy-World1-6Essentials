package com.andrew121410.mc.world16essentials.commands.kit;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.objects.KitObject;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class DelKitCMD implements CommandExecutor {

    private final Map<String, KitObject> kitsMap;

    private final World16Essentials plugin;
    private final API api;

    public DelKitCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();
        this.kitsMap = this.plugin.getMemoryHolder().getKitsMap();

        this.plugin.getCommand("delkit").setExecutor(this);
        this.plugin.getCommand("delkit").setTabCompleter((sender, command, alias, args) -> {
            if (!(sender instanceof Player player)) return null;
            if (!player.hasPermission("world16.kit.delete")) return null;

            List<String> kits = this.plugin.getMemoryHolder().getKitsMap().keySet().stream().toList();

            if (args.length == 1) {
                return TabUtils.getContainsString(args[0], kits);
            }
            return null;
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.kit.delete")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 1) {
            String name = args[0];

            if (!this.kitsMap.containsKey(name)) {
                player.sendMessage(Translate.miniMessage("<red>That kit doesn't exist!"));
                return true;
            }

            this.plugin.getKitManager().deleteKit(name);
            player.sendMessage(Translate.miniMessage("<green>Kit <blue>" + name + " <green>has been deleted!"));
            return true;
        }
        player.sendMessage(Translate.miniMessage("<red>Usage: /delkit <name>"));
        return false;
    }
}
