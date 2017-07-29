package TitanCore;

import TitanCore.Commands.ProfileCMD;
import TitanCore.Commands.SetHubCMD;
import TitanCore.Commands.SnowmanCMD;
import TitanCore.Commands.TutorialCMD;
import TitanCore.Hub.Announcer;
import TitanCore.Hub.HubManager;
import TitanCore.Listeners.EntityListeners;
import TitanCore.Listeners.InventoryListeners;
import TitanCore.Listeners.ProjectileListeners;
import TitanCore.Vanity.WardrobeManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import titancoreapi.API.Util.UtilLogger;
import titancoreapi.Core.LogLevel;
import TitanCore.Commands.HubCMD;
import TitanCore.Commands.TitanHubCMD;
import TitanCore.Hub.TutorialManager;
import TitanCore.Listeners.PlayerListeners;
import TitanCore.Pet.PetManager;
import TitanCore.Vanity.VanityManager;

public class TCHub extends JavaPlugin{

    private UtilLogger logger;
    private static TCHub main;
    private ConfigManager cfm;
    private HubCMD hubcmd;

    @Override
    public void onEnable()
    {
        main = this;
        this.logger = new UtilLogger(this);
        checkDependencies();
        startManagers();
        registerCMDS();
        registerListeners();
        startTasks();
        this.logger.logEnableDisable(true);
    }

    @Override
    public void onDisable()
    {
        TutorialManager.shutdown();
        this.logger.logEnableDisable(false);
    }

    public static TCHub getInstance()
    {
        return main;
    }

    public HubCMD getHubCMD()
    {
        return this.hubcmd;
    }

    public ConfigManager getConfigManager()
    {
        return this.cfm;
    }

    public UtilLogger getUtilLogger()
    {
        return this.logger;
    }

    private void startTasks()
    {
        logger.logNormal("Plugin> Starting tasks...");
        BukkitScheduler s = this.getServer().getScheduler();
        s.scheduleSyncRepeatingTask(this, new PetManager(), 300L, 30L);
        s.scheduleSyncRepeatingTask(this, new WardrobeManager(), 300L, 6L);
        s.scheduleSyncRepeatingTask(this, new Announcer(), 300L, cfm.getConfig().getInt("announcer.interval") * 20);
        logger.logNormal("Plugin> Tasks have been started!");
    }

    private void registerListeners()
    {
        logger.logNormal("Plugin> Registering listeners...");
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new EntityListeners(), this);
        pm.registerEvents(new PlayerListeners(), this);
        pm.registerEvents(new InventoryListeners(), this);
        pm.registerEvents(new ProjectileListeners(), this);
        logger.logNormal("Plugin> Registered listners!");
    }

    private void registerCMDS()
    {
        logger.logNormal("Plugin> Registering commands...");
        this.hubcmd = new HubCMD();
        getCommand("tchub").setExecutor(new TitanHubCMD());
        getCommand("sethub").setExecutor(new SetHubCMD());
        getCommand("profile").setExecutor(new ProfileCMD());
        getCommand("tutorial").setExecutor(new TutorialCMD());
        getCommand("snowman").setExecutor(new SnowmanCMD());
        logger.logNormal("Plugin> Registered Commands!");
    }

    private void startManagers()
    {
        logger.logNormal("Plugin> Starting managers...");
        this.cfm = new ConfigManager();
        this.cfm.load();
        VanityManager.loadItems();
        TutorialManager.load();
        HubManager.loadRegion();
        logger.logNormal("Plugin> Managers have been initialized!");
    }

    private void checkDependencies()
    {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        if (pm.getPlugin("TitanAPI") == null)
        {
            logger.logLevel(LogLevel.WARNING, "Plugin> TitanAPI is missing, disabling!");
            this.setEnabled(false);
        }
        else
        {
            logger.logNormal("Plugin> Found TitanAPI!");
        }
        if (pm.getPlugin("LibsDisguises") == null)
        {
            logger.logLevel(LogLevel.WARNING, "Plugin> LibsDisguises is missing, disabling!");
            this.setEnabled(false);
        }
        else
        {
            logger.logNormal("Plugin> Found LibsDisguises!");
        }
    }
}
