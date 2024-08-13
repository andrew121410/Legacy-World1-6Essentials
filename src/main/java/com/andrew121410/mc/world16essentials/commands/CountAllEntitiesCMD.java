package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CountAllEntitiesCMD implements CommandExecutor {
    private final World16Essentials plugin;
    private final API api;

    public CountAllEntitiesCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("countallentities").setExecutor(this);
        this.plugin.getCommand("countallentities").setTabCompleter((sender, command, s, args) -> {
            if (!sender.hasPermission("world16.countallentities")) return null;

            if (args.length == 1) {
                List<String> entities = new ArrayList<>();
                for (EntityType entityType : EntityType.values()) {
                    entities.add(entityType.name());
                }

                // Not an entity type, but a sub-command.
                entities.add("top");
                entities.add("forworld");

                return TabUtils.getContainsString(args[0], entities);
            } else if (args.length == 2 && args[0].equalsIgnoreCase("forworld")) {
                List<String> worlds = new ArrayList<>();
                for (World world : plugin.getServer().getWorlds()) {
                    worlds.add(world.getName());
                }
                return TabUtils.getContainsString(args[1], worlds);
            } else if (args.length == 3 && args[0].equalsIgnoreCase("forworld")) {
                List<String> entities = new ArrayList<>();
                for (EntityType entityType : EntityType.values()) {
                    entities.add(entityType.name());
                }
                entities.add("top");
                return TabUtils.getContainsString(args[2], entities);
            }
            return null;
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("world16.countallentities")) {
            api.sendPermissionErrorMessage(sender);
            return true;
        }

        if (args.length == 0) {
            handleCountAllEntities(sender, null);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("top")) {
            handleTopEntities(sender, null);
        } else if (args[0].equalsIgnoreCase("forworld")) { // /countallentities forworld <world> <optional:entity>
            if (args.length < 2) {
                sender.sendMessage(Translate.miniMessage("<red>Please provide a world name"));
                return true;
            }

            World world = plugin.getServer().getWorld(args[1]);
            if (world == null) {
                sender.sendMessage(Translate.color("&cInvalid world name."));
                return true;
            }

            if (args.length == 2) {
                handleCountAllEntities(sender, world);
            } else if (args[2].equalsIgnoreCase("top")) {
                handleTopEntities(sender, world);
            } else {
                handleEntityCountForWorld(sender, world, args[2]);
            }

            return true;
        } else {
            handleEntityCount(sender, args[0]);
        }
        return true;
    }

    private void handleCountAllEntities(CommandSender sender, World world) {
        List<Entity> allEntities = new ArrayList<>();

        if (world != null) {
            allEntities.addAll(world.getEntities());
            sender.sendMessage(Translate.color("&aThere are &e" + allEntities.size() + " &aentities in &e" + world.getName() + "."));
            return;
        }

        // Count all entities in all worlds.
        for (World worldFor : this.plugin.getServer().getWorlds()) {
            allEntities.addAll(worldFor.getEntities());
        }

        sender.sendMessage(Translate.color("&aThere are &e" + allEntities.size() + " &aentities on the server."));
    }

    private void handleTopEntities(CommandSender sender, World world) {
        List<Entity> entities = (world == null) ? getAllEntities() : world.getEntities();
        Map<String, Long> entityCounts = entities.stream()
                .map(entity -> entity.getType().name())
                .collect(Collectors.groupingBy(name -> name, Collectors.counting()));
        sender.sendMessage(Translate.color("&a&lTop Entities" + (world != null ? " in " + world.getName() : "") + ":"));
        entityCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .forEach(entry -> sender.sendMessage(Translate.color("&a" + entry.getKey() + " &f- &e" + entry.getValue())));
    }

    private void handleEntityCountForWorld(CommandSender sender, World world, String entityTypeStr) {
        EntityType entityType = getEntityType(entityTypeStr);
        if (entityType == null) {
            sender.sendMessage(Translate.color("&cInvalid Entity Type."));
            return;
        }
        Class<? extends Entity> clazz = entityType.getEntityClass();
        if (clazz == null) {
            sender.sendMessage(Translate.color("&cInvalid Entity Type."));
            return;
        }
        Collection<? extends Entity> entities = world.getEntitiesByClass(clazz);
        sender.sendMessage(Translate.color("&a&lTotal Entities in " + world.getName() + ": &f" + entities.size() + " &aof type &f" + entityType.name()));
    }

    private void handleEntityCount(CommandSender sender, String entityTypeStr) {
        EntityType entityType = getEntityType(entityTypeStr);
        if (entityType == null) {
            sender.sendMessage(Translate.color("&cInvalid Entity Type."));
            return;
        }
        List<Entity> entities = new ArrayList<>();
        for (World world : this.plugin.getServer().getWorlds()) {
            Class<? extends Entity> clazz = entityType.getEntityClass();
            if (clazz == null) continue;
            entities.addAll(world.getEntitiesByClass(clazz));
        }
        sender.sendMessage(Translate.color("&a&lTotal Entities: &f" + entities.size() + " &aof type &f" + entityType.name()));
    }

    private List<Entity> getAllEntities() {
        List<Entity> allEntities = new ArrayList<>();
        for (World world : this.plugin.getServer().getWorlds()) {
            allEntities.addAll(world.getEntities());
        }
        return allEntities;
    }

    private EntityType getEntityType(String entityTypeStr) {
        try {
            return EntityType.valueOf(entityTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}