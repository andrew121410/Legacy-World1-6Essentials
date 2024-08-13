package com.andrew121410.mc.world16essentials.objects;

import java.util.UUID;

public class KitObject {

    private final String kitName;
    private final KitSettingsObject settings;
    private final UUID whoCreatedUUID;
    private final String timeCreated;
    private final String[] data;

    public KitObject(String kitName, KitSettingsObject settings, UUID whoCreatedUUID, String timeCreated, String[] data) {
        this.kitName = kitName;
        this.settings = settings;
        this.whoCreatedUUID = whoCreatedUUID;
        this.timeCreated = timeCreated;
        this.data = data;
    }

    public String getKitName() {
        return kitName;
    }

    public UUID getWhoCreatedUUID() {
        return whoCreatedUUID;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public String[] getData() {
        return data;
    }

    public KitSettingsObject getSettings() {
        return settings;
    }
}
