package com.andrew121410.mc.world16essentials.objects;

import com.andrew121410.mc.world16utils.utils.BukkitSerialization;
import org.bukkit.entity.Player;

public class SavedInventoryObject {

    private final String name;
    private final String[] data;

    public SavedInventoryObject(String name, String[] data) {
        this.name = name;
        this.data = data;
    }

    public static SavedInventoryObject create(Player player, String name) {
        String[] data = BukkitSerialization.turnInventoryIntoBase64s(player);
        return new SavedInventoryObject(name, data);
    }

    public void give(Player player) {
        BukkitSerialization.giveFromBase64s(player, this.data);
    }

    public String getName() {
        return name;
    }

    public String[] getData() {
        return data;
    }
}
