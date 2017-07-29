package TitanCore;


import org.bukkit.configuration.file.FileConfiguration;

import titancoreapi.API.Configs.ConfigFile;

public class ConfigManager {

    private ConfigFile config;
    private ConfigFile tutorial;

    public void load()
    {
        TCHub tevohub = TCHub.getInstance();

        this.config = new ConfigFile(tevohub, tevohub.getDataFolder(), "config", true);
        this.tutorial = new ConfigFile(tevohub, tevohub.getDataFolder(), "tutorial", false);

        reloadConfig();
        reloadTutorial();
    }

    public FileConfiguration getConfig()
    {
        return this.config.getConfig();
    }

    public FileConfiguration getTutorial()
    {
        return this.tutorial.getConfig();
    }

    public void reloadConfig()
    {
        this.config.reload();
    }

    public void reloadTutorial()
    {
        this.tutorial.reload();
    }

    public void saveConfig()
    {
        this.config.save();
    }

    public void saveTutorial()
    {
        this.tutorial.save();
    }

}
