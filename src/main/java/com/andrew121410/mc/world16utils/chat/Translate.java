package com.andrew121410.mc.world16utils.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;

public class Translate {

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String colorc(String s) {
        return Translate.color(s);
    }

    public static String chat(String s) {
        return color(s);
    }

    public static String miniMessage(String s) {
        Component component = MiniMessage.miniMessage().deserialize(s);
        String text = LegacyComponentSerializer.legacyAmpersand().serialize(component);
        return Translate.color(text);
    }
}
