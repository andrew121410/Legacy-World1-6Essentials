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

public class TpDenyCMD implements CommandExecutor {

    private final Map<UUID, UUID> tpaMap;

    private final World16Essentials plugin;
    private final API api;

    public TpDenyCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.tpaMap = this.plugin.getMemoryHolder().getTpaMap();

        this.plugin.getCommand("tpdeny").setExecutor(this);
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

        UUID uuid = this.tpaMap.get(player.getUniqueId());
        Player tpa = this.plugin.getServer().getPlayer(uuid); // This is the player that sent the tpa request.

        if (tpa != null) {
            this.tpaMap.remove(player.getUniqueId());
            tpa.sendMessage(Translate.miniMessage("<red>Your tpa request got denied by " + player.getName() + "."));
            player.sendMessage(Translate.miniMessage("<red>You have denied the tpa request from " + tpa.getName() + "."));
        } else {
            player.sendMessage(Translate.miniMessage("<yellow>You don't have any tpa request."));
        }
        return true;
    }
}