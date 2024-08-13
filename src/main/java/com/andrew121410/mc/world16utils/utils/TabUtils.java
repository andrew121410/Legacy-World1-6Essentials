package com.andrew121410.mc.world16utils.utils;

import java.util.ArrayList;
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
}
