package com.andrew121410.mc.world16essentials.objects;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AfkObject {

    private UUID uuid;

    private Location location;
    private boolean isAfk;

    public AfkObject(Player player) {
        this.uuid = player.getUniqueId();
        this.location = player.getLocation();
        this.isAfk = false;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isAfk() {
        return isAfk;
    }

    public void setAfk(boolean afk) {
        isAfk = afk;
    }

    public void setAfk(boolean afk, Location location) {
        this.isAfk = afk;
        this.location = location;
    }

    public void restart(Player player) {
        this.location = player.getLocation();
        this.isAfk = false;
    }
}
