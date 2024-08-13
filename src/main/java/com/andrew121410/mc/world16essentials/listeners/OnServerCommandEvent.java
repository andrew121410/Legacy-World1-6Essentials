package com.andrew121410.mc.world16essentials.listeners;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.chat.Translate;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.Iterator;
import java.util.List;

public class OnServerCommandEvent implements Listener {

    private final World16Essentials plugin;
    private final List<String> spyCommandBlock;

    public OnServerCommandEvent(World16Essentials plugin) {
        this.plugin = plugin;
        this.spyCommandBlock = this.plugin.getMemoryHolder().getSpyCommandBlock();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onCommand(ServerCommandEvent event) {
        // Don't try to iterate if the list is empty.
        if (spyCommandBlock.isEmpty()) return;

        if (event.getSender() instanceof BlockCommandSender) {
            BlockCommandSender blockCommandSender = (BlockCommandSender) event.getSender();

            Iterator<String> iterator = this.spyCommandBlock.iterator();
            while (iterator.hasNext()) {
                String toSearch = iterator.next();
                if (event.getCommand().contains(toSearch)) {
                    this.plugin.getServer().broadcastMessage(Translate.chat("&c&lSPY FOUND&e->&r Found: " + toSearch + " Location: X:" + blockCommandSender.getBlock().getLocation().getX() + " Y: " + blockCommandSender.getBlock().getLocation().getY() + " Z: " + blockCommandSender.getBlock().getLocation().getZ()));
                    ComponentBuilder components = new ComponentBuilder(Translate.chat("[&eCLICK ME TO TP TO IT EASY&r]")).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + blockCommandSender.getBlock().getLocation().getBlockX() + " " + blockCommandSender.getBlock().getLocation().getBlockY() + " " + blockCommandSender.getBlock().getLocation().getBlockZ()));
                    this.plugin.getServer().spigot().broadcast(components.create());

                    iterator.remove();
                }
            }
        }
    }
}