package org.flyaga.sleepnow;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class SleepNow extends JavaPlugin {
    private static SleepNow instance;

    ///console
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    ///

    /// configs
    private File settingsConfigFile;
    private FileConfiguration settingsConfig;
    private File defaultLangConfigFile; // do not change the default language, it changes in the settings.yml file.
    private FileConfiguration defaultLangConfig;
    private File customLangConfigFile;
    private FileConfiguration customLangConfig;
    ///

    @Override
    public void onEnable() {
        instance = this;

        /// language config
        createSettingsConfig();
        SleepLang sleeplang = new SleepLang();
        if(sleeplang.isCustomLang() == true){
            customLangConfigFile = new File(getDataFolder(), "/language/" + SleepLang.language);
            loadCustomLangConfig();
        }
        else{
            createDefaultLangConfig();
        }
        sleeplang.customMessagesLoad();
        /// language config

        SleepLogic sleeplogic = new SleepLogic();
        sleeplogic.logicStart();
        getLogger().info(ANSI_RED + "------------------------" + ANSI_RESET);
        getLogger().info(ANSI_RED + "SleepNow loaded!" + ANSI_RESET);
        getLogger().info(ANSI_RED + "https://github.com/0x115/SleepNow" + ANSI_RESET);
        getLogger().info(ANSI_RED + "------------------------" + ANSI_RESET);
    }

    @Override
    public void onDisable() {
        getLogger().info(ANSI_RED + "------------------------" + ANSI_RESET);
        getLogger().info(ANSI_RED + "SleepNow disabled!" + ANSI_RESET);
        getLogger().info(ANSI_RED + "https://github.com/0x115/SleepNow" + ANSI_RESET);
        getLogger().info(ANSI_RED + "------------------------" + ANSI_RESET);
    }
    public static SleepNow getInstance(){
        return instance;
    }



    ///////////////////////////////////
    public FileConfiguration getSettingsConfig() {
        return this.settingsConfig;
    }
    public FileConfiguration getDefaultLangConfig() {
        return this.defaultLangConfig;
    }
    public FileConfiguration getCustomLangConfig() {
        return this.customLangConfig;
    }

    private void createSettingsConfig() {
        settingsConfigFile = new File(getDataFolder(), "settings.yml");
        if (!settingsConfigFile.exists()) {
            settingsConfigFile.getParentFile().mkdirs();
            saveResource("settings.yml", false);
        }
        settingsConfig = new YamlConfiguration();
        try {
            settingsConfig.load(settingsConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void createDefaultLangConfig() {
        defaultLangConfigFile = new File(getDataFolder(), "language/sleepnow_en.yml");
        if (!defaultLangConfigFile.exists()) {
            defaultLangConfigFile.getParentFile().mkdirs();
            saveResource("language/sleepnow_en.yml", false);
        }
        defaultLangConfig = new YamlConfiguration();
        try {
            defaultLangConfig.load(defaultLangConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void loadCustomLangConfig(){
        customLangConfig = new YamlConfiguration();
        try {
            customLangConfig.load(customLangConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    ///////////////////////////////////

}


