package com.andrew121410.mc.world16utils.chat;

import org.bukkit.ChatColor;

public class Translate {

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String chat(String s) {
        return color(s);
    }

    public static String miniMessage(String s) {
        return Translate.color(MinimotdTranslator.translate(s));
    }
}
