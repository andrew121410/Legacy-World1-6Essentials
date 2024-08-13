package com.andrew121410.mc.world16utils.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * A normal Location when deserialized will not work if the world is not loaded.
 * <p>
 * Before you use the Location you must check if the world is loaded with isWorldLoaded()
 */

@SerializableAs("UnlinkedWorldLocation")
public class UnlinkedWorldLocation extends Location implements ConfigurationSerializable {

    private final UUID world;

    public UnlinkedWorldLocation(UUID world, double x, double y, double z, float yaw, float pitch) {
        super(null, x, y, z, yaw, pitch); // World is null, because it might not be loaded.
        this.world = world;
    }

    public UnlinkedWorldLocation(UUID world, double x, double y, double z) {
        this(world, x, y, z, 0, 0);
    }

    public UnlinkedWorldLocation(Location location) {
        this(location.getWorld().getUID(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public boolean isWorldLoaded() {
        return org.bukkit.Bukkit.getWorld(world) != null;
    }

    @Override
    public World getWorld() {
        return org.bukkit.Bukkit.getWorld(world);
    }

    public UUID getWorldUUID() {
        return this.world;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("world", String.valueOf(this.world)); // Why can't I just save it as a UUID and not a string?
        map.put("x", this.getX());
        map.put("y", this.getY());
        map.put("z", this.getZ());
        map.put("yaw", this.getYaw());
        map.put("pitch", this.getPitch());
        return map;
    }

    public static UnlinkedWorldLocation deserialize(Map<String, Object> map) {
        String worldString = (String) map.get("world");

        // Temporary
        UUID worldUUID;
        try {
            worldUUID = UUID.fromString(worldString);
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "UnlinkedWorldLocation - You need to convert the world name to a UUID. Using the world name is deprecated.");

            World world = org.bukkit.Bukkit.getWorld(worldString);

            if (world == null) {
                throw new IllegalArgumentException("Invalid world: " + worldString);
            }

            worldUUID = world.getUID();
        }

        double x = (Double) map.getOrDefault("x", 0);
        double y = (Double) map.getOrDefault("y", 0);
        double z = (Double) map.getOrDefault("z", 0);

        // java.lang.Double cannot be cast to class java.lang.Float
        double fakeYaw = (Double) map.getOrDefault("yaw", 0);
        double fakePitch = (Double) map.getOrDefault("pitch", 0);

        return new UnlinkedWorldLocation(worldUUID, x, y, z, (float) fakeYaw, (float) fakePitch);
    }
}