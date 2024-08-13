package com.andrew121410.mc.world16essentials.objects;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Map;

public class KitSettingsObject implements ConfigurationSerializable {
    private String cooldown;
    private String permission;
    private boolean giveOnFirstJoin;

    public KitSettingsObject(String cooldown, String permission, boolean giveOnFirstJoin) {
        this.cooldown = cooldown;
        this.permission = permission;
        this.giveOnFirstJoin = giveOnFirstJoin;
    }

    public String getCooldown() {
        return cooldown;
    }

    public String getPermission() {
        return permission;
    }

    public boolean isGiveOnFirstJoin() {
        return giveOnFirstJoin;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new java.util.HashMap<>();
        map.put("cooldown", this.cooldown);
        map.put("permission", this.permission);
        map.put("giveOnFirstJoin", this.giveOnFirstJoin);
        return map;
    }

    public static KitSettingsObject deserialize(Map<String, Object> map) {
        String cooldown = (String) map.getOrDefault("cooldown", "none");
        String permission = (String) map.getOrDefault("permission", "none");
        boolean giveOnFirstJoin = (Boolean) map.getOrDefault("giveOnFirstJoin", false);

        return new KitSettingsObject(cooldown, permission, giveOnFirstJoin);
    }
}