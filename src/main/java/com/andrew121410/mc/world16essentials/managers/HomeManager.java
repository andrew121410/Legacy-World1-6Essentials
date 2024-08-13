package com.andrew121410.mc.world16essentials.managers;

import com.andrew121410.ccutils.storage.ISQL;
import com.andrew121410.ccutils.storage.SQLite;
import com.andrew121410.ccutils.storage.easy.EasySQL;
import com.andrew121410.ccutils.storage.easy.MultiTableEasySQL;
import com.andrew121410.ccutils.storage.easy.SQLDataStore;
import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import com.andrew121410.mc.world16utils.utils.Utils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.sql.SQLException;
import java.util.*;

public class HomeManager {

    private final Map<UUID, Map<String, UnlinkedWorldLocation>> homesMap;

    private final World16Essentials plugin;

    private final EasySQL easySQL;
    private final ISQL isql;

    public HomeManager(World16Essentials plugin) {
        this.plugin = plugin;
        this.homesMap = this.plugin.getMemoryHolder().getHomesMap();

        this.isql = new SQLite(this.plugin.getDataFolder(), "Homes");
        this.easySQL = new EasySQL("Homes", new MultiTableEasySQL(this.isql));

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

    public void load(OfflinePlayer player) {
        this.homesMap.putIfAbsent(player.getUniqueId(), loadHomes(player.getUniqueId()));
    }

    public Map<String, UnlinkedWorldLocation> loadHomes(UUID uuid) {
        Multimap<String, SQLDataStore> multimap = null;

        SQLDataStore toGet = new SQLDataStore();
        toGet.put("UUID", String.valueOf(uuid));
        try {
            multimap = easySQL.get(toGet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (multimap == null) return new HashMap<>();

        Map<String, UnlinkedWorldLocation> homes = new HashMap<>();
        multimap.forEach((key, value) -> {
            String homeName = value.get("HomeName");
            UnlinkedWorldLocation location = getLocation(value);
            homes.put(homeName, location);
        });

        return homes;
    }

    public void add(OfflinePlayer offlinePlayer, String homeName, UnlinkedWorldLocation location) {
        if (offlinePlayer == null) return;

        if (offlinePlayer.isOnline()) {
            this.homesMap.get(offlinePlayer.getUniqueId()).put(homeName, location);
        }

        save(makeSQLDataStore(offlinePlayer.getUniqueId(), offlinePlayer.getName(), homeName, location));
    }

    public void add(OfflinePlayer offlinePlayer, Map<String, UnlinkedWorldLocation> map) {
        if (offlinePlayer == null) return;

        if (offlinePlayer.isOnline()) {
            this.homesMap.get(offlinePlayer.getUniqueId()).putAll(map);
        }

        saveBulk(offlinePlayer.getUniqueId(), map);
    }

    public void delete(UUID uuid, String homeName) {
        this.homesMap.get(uuid).remove(homeName.toLowerCase());
        SQLDataStore toDelete = new SQLDataStore();
        toDelete.put("UUID", String.valueOf(uuid));
        toDelete.put("HomeName", homeName.toLowerCase());
        try {
            easySQL.delete(toDelete);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllHomes(UUID uuid) {
        this.homesMap.get(uuid).clear();
        SQLDataStore toDelete = new SQLDataStore();
        toDelete.put("UUID", String.valueOf(uuid));
        try {
            easySQL.delete(toDelete);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(SQLDataStore sqlDataStore) {
        try {
            easySQL.save(sqlDataStore);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveBulk(UUID uuid, Map<String, UnlinkedWorldLocation> map) {
        Multimap<String, SQLDataStore> multimap = ArrayListMultimap.create();

        for (Map.Entry<String, UnlinkedWorldLocation> entry : map.entrySet()) {
            String homeName = entry.getKey();
            UnlinkedWorldLocation location = entry.getValue();
            multimap.put(String.valueOf(uuid), makeSQLDataStore(uuid, "na", homeName, location));
        }

        try {
            this.easySQL.save(multimap);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UnlinkedWorldLocation getLocation(SQLDataStore sqlDataStore) {
        String stringX = sqlDataStore.get("X");
        String stringY = sqlDataStore.get("Y");
        String stringZ = sqlDataStore.get("Z");
        String stringYaw = sqlDataStore.get("YAW");
        String stringPitch = sqlDataStore.get("PITCH");
        String stringWorld = sqlDataStore.get("World");

        return new UnlinkedWorldLocation(stringWorld, Double.parseDouble(stringX), Double.parseDouble(stringY), Double.parseDouble(stringZ), Float.parseFloat(stringYaw), Float.parseFloat(stringPitch));
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
        sqlDataStore.put("World", location.getWorldName());
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

    public int getMaximumHomeCount(Player player) {
        //@TODO should we have some sort of cache for this?

        for (PermissionAttachmentInfo permissionAttachmentInfo : player.getEffectivePermissions()) {
            String permission = permissionAttachmentInfo.getPermission();
            if (permission.startsWith("world16.home.") || permission.startsWith("world16.homes.")) {
                return Utils.asIntegerOrElse(permission.substring(permission.lastIndexOf(".") + 1), -1);
            }
        }
        return -1;
    }
}