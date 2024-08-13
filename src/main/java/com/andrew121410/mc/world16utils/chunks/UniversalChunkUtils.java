package com.andrew121410.mc.world16utils.chunks;

import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class UniversalChunkUtils {

    public static Location getCenterChunkLocation(Chunk c) {
        Location center = new Location(c.getWorld(), c.getX() << 4, 64, c.getZ() << 4).add(8, 0, 8);
        center.setY(center.getWorld().getHighestBlockYAt(center) + 1);
        return center;
    }

    public static Location getSouthEast(Chunk chunk) {
        Location center = getCenterChunkLocation(chunk);
        return center.clone().add(7.5, 0, 7.5);
    }

    public static Location getNorthEast(Chunk chunk) {
        Location center = getCenterChunkLocation(chunk);
        return center.clone().add(7.5, 0, -7.5);
    }

    public static Location getNorthWest(Chunk chunk) {
        Location center = getCenterChunkLocation(chunk);
        return center.clone().add(-7.5, 0, -7.5);
    }

    public static Location getSouthWest(Chunk chunk) {
        Location center = getCenterChunkLocation(chunk);
        return center.clone().add(-7.5, 0, 7.5);
    }

    public static List<Location> getFourOuterPoints(Chunk chunk) {
        List<Location> locations = new ArrayList<>();
        locations.add(UniversalChunkUtils.getNorthEast(chunk));
        locations.add(UniversalChunkUtils.getNorthWest(chunk));
        locations.add(UniversalChunkUtils.getSouthEast(chunk));
        locations.add(UniversalChunkUtils.getSouthWest(chunk));
        return locations;
    }
}
