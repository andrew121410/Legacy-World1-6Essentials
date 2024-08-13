package com.andrew121410.mc.world16utils.config;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A normal Location when deserialized will not work if the world is not loaded.
 * <p>
 * Before you use the Location you must check if the world is loaded with isWorldLoaded()
 */

@SerializableAs("UnlinkedWorldLocation")
public class UnlinkedWorldLocation extends Location implements ConfigurationSerializable {

    private final String world;

    public UnlinkedWorldLocation(String world, double x, double y, double z, float yaw, float pitch) {
        super(null, x, y, z, yaw, pitch); // World is null, because it might not be loaded.
        this.world = world;
    }

    public UnlinkedWorldLocation(String world, double x, double y, double z) {
        this(world, x, y, z, 0, 0);
    }

    public UnlinkedWorldLocation(Location location) {
        this(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

//    @Override
//    public boolean isWorldLoaded() {
//        return org.bukkit.Bukkit.getWorld(world) != null;
//    }

    @Override
    public World getWorld() {
        return org.bukkit.Bukkit.getWorld(world);
    }

    public String getWorldName() {
        return world;
    }

    @Deprecated(forRemoval = true)
    public Location toLocation() {
        return new Location(getWorld(), getX(), getY(), getZ(), getYaw(), getPitch());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("world", world);
        map.put("x", this.getX());
        map.put("y", this.getY());
        map.put("z", this.getZ());
        map.put("yaw", this.getYaw());
        map.put("pitch", this.getPitch());
        return map;
    }

    public static UnlinkedWorldLocation deserialize(Map<String, Object> map) {
        String world = (String) map.get("world");
        double x = (Double) map.getOrDefault("x", 0);
        double y = (Double) map.getOrDefault("y", 0);
        double z = (Double) map.getOrDefault("z", 0);

        // java.lang.Double cannot be cast to class java.lang.Float
        double fakeYaw = (Double) map.getOrDefault("yaw", 0);
        double fakePitch = (Double) map.getOrDefault("pitch", 0);

        return new UnlinkedWorldLocation(world, x, y, z, (float) fakeYaw, (float) fakePitch);
    }
}
