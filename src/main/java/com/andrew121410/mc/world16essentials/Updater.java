package com.andrew121410.mc.world16essentials;

import com.andrew121410.ccutils.utils.HashBasedUpdater;

public class Updater extends HashBasedUpdater {

    private static final String JAR_URL = "https://github.com/World1-6/Legacy-World1-6Essentials/releases/download/latest/Legacy-World1-6Essentials+java8.jar";
    private static final String HASH_URL = "https://github.com/World1-6/Legacy-World1-6Essentials/releases/download/latest/hash.txt";

    public Updater(World16Essentials plugin) {
        super(plugin.getClass(), JAR_URL, HASH_URL);
    }
}
