package com.andrew121410.mc.world16essentials.utils;

import com.andrew121410.mc.world16essentials.commands.back.BackEnum;
import com.andrew121410.mc.world16essentials.objects.AfkObject;
import com.andrew121410.mc.world16essentials.objects.KitObject;
import com.andrew121410.mc.world16essentials.objects.PowerToolObject;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import org.bukkit.entity.Player;

import java.util.*;

public class MemoryHolder {

    // Clear on player leave (maps)
    private final Map<UUID, Map<BackEnum, UnlinkedWorldLocation>> backMap;
    private final Map<UUID, UUID> tpaMap;
    private final Map<UUID, AfkObject> afkMap;
    private final Map<UUID, Map<String, UnlinkedWorldLocation>> homesMap;
    private final Map<UUID, PowerToolObject> powerToolMap;
    private final Map<UUID, Long> timeOfLoginMap;
    private final Map<UUID, Set<String>> savedInventoryMap;
    private final Map<UUID, UUID> lastPlayerToMessageMap;

    // Clear when server stops (maps)
    private final Map<String, UnlinkedWorldLocation> warpsMap;
    private final Map<String, KitObject> kitsMap;

    // Clear on player leave (lists)
    private final List<UUID> godList;
    private final List<UUID> hiddenPlayers;

    // Clear when server stops (lists)
    private final List<String> soundsList;
    private final List<String> spyCommandBlock;

    public MemoryHolder() {
        this.backMap = new HashMap<>();
        this.tpaMap = new LinkedHashMap<>();
        this.afkMap = new HashMap<>();
        this.homesMap = new HashMap<>();
        this.powerToolMap = new HashMap<>();
        this.timeOfLoginMap = new HashMap<>();
        this.savedInventoryMap = new HashMap<>();
        this.lastPlayerToMessageMap = new HashMap<>();

        this.warpsMap = new HashMap<>();
        this.kitsMap = new HashMap<>();

        //Lists
        this.godList = new ArrayList<>();
        this.hiddenPlayers = new ArrayList<>();

        this.soundsList = new ArrayList<>();
        this.spyCommandBlock = new ArrayList<>();

    }

    /*
     * Clear all memory for a player
     */
    public void remove(Player player) {
        clearAllMaps(player);
        clearAllLists(player);
    }

    /*
     * Clear all memory
     */
    public void clear() {
        clearAllMaps();
        clearAllLists();
    }

    private void clearAllMaps(Player player) {
        backMap.remove(player.getUniqueId());
        tpaMap.remove(player.getUniqueId());
        afkMap.remove(player.getUniqueId());
        homesMap.remove(player.getUniqueId());
        powerToolMap.remove(player.getUniqueId());
        timeOfLoginMap.remove(player.getUniqueId());
        savedInventoryMap.remove(player.getUniqueId());
        lastPlayerToMessageMap.remove(player.getUniqueId());
    }

    private void clearAllMaps() {
        backMap.clear();
        tpaMap.clear();
        afkMap.clear();
        homesMap.clear();
        powerToolMap.clear();
        timeOfLoginMap.clear();
        savedInventoryMap.clear();
        lastPlayerToMessageMap.clear();

        warpsMap.clear();
        kitsMap.clear();
    }

    private void clearAllLists(Player player) {
        godList.remove(player.getUniqueId());
        hiddenPlayers.remove(player.getUniqueId());
    }

    private void clearAllLists() {
        godList.clear();
        hiddenPlayers.clear();
        soundsList.clear();
        spyCommandBlock.clear();
    }

    public Map<UUID, Map<BackEnum, UnlinkedWorldLocation>> getBackMap() {
        return backMap;
    }

    public Map<UUID, UUID> getTpaMap() {
        return tpaMap;
    }

    public Map<UUID, AfkObject> getAfkMap() {
        return afkMap;
    }

    public Map<UUID, Map<String, UnlinkedWorldLocation>> getHomesMap() {
        return homesMap;
    }

    public List<UUID> getGodList() {
        return godList;
    }

    public List<UUID> getHiddenPlayers() {
        return hiddenPlayers;
    }

    public Map<String, UnlinkedWorldLocation> getWarpsMap() {
        return warpsMap;
    }

    public List<String> getSoundsList() {
        return soundsList;
    }

    public Map<UUID, PowerToolObject> getPowerToolMap() {
        return powerToolMap;
    }

    public List<String> getSpyCommandBlock() {
        return spyCommandBlock;
    }

    public Map<UUID, Long> getTimeOfLoginMap() {
        return timeOfLoginMap;
    }

    public Map<String, KitObject> getKitsMap() {
        return kitsMap;
    }

    public Map<UUID, Set<String>> getSavedInventoryMap() {
        return savedInventoryMap;
    }

    public Map<UUID, UUID> getLastPlayerToMessageMap() {
        return lastPlayerToMessageMap;
    }
}
