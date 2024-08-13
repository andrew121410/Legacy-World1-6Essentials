package com.andrew121410.mc.world16essentials.managers;

import com.andrew121410.ccutils.dependencies.google.common.collect.Multimap;
import com.andrew121410.ccutils.storage.ISQL;
import com.andrew121410.ccutils.storage.SQLite;
import com.andrew121410.ccutils.storage.easy.EasySQL;
import com.andrew121410.ccutils.storage.easy.MultiTableEasySQL;
import com.andrew121410.ccutils.storage.easy.SQLDataStore;
import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.objects.SavedInventoryObject;

import java.sql.SQLException;
import java.util.*;

public class SavedInventoriesManager {

    private final Map<UUID, Set<String>> savedInventoryMap;

    private final World16Essentials plugin;

    private final ISQL iSQL;
    private final EasySQL easySQL;

    public SavedInventoriesManager(World16Essentials plugin) {
        this.plugin = plugin;
        this.savedInventoryMap = this.plugin.getMemoryHolder().getSavedInventoryMap();

        this.iSQL = new SQLite(this.plugin.getDataFolder(), "SavedInventories");
        this.easySQL = new EasySQL("SavedInventories", new MultiTableEasySQL(this.iSQL));

        List<String> columns = new ArrayList<>();

        columns.add("UUID");
        columns.add("InventoryName");
        columns.add("RegularInventory");
        columns.add("ArmorContent");

        easySQL.create(columns, false);
    }

    public void save(UUID uuid, SavedInventoryObject savedInventoryObject) {
        save(uuid, savedInventoryObject.getName(), savedInventoryObject.getData());
    }

    public void save(UUID uuid, String name, String[] data) {
        addToMapIfNotPresent(uuid);
        this.savedInventoryMap.get(uuid).add(name);

        SQLDataStore map = new SQLDataStore();

        map.put("UUID", uuid.toString());
        map.put("InventoryName", name);
        map.put("RegularInventory", data[0]);
        map.put("ArmorContent", data[1]);

        try {
            this.easySQL.save(map);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SavedInventoryObject load(UUID uuid, String inventoryName) {
        SQLDataStore toGet = new SQLDataStore();

        toGet.put("UUID", uuid.toString());
        toGet.put("InventoryName", inventoryName);

        Multimap<String, SQLDataStore> multimap = null;
        try {
            multimap = this.easySQL.get(toGet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (multimap == null) return null;

        SQLDataStore sqlDataStore = multimap.values().stream().findFirst().get();

        String regularInventory = sqlDataStore.get("RegularInventory");
        String armorContent = sqlDataStore.get("ArmorContent");
        String[] data = new String[]{regularInventory, armorContent};

        return new SavedInventoryObject(inventoryName, data);
    }

    // Used in CMIDataTranslator
    public Map<String, SavedInventoryObject> load(UUID uuid) {
        Map<String, SavedInventoryObject> savedInventoryObjectMap = new HashMap<>();

        SQLDataStore toGet = new SQLDataStore();
        toGet.put("UUID", uuid.toString());

        Multimap<String, SQLDataStore> multimap = null;
        try {
            multimap = this.easySQL.get(toGet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (multimap == null) return null;

        for (SQLDataStore sqlDataStore : multimap.values()) {
            String inventoryName = sqlDataStore.get("InventoryName");
            String regularInventory = sqlDataStore.get("RegularInventory");
            String armorContent = sqlDataStore.get("ArmorContent");
            String[] data = new String[]{regularInventory, armorContent};

            savedInventoryObjectMap.put(inventoryName, new SavedInventoryObject(inventoryName, data));
        }

        return savedInventoryObjectMap;
    }

    // Used in PlayerInitializer
    public void loadAllSavedInventoriesNames(UUID uuid) {
        addToMapIfNotPresent(uuid);

        SQLDataStore toGet = new SQLDataStore();
        toGet.put("UUID", uuid.toString());

        Multimap<String, SQLDataStore> multimap = null;
        try {
            multimap = this.easySQL.get(toGet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (multimap == null) return;

        multimap.forEach((s, sqlDataStore) -> {
            String inventoryName = sqlDataStore.get("InventoryName");
            this.savedInventoryMap.get(uuid).add(inventoryName);
        });
    }

    public void delete(UUID uuid, String inventoryName) {
        addToMapIfNotPresent(uuid);
        this.savedInventoryMap.get(uuid).remove(inventoryName);

        SQLDataStore map = new SQLDataStore();
        map.put("UUID", uuid.toString());
        map.put("InventoryName", inventoryName);

        try {
            this.easySQL.delete(map);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addToMapIfNotPresent(UUID uuid) {
        this.savedInventoryMap.putIfAbsent(uuid, new HashSet<>());
    }
}
