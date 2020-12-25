package org.flyaga.sleepnow;
import org.bukkit.plugin.java.JavaPlugin;

public final class SleepNow extends JavaPlugin {
    private static SleepNow instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        SleepLogic sleeplogic = new SleepLogic();
        sleeplogic.logicStart();
        getLogger().info("------------------------");
        getLogger().info("[SleepNow] Loaded!");
        getLogger().info("------------------------");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("------------------------");
        getLogger().info("[SleepNow] Disabled!");
        getLogger().info("------------------------");
    }

    public static SleepNow getInstance(){
        return instance;
    }
}


