package com.andrew121410.mc.world16essentials.commands.tp;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class TpAcceptCMD implements CommandExecutor {

    private final Map<UUID, UUID> tpaMap;

    private final World16Essentials plugin;
    private final API api;

    public TpAcceptCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.tpaMap = this.plugin.getMemoryHolder().getTpaMap();

        this.plugin.getCommand("tpaccept").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.tpa")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 0) {
            UUID uuid = this.tpaMap.get(player.getUniqueId());
            Player tpa = this.plugin.getServer().getPlayer(uuid);
            if (tpa != null) {
                tpa.teleport(player);
                tpa.sendMessage(Translate.color("&6" + player.getName() + " &ahas accepted your tpa request."));
                this.tpaMap.remove(player.getUniqueId());
                return true;
            } else {
                player.sendMessage(Translate.color("&eYou don't have any tpa request."));
            }
            return true;
        }
        return true;
    }
}