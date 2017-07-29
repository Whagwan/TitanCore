package TitanCore.Hub;

import TitanCore.ConfigManager;
import TitanCore.TCHub;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import titancoreapi.API.MySQL.SQLCallback;
import titancoreapi.API.MySQL.SQLManager;
import titancoreapi.API.MySQL.SQLRunnable;
import titancoreapi.API.Titles.Title;
import titancoreapi.API.Util.CC;
import titancoreapi.API.Util.UtilPlayer;
import titancoreapi.API.Util.UtilPlayer.playerSounds;
import titancoreapi.Core.Category;
import titancoreapi.Core.LogLevel;
import titancoreapi.Economy.EconomyManager;
import titancoreapi.TitanCoreAPI;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class TutorialManager {

    private static TCHub main = TCHub.getInstance();
    private static SQLManager sql = TitanCoreAPI.getInstance().getSQLManager();
    private static ConfigManager cfm = main.getConfigManager();
    private static Creature npc;
    private static BukkitTask npc_reattach;
    private static ArrayList<Player> players_Tutorial = new ArrayList<Player>();
    private static ArrayList<TutorialStep> tutorial_Steps = new ArrayList<TutorialStep>();

    public static void startTutorial(final Player p)
    {
        if (tutorial_Steps.size() <= 0)
        {
            main.getUtilLogger().logLevel(LogLevel.WARNING, "Tutorial> Player could not enter tutorial as it is not setup correctly!");
            return;
        }
        players_Tutorial.add(p);
        p.closeInventory();
        UtilPlayer.message(Category.HUB, p, CC.tnInfo + "You started the tutorial!");
        UtilPlayer.message(Category.PLAYER, p, CC.tnInfo + "You are now invisible.");
        p.setFlySpeed(0.0F);
        p.setWalkSpeed(0.0F);
        for (Player online : Bukkit.getOnlinePlayers())
        {
            online.hidePlayer(p);
            p.hidePlayer(online);
        }
        final Iterator<TutorialStep> itr = tutorial_Steps.iterator();
        new BukkitRunnable() {

            @Override
            public void run() {
                if (!itr.hasNext())
                {
                    doneTutorial(p);
                    cancel();
                }
                if (itr.hasNext())
                {
                    TutorialStep step = itr.next();
                    p.teleport(step.getLocation());
                    p.playSound(p.getLocation(), Sound.ORB_PICKUP, 2F, 2F);
                    Title text = new Title(step.getTitle(), step.getSubtitle());
                    text.setfadeIn(10);
                    text.setStay(60);
                    text.setfadeOut(10);
                    text.send(p);
                    UtilPlayer.messageNoCategory(p, " ");
                    UtilPlayer.messageNoCategory(p, step.getTitle());
                    UtilPlayer.messageNoCategory(p, step.getSubtitle());
                    p.setAllowFlight(true);
                    p.setFlying(true);
                }
                if (!players_Tutorial.contains(p))
                {
                    cancel();
                }

            }
        }.runTaskTimer(main, 0L, 80L);

    }

    public static void stopTutorial(Player p)
    {
        if (players_Tutorial.contains(p))
        {
            players_Tutorial.remove(p);
            for (Player online : Bukkit.getOnlinePlayers())
            {
                online.showPlayer(p);
                p.showPlayer(online);
            }
            p.setWalkSpeed(0.2F);
            p.setFlySpeed(0.1F);
            p.setAllowFlight(false);
            p.setFlying(false);
        }
    }

    public static boolean inTutorial(Player p)
    {
        return players_Tutorial.contains(p);
    }

    public static void handleLogin(Player p)
    {
        for (Player intut : players_Tutorial)
        {
            intut.hidePlayer(p);
            p.hidePlayer(intut);
        }
    }

    public static void handleLogout(Player p)
    {
        stopTutorial(p);
    }

    public static void addTutorialStep(String name, TutorialStep step)
    {
        String path = "tutorial." + name + ".";
        cfm.getTutorial().set(path + "location.x", step.getLocation().getX());
        cfm.getTutorial().set(path + "location.y", step.getLocation().getY());
        cfm.getTutorial().set(path + "location.z", step.getLocation().getZ());
        cfm.getTutorial().set(path + "location.yaw", step.getLocation().getYaw());
        cfm.getTutorial().set(path + "location.pitch", step.getLocation().getPitch());
        cfm.getTutorial().set(path + "title", step.getTitle());
        cfm.getTutorial().set(path + "subtitle", step.getSubtitle());
        cfm.saveTutorial();
    }

    public static boolean removeTutorialStep(String name)
    {
        if (cfm.getTutorial().contains("tutorial." + name))
        {
            cfm.getTutorial().set("tutorial." + name, null);
            return true;
        }
        return false;
    }

    public static void setNPCPosition(Location loc)
    {
        String path = "npc.location.";
        cfm.getTutorial().set(path + "x", loc.getX());
        cfm.getTutorial().set(path + "y", loc.getY());
        cfm.getTutorial().set(path + "z", loc.getZ());
        cfm.getTutorial().set(path + "yaw", loc.getYaw());
        cfm.getTutorial().set(path + "pitch", loc.getPitch());
        cfm.saveTutorial();
    }

    public static void spawnNPC()
    {
        if (cfm.getTutorial().contains("npc.location"))
        {
            Location loc = locFromConfig("npc.location");
            Chunk c = loc.getChunk();
            loc.getWorld().loadChunk(c);
            removeNPC();
            npc = (Creature)loc.getWorld().spawnEntity(loc, EntityType.SKELETON);
            npc.setMetadata("Tutorial", new FixedMetadataValue(main, "Identifier"));
            ((Skeleton)npc).setSkeletonType(SkeletonType.NORMAL);
            npc.getEquipment().setItemInHand(new ItemStack(Material.PAPER));
            npc.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
            net.minecraft.server.v1_8_R3.Entity nmsen = ((CraftEntity) npc)
                    .getHandle();
            NBTTagCompound compound = new NBTTagCompound();
            nmsen.c(compound);
            compound.setByte("NoAI", (byte) 1);
            compound.setByte("Silent", (byte)1);
            nmsen.f(compound);

            ArmorStand holo = (ArmorStand)loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
            EntityArmorStand stand = ((CraftArmorStand)holo).getHandle();
            stand.setInvisible(true);
            stand.setGravity(true);
            stand.setSmall(true);
            stand.setCustomName(CC.tnInfo + CC.BOLD + "Tutorial " + CC.tnUse + "(Right Click) " + CC.tnValue + "500 TitanCore Token " + CC.tnInfo + "Reward!");
            stand.setCustomNameVisible(true);
            holo.setMetadata("Tutorial", new FixedMetadataValue(main, "Identifier"));
            npc.setPassenger(holo);

            npc_reattach = new BukkitRunnable() {

                @Override
                public void run()
                {
                    npc.getEquipment().setItemInHand(new ItemStack(Material.PAPER));
                    npc.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
                }
            }.runTaskTimer(main, 200L, 200L);
        }
    }

    public static void handleInteract(PlayerInteractEntityEvent e)
    {
        if (!(e.getRightClicked() instanceof Skeleton) && (!(e.getRightClicked() instanceof ArmorStand)))
        {
            return;
        }
        if (e.getRightClicked() instanceof Skeleton)
        {
            Skeleton npc = (Skeleton) e.getRightClicked();
            if (npc.hasMetadata("Tutorial"))
            {
                if (inTutorial(e.getPlayer()))
                {
                    return;
                }
                startTutorial(e.getPlayer());
            }
        }
        if (e.getRightClicked() instanceof ArmorStand)
        {
            ArmorStand stand = (ArmorStand)e.getRightClicked();
            if (stand.hasMetadata("Tutorial"))
            {
                if (inTutorial(e.getPlayer()))
                {
                    return;
                }
                startTutorial(e.getPlayer());
            }
        }
    }

    public static void load()
    {
        main.getUtilLogger().logNormal("TutorialManager> Loading...");
        tutorial_Steps.clear();
        if (!cfm.getTutorial().contains("tutorial"))
        {
            main.getUtilLogger().logNormal("Tutorial> The tutorial could not be loaded as it has not been setup correctly!");
            return;
        }
        spawnNPC();
        Set<String> steps = cfm.getTutorial().getConfigurationSection("tutorial").getKeys(false);
        Iterator<String> itr = steps.iterator();
        while (itr.hasNext())
        {
            String step = itr.next();
            TutorialStep newstep = new TutorialStep(ChatColor.translateAlternateColorCodes('&', cfm.getTutorial().getString("tutorial." + step + ".title")), ChatColor.translateAlternateColorCodes('&', cfm.getTutorial().getString("tutorial." + step + ".subtitle")));
            newstep.setLocation(locFromConfig("tutorial." + step + ".location"));
            tutorial_Steps.add(newstep);
        }
        main.getUtilLogger().logNormal("Tutorial> Loaded tutorial with " + steps.size() + " steps.");

    }

    public static void shutdown()
    {
        main.getUtilLogger().logNormal("TutorialManager> Shutting down!");
        removeNPC();
        players_Tutorial.clear();
        tutorial_Steps.clear();
    }

    public static void removeNPC()
    {
        if (npc_reattach != null)
        {
            npc_reattach.cancel();
        }
        if (npc != null)
        {
            for (Entity entity : npc.getWorld().getEntities())
            {
                if (entity.hasMetadata("Tutorial"))
                {
                    entity.remove();
                }
            }
        }
    }

    public static void doneTutorial(final Player p)
    {
        players_Tutorial.remove(p);
        for (Player online : Bukkit.getOnlinePlayers())
        {
            online.showPlayer(p);
            p.showPlayer(online);
        }
        p.setWalkSpeed(0.2F);
        p.setFlySpeed(0.1F);
        p.setAllowFlight(false);
        p.setFlying(false);
        UtilPlayer.message(Category.HUB, p, CC.tnInfo + "You completed the hub tutorial!");
        p.playSound(p.getLocation(), Sound.LEVEL_UP, 2F, 1.5F);
        final String table = "Tutorial";
        SQLCallback<Boolean> call = new SQLCallback<Boolean>() {
            @Override
            public void execute(Boolean response)
            {
                if (!response)
                {
                    main.getServer().getScheduler().runTaskAsynchronously(main, new SQLRunnable("INSERT INTO " + table + "(UUID) VALUES('" + p.getUniqueId().toString().replace("-", "") + "') ON DUPLICATE KEY UPDATE UUID = UUID;", "TutorialManager"));
                    EconomyManager.addTokens(p, 500);
                    UtilPlayer.transaction(p, 500, "completing the tutorial", null);
                    UtilPlayer.sound(p, playerSounds.TRANSACTIONSUCCESS);
                }
            }
        };
        runCallback(p.getUniqueId().toString().replace("-", ""), call);
    }

    public static void runCallback(final String uuid, final SQLCallback<Boolean> callback)
    {
        new BukkitRunnable() {

            @Override
            public void run()
            {
                boolean ret = false;
                try
                {
                    ret = sql.existanceQuery("SELECT UUID FROM Tutorial WHERE UUID ='" + uuid + "';");
                }
                catch(SQLException e)
                {
                    main.getUtilLogger().logLevel(LogLevel.WARNING, "TutorialManager> SQL error in callback!");
                    e.printStackTrace();
                }
                final boolean callbackboolean = ret;
                new BukkitRunnable() {

                    @Override
                    public void run()
                    {
                        callback.execute(callbackboolean);
                    }
                }.runTask(main);
            }
        }.runTaskAsynchronously(main);
    }

    private static Location locFromConfig(String path)
    {
        World world = Bukkit.getServer().getWorld("hub-main");
        double x = cfm.getTutorial().getDouble(path + ".x");
        double y = cfm.getTutorial().getDouble(path + ".y");
        double z = cfm.getTutorial().getDouble(path + ".z");
        float yaw = (float)cfm.getTutorial().getDouble(path + ".yaw");
        float pitch = (float)cfm.getTutorial().getDouble(path + ".pitch");
        return new Location(world, x, y, z, yaw, pitch);
    }
}