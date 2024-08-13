package com.andrew121410.mc.world16essentials.managers;

import com.andrew121410.ccutils.dependencies.google.common.collect.Multimap;
import com.andrew121410.ccutils.storage.easy.EasySQL;
import com.andrew121410.ccutils.storage.easy.MultiTableEasySQL;
import com.andrew121410.ccutils.storage.easy.SQLDataStore;
import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import org.bukkit.Location;

import java.sql.SQLException;
import java.util.*;

public class OldHomeManager {

    private final World16Essentials plugin;
    private final EasySQL easySQL;

    public OldHomeManager(World16Essentials plugin, MultiTableEasySQL multiTableEasySQL) {
        this.plugin = plugin;

        this.easySQL = new EasySQL("Homes", multiTableEasySQL);

        List<String> columns = new ArrayList<>();
        columns.add("UUID");
        columns.add("Date");
        columns.add("PlayerName");
        columns.add("HomeName");
        columns.add("X");
        columns.add("Y");
        columns.add("Z");
        columns.add("YAW");
        columns.add("PITCH");
        columns.add("World");
        easySQL.create(columns, false);
    }

    public UnlinkedWorldLocation getLocation(SQLDataStore sqlDataStore) {
        String stringX = sqlDataStore.get("X");
        String stringY = sqlDataStore.get("Y");
        String stringZ = sqlDataStore.get("Z");
        String stringYaw = sqlDataStore.get("YAW");
        String stringPitch = sqlDataStore.get("PITCH");
        String stringWorld = sqlDataStore.get("World");

        // Convert old data if needed.
        UUID worldUUID = convertWorldNamesToWorldUUIDsIfNeeded(sqlDataStore);

        return new UnlinkedWorldLocation(worldUUID, Double.parseDouble(stringX), Double.parseDouble(stringY), Double.parseDouble(stringZ), Float.parseFloat(stringYaw), Float.parseFloat(stringPitch));
    }

    private UUID convertWorldNamesToWorldUUIDsIfNeeded(SQLDataStore sqlDataStore) {
        String stringX = sqlDataStore.get("X");
        String stringY = sqlDataStore.get("Y");
        String stringZ = sqlDataStore.get("Z");
        String stringYaw = sqlDataStore.get("YAW");
        String stringPitch = sqlDataStore.get("PITCH");
        String stringWorld = sqlDataStore.get("World");

        UUID worldUUID = null;

        // Test to see if the world is a UUID or a string.
        try {
            worldUUID = UUID.fromString(stringWorld);
        } catch (IllegalArgumentException e) {
            Location location = new Location(this.plugin.getServer().getWorld(stringWorld), Double.parseDouble(stringX), Double.parseDouble(stringY), Double.parseDouble(stringZ), Float.parseFloat(stringYaw), Float.parseFloat(stringPitch));

            // Delete the old data.
            SQLDataStore toDelete = new SQLDataStore();
            toDelete.put("UUID", sqlDataStore.get("UUID"));
            toDelete.put("HomeName", sqlDataStore.get("HomeName"));
            try {
                easySQL.delete(toDelete);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            // Save the new data.
            SQLDataStore newData = makeSQLDataStore(UUID.fromString(sqlDataStore.get("UUID")), sqlDataStore.get("PlayerName"), sqlDataStore.get("HomeName"), new UnlinkedWorldLocation(location));
            try {
                easySQL.save(newData);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            this.plugin.getServer().getLogger().info("Converted old home data for " + sqlDataStore.get("PlayerName") + " for home " + sqlDataStore.get("HomeName") + " to use world UUID instead of world name.");

            // Set the worldUUID to the new world.
            worldUUID = location.getWorld().getUID();
        }

        return worldUUID;
    }

    public SQLDataStore makeSQLDataStore(UUID uuid, String playerName, String homeName, UnlinkedWorldLocation location) {
        SQLDataStore sqlDataStore = new SQLDataStore();
        sqlDataStore.put("UUID", String.valueOf(uuid));
        sqlDataStore.put("Date", String.valueOf(System.currentTimeMillis()));
        sqlDataStore.put("PlayerName", playerName);
        sqlDataStore.put("HomeName", homeName.toLowerCase());
        sqlDataStore.put("X", String.valueOf(location.getX()));
        sqlDataStore.put("Y", String.valueOf(location.getY()));
        sqlDataStore.put("Z", String.valueOf(location.getZ()));
        sqlDataStore.put("YAW", String.valueOf(location.getYaw()));
        sqlDataStore.put("PITCH", String.valueOf(location.getPitch()));
        sqlDataStore.put("World", String.valueOf(location.getWorldUUID()));
        return sqlDataStore;
    }

    // Used for data translator (EssentialsX, CMI, etc.)
    public Map<UUID, Map<String, UnlinkedWorldLocation>> loadAllHomesFromDatabase() {
        Map<UUID, Map<String, UnlinkedWorldLocation>> bigMap = new HashMap<>();

        try {
            Multimap<String, SQLDataStore> convert = easySQL.getEverything();
            convert.forEach((key, value) -> {
                UUID uuid = UUID.fromString(value.get("UUID"));
                String homeName = value.get("HomeName");
                UnlinkedWorldLocation location = getLocation(value);

                if (!bigMap.containsKey(uuid)) bigMap.put(uuid, new HashMap<>());

                bigMap.get(uuid).put(homeName, location);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bigMap;
    }
}