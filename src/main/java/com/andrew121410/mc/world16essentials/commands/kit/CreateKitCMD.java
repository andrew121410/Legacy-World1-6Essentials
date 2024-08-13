package com.andrew121410.mc.world16essentials.commands.kit;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.utils.BukkitSerialization;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateKitCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public CreateKitCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();
        this.plugin.getCommand("createkit").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.kit.create")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 1) {
            String name = args[0];

            this.plugin.getKitManager().addKit(player.getUniqueId(), name, BukkitSerialization.turnInventoryIntoBase64s(player));
            player.sendMessage(Translate.color("&2Kit &9" + name + " &2has been created!"));
            return true;
        }
        player.sendMessage(Translate.color("&2Usage: /createkit <name>"));
        return false;
    }
}
