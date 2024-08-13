package com.andrew121410.mc.world16utils.time;

import com.andrew121410.ccutils.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class CountdownTimer implements Runnable {

    private final JavaPlugin plugin;

    private Integer assignedTaskId;

    private final int seconds;
    private int secondsLeft;

    private final Consumer<CountdownTimer> everySecond;
    private final Runnable afterTimer;

    public CountdownTimer(JavaPlugin plugin, int seconds, Runnable afterTimer, Consumer<CountdownTimer> everySecond) {
        this.plugin = plugin;
        this.seconds = seconds;
        this.secondsLeft = seconds;
        this.afterTimer = afterTimer;
        this.everySecond = everySecond;
    }

    public CountdownTimer(JavaPlugin plugin, int seconds, int secondsLeft, Runnable afterTimer, Consumer<CountdownTimer> everySecond) {
        this.plugin = plugin;
        this.seconds = seconds;
        this.secondsLeft = secondsLeft;
        this.afterTimer = afterTimer;
        this.everySecond = everySecond;
    }

    @Override
    public void run() {
        if (secondsLeft < 1) {
            afterTimer.run();

            // Cancel timer
            if (assignedTaskId != null) Bukkit.getScheduler().cancelTask(assignedTaskId);
            return;
        }

        everySecond.accept(this);

        secondsLeft--;
    }

    public void scheduleTimer() {
        this.assignedTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, 20L);
    }

    @Deprecated
    public void scheduleTimerAsync() {
        this.assignedTaskId = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, this, 0L, 20L);
    }

    public void cancelTimer() {
        Bukkit.getScheduler().cancelTask(this.assignedTaskId);
    }

    public int getTotalSeconds() {
        return seconds;
    }

    public int getSecondsLeft() {
        return secondsLeft;
    }

    public String getFancyTimeLeft(boolean shortText, boolean showSeconds) {
        long currentTime = System.currentTimeMillis();
        long startTime = currentTime - (secondsLeft * 1000L);
        return TimeUtils.makeIntoEnglishWords(startTime, currentTime, shortText, showSeconds);
    }

    public String getFancyTimeLeft(boolean shortText) {
        return getFancyTimeLeft(shortText, true);
    }

    public Integer getAssignedTaskId() {
        return assignedTaskId;
    }
}
