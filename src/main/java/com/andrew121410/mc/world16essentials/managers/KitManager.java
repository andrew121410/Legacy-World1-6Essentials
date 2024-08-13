package com.andrew121410.mc.world16essentials.managers;

import com.andrew121410.ccutils.dependencies.google.common.collect.Multimap;
import com.andrew121410.ccutils.storage.ISQL;
import com.andrew121410.ccutils.storage.SQLite;
import com.andrew121410.ccutils.storage.easy.EasySQL;
import com.andrew121410.ccutils.storage.easy.MultiTableEasySQL;
import com.andrew121410.ccutils.storage.easy.SQLDataStore;
import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.objects.KitObject;
import com.andrew121410.mc.world16utils.utils.BukkitSerialization;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class KitManager {

    private final Map<String, KitObject> kitsMap;

    private final World16Essentials plugin;
    private final KitSettingsManager kitSettingsManager;

    private final ISQL iSQL;
    private final EasySQL easySQL;

    public KitManager(World16Essentials plugin) {
        this.plugin = plugin;
        this.kitsMap = this.plugin.getMemoryHolder().getKitsMap();
        this.kitSettingsManager = this.plugin.getKitSettingsManager();

        this.iSQL = new SQLite(this.plugin.getDataFolder(), "Kits");
        this.easySQL = new EasySQL("Kits", new MultiTableEasySQL(this.iSQL));

        List<String> columns = new ArrayList<>();

        columns.add("KitName");
        columns.add("WhoCreated");
        columns.add("TimeCreated");
        columns.add("RegularInventory");
        columns.add("ArmorContent");

        easySQL.create(columns, true);
    }

    public void addKit(UUID creator, String kitName, String[] data) {
        KitObject kitObject = new KitObject(kitName, this.kitSettingsManager.generateSettings(kitName), creator, System.currentTimeMillis() + "", data);
        this.kitsMap.put(kitName, kitObject);
        saveKit(kitObject);
    }

    public void saveKit(KitObject kitObject) {
        SQLDataStore map = new SQLDataStore();

        map.put("KitName", kitObject.getKitName());
        map.put("WhoCreated", kitObject.getWhoCreatedUUID() != null ? kitObject.getWhoCreatedUUID().toString() : "null");
        map.put("TimeCreated", kitObject.getTimeCreated());
        map.put("RegularInventory", kitObject.getData()[0]);
        map.put("ArmorContent", kitObject.getData()[1]);

        try {
            this.easySQL.save(map);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteKit(String kitName) {
        // Remove from memory
        this.kitsMap.remove(kitName);

        // Remove settings
        this.plugin.getKitSettingsManager().deleteFromConfig(kitName);

        // Remove from database
        SQLDataStore map = new SQLDataStore();
        map.put("KitName", kitName);
        try {
            this.easySQL.delete(map);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadKits() {
        try {
            Multimap<String, SQLDataStore> everything = this.easySQL.getEverything();
            everything.forEach((key, value) -> {
                String kitName = value.get("KitName");
                String whoCreated = value.get("WhoCreated");
                String timeCreated = value.get("TimeCreated");
                String regularInventory = value.get("RegularInventory");
                String armorContent = value.get("ArmorContent");

                KitObject kitObject = new KitObject(kitName, this.kitSettingsManager.getFromConfig(kitName), !whoCreated.equals("null") ? UUID.fromString(whoCreated) : null, timeCreated, new String[]{regularInventory, armorContent});
                this.kitsMap.put(kitName, kitObject);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void giveKit(Player player, KitObject kitObject) {
        this.plugin.getKitSettingsManager().setLastUsed(player, kitObject);
        BukkitSerialization.giveFromBase64s(player, kitObject.getData());
    }
}
