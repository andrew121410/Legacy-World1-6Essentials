package com.andrew121410.mc.world16essentials.config;

import com.andrew121410.mc.world16essentials.World16Essentials;

public class ConfigUtils {

    private final World16Essentials plugin;

    private boolean showLastUpdatedMessageToOPs;
    private boolean signTranslateColors;
    private boolean preventCropsTrampling;
    private int spawnMobCap;
    private boolean saveInventoryOnDeath;
    private boolean moreAccurateDiskInfo;
    private boolean offlinePlayersTabCompletion;

    public ConfigUtils(World16Essentials plugin) {
        this.plugin = plugin;

        this.showLastUpdatedMessageToOPs = this.plugin.getConfig().getBoolean("showLastUpdatedMessageToOPs");
        this.signTranslateColors = this.plugin.getConfig().getBoolean("signTranslateColors");
        this.preventCropsTrampling = this.plugin.getConfig().getBoolean("preventCropsTrampling");
        this.spawnMobCap = this.plugin.getConfig().getInt("spawnMobCap");
        this.saveInventoryOnDeath = this.plugin.getConfig().getBoolean("saveInventoryOnDeath");
        this.moreAccurateDiskInfo = this.plugin.getConfig().getBoolean("moreAccurateDiskInfo");
        this.offlinePlayersTabCompletion = this.plugin.getConfig().getBoolean("offlinePlayersTabCompletion");
    }

    public boolean isShowLastUpdatedMessageToOPs() {
        return showLastUpdatedMessageToOPs;
    }

    public void setShowLastUpdatedMessageToOPs(boolean showLastUpdatedMessageToOPs) {
        this.showLastUpdatedMessageToOPs = showLastUpdatedMessageToOPs;
        this.plugin.getConfig().set("showLastUpdatedMessageToOPs", showLastUpdatedMessageToOPs);
        this.plugin.saveConfig();
    }

    public boolean isSignTranslateColors() {
        return signTranslateColors;
    }

    public void setSignTranslateColors(boolean signTranslateColors) {
        this.signTranslateColors = signTranslateColors;
        this.plugin.getConfig().set("signTranslateColors", signTranslateColors);
        this.plugin.saveConfig();
    }

    public boolean isPreventCropsTrampling() {
        return preventCropsTrampling;
    }

    public void setPreventCropsTrampling(boolean preventCropsTrampling) {
        this.preventCropsTrampling = preventCropsTrampling;
        this.plugin.getConfig().set("preventCropsTrampling", preventCropsTrampling);
        this.plugin.saveConfig();
    }

    public int getSpawnMobCap() {
        return spawnMobCap;
    }

    public void setSpawnMobCap(int spawnMobCap) {
        this.spawnMobCap = spawnMobCap;
        this.plugin.getConfig().set("spawnMobCap", spawnMobCap);
        this.plugin.saveConfig();
    }

    public boolean isSaveInventoryOnDeath() {
        return saveInventoryOnDeath;
    }

    public void setSaveInventoryOnDeath(boolean saveInventoryOnDeath) {
        this.saveInventoryOnDeath = saveInventoryOnDeath;
        this.plugin.getConfig().set("saveInventoryOnDeath", saveInventoryOnDeath);
        this.plugin.saveConfig();
    }

    public boolean isMoreAccurateDiskInfo() {
        return moreAccurateDiskInfo;
    }

    public void setMoreAccurateDiskInfo(boolean moreAccurateDiskInfo) {
        this.moreAccurateDiskInfo = moreAccurateDiskInfo;
        this.plugin.getConfig().set("moreAccurateDiskInfo", moreAccurateDiskInfo);
        this.plugin.saveConfig();
    }

    public boolean isOfflinePlayersTabCompletion() {
        return offlinePlayersTabCompletion;
    }

    public void setOfflinePlayersTabCompletion(boolean offlinePlayersTabCompletion) {
        this.offlinePlayersTabCompletion = offlinePlayersTabCompletion;
        this.plugin.getConfig().set("offlinePlayersTabCompletion", offlinePlayersTabCompletion);
        this.plugin.saveConfig();
    }
}
