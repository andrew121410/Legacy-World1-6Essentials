package com.andrew121410.mc.world16essentials.listeners;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.objects.PowerToolObject;
import com.andrew121410.mc.world16essentials.utils.API;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;
import java.util.UUID;

public class OnPlayerInteractEvent implements Listener {

    private final Map<UUID, PowerToolObject> powerToolMap;

    private final World16Essentials plugin;
    private final API api;

    public OnPlayerInteractEvent(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.powerToolMap = this.plugin.getMemoryHolder().getPowerToolMap();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        Action action = event.getAction();

        PowerToolObject powerToolObject = this.powerToolMap.get(player.getUniqueId());
        if (action == Action.RIGHT_CLICK_BLOCK && block != null) {
            powerToolObject.runCommand(player, player.getInventory().getItemInMainHand().getType());
        } else if (action == Action.LEFT_CLICK_AIR) {
            powerToolObject.runCommand(player, player.getInventory().getItemInMainHand().getType());
        } else if (action == Action.PHYSICAL) {
            if (block != null) {
                if (block.getType() == Material.SOIL && api.getConfigUtils().isPreventCropsTrampling()) {
                    event.setCancelled(true);
                }
            }
        }
    }
}