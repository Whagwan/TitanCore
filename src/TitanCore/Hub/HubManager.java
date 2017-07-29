package TitanCore.Hub;

import TitanCore.ConfigManager;
import TitanCore.TCHub;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;
import titancoreapi.API.Regions.Region;
import titancoreapi.API.Titles.Title;
import titancoreapi.Core.LogLevel;

import java.util.ArrayList;
import java.util.List;

public class HubManager {

    private static TCHub main = TCHub.getInstance();
    private static ConfigManager cfm = main.getConfigManager();
    private static Region chatregion;
    private static ArrayList<String> allowed_Chat = new ArrayList<String>();

    public static void sendTitles(final Player p)
    {
        final List<String> welcomes = cfm.getConfig().getStringList("welcometitles");
        if (welcomes.isEmpty())
        {
            return;
        }
        new BukkitRunnable() {
            int count = 0;
            @Override
            public void run() {

                Title msg = new Title(ChatColor.translateAlternateColorCodes('&', welcomes.get(count).split("_")[0]), ChatColor.translateAlternateColorCodes('&', welcomes.get(count).split("_")[1]));
                msg.setfadeIn(10);
                msg.setfadeOut(10);
                msg.setStay(40);
                msg.send(p);
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1F, 2F);
                count++;
                if (count >= welcomes.size())
                {
                    cancel();
                }
            }
        }.runTaskTimer(main, 20L, 60L);
    }

    public static void allowedChat(Player p)
    {
        allowed_Chat.add(p.getName());
    }

    public static void clearAllowedChat(Player p)
    {
        allowed_Chat.remove(p.getName());
    }

    public static boolean isAllowedChat(Player p)
    {
        if (allowed_Chat.contains(p.getName()))
        {
            return true;
        }
        return false;
    }

    public static void loadRegion()
    {
        if (!cfm.getConfig().contains("chatregion"))
        {
            main.getUtilLogger().logLevel(LogLevel.WARNING, "HubManager> The chat region has not been set.");
            return;
        }
        String path = "chatregion.";
        String worldname = cfm.getConfig().getString("chatregion.world");
        World world = Bukkit.getServer().getWorld(worldname);
        if (world == null)
        {
            main.getUtilLogger().logLevel(LogLevel.WARNING, "HubManager> The chat region world is invalid!");
            return;
        }

        double xmin = cfm.getConfig().getDouble(path + "min.x");
        double ymin = cfm.getConfig().getDouble(path + "min.y");
        double zmin = cfm.getConfig().getDouble(path + "min.z");

        double xmax = cfm.getConfig().getDouble(path + "max.x");
        double ymax = cfm.getConfig().getDouble(path + "max.y");
        double zmax = cfm.getConfig().getDouble(path + "max.z");

        chatregion = new Region(new Location(world,xmin, ymin, zmin), new Location(world, xmax, ymax, zmax));
        main.getUtilLogger().logNormal("HubManager> Chat region loaded successfully!");
    }

    public static Region getChatRegion()
    {
        return chatregion;
    }

    public static boolean sendtoHub(Player p)
    {
        if (getLoginLocation() != null)
        {
            p.teleport(getLoginLocation());
            return true;
        }
        return false;
    }

    public static boolean sendToBorderlineHub(Player p)
    {
        if (getBorderlineLocation() != null)
        {
            p.teleport(getBorderlineLocation());
            return true;
        }
        return false;
    }

    public static void handleInvClick(InventoryClickEvent e)
    {
        if (e.getInventory().getTitle().equalsIgnoreCase("Staff Menu"))
        {
            e.setCancelled(true);
        }
    }

    private static Location getBorderlineLocation()
    {
        if (cfm.getConfig().contains("spawnpoints.borderline"))
        {
            String path = "spawnpoints.borderline.";
            World world = Bukkit.getWorld(cfm.getConfig().getString(path + "world"));
            double x = cfm.getConfig().getDouble(path + "x");
            double y = cfm.getConfig().getDouble(path + "y");
            double z = cfm.getConfig().getDouble(path + "z");
            double yaw = cfm.getConfig().getDouble(path + "yaw");
            double pitch = cfm.getConfig().getDouble(path + "pitch");
            return new Location(world, x, y, z, (float)yaw, (float)pitch);
        }
        return null;
    }

    private static Location getLoginLocation()
    {
        if (cfm.getConfig().contains("spawnpoints.main"))
        {
            String path = "spawnpoints.main.";
            World world = Bukkit.getWorld(cfm.getConfig().getString(path + "world"));
            double x = cfm.getConfig().getDouble(path + "x");
            double y = cfm.getConfig().getDouble(path + "y");
            double z = cfm.getConfig().getDouble(path + "z");
            double yaw = cfm.getConfig().getDouble(path + "yaw");
            double pitch = cfm.getConfig().getDouble(path + "pitch");
            return new Location(world, x, y, z, (float)yaw, (float)pitch);
        }
        return null;
    }

}
