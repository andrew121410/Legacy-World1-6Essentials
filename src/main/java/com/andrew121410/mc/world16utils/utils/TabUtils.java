package com.andrew121410.mc.world16utils.utils;

import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabUtils {
    public static List<String> getContainsString(String args, List<String> list) {
        List<String> newList = new ArrayList<>();
        for (String string : list) {
            if (string.contains(args)) {
                newList.add(string);
            }
        }
        return newList;
    }

    public static List<String> getOfflinePlayerNames(List<OfflinePlayer> offlinePlayers) {
        List<String> playerNames = new ArrayList<>();
        for (OfflinePlayer player : offlinePlayers) {
            if (player != null) {
                String name = player.getName();
                if (name != null && !name.isEmpty() && !"null".equals(name)) {
                    playerNames.add(name);
                }
            }
        }
        return playerNames;
    }

    public static List<String> getOfflinePlayerNames(OfflinePlayer[] offlinePlayers) {
        return getOfflinePlayerNames(Arrays.asList(offlinePlayers));
    }
}
