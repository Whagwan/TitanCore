package TitanCore.Listeners;

import TitanCore.Disguise.DisguiseManager;
import TitanCore.Gadget.GadgetManager;
import TitanCore.Hub.HubManager;
import TitanCore.Hub.TutorialManager;
import TitanCore.TCHub;
import TitanCore.Vanity.VanityManager;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import titancoreapi.API.Util.CC;
import titancoreapi.API.Util.UtilPlayer;
import titancoreapi.Core.Category;
import titancoreapi.Core.LogLevel;
import titancoreapi.Core.Rank;
import titancoreapi.Core.Messages.AnnounceMSG;

public class PlayerListeners implements Listener {

    private TCHub main = TCHub.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();
        e.setJoinMessage(null);
        UtilPlayer.clearInv(p);
        p.setGameMode(GameMode.ADVENTURE);
        TutorialManager.handleLogin(p);
        if (!HubManager.sendtoHub(p))
        {
            main.getUtilLogger().logLevel(LogLevel.WARNING, "LoginRedirect> Player: " + e.getPlayer().getName() + " was not redirected as the spawn is not set in the config!");
        }
        else
        {
            VanityManager.equipHotbar(p);
        }
        HubManager.sendTitles(p);
        if (UtilPlayer.hasRank(p, Rank.TITAN))
        {
            p.getWorld().playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 100F, 1F);
        }
        if (UtilPlayer.hasRank(p, Rank.VIP))
        {
            Firework firework = (Firework)p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
            FireworkMeta fireworkmeta = firework.getFireworkMeta();
            FireworkEffect effect = FireworkEffect.builder()
                    .with(Type.BALL_LARGE)
                    .flicker(true)
                    .withFade(Color.RED)
                    .withColor(Color.YELLOW,Color.ORANGE)
                    .build();
            FireworkEffect effect2 = FireworkEffect.builder()
                    .with(Type.BALL)
                    .withColor(Color.YELLOW,Color.ORANGE)
                    .withFade(Color.RED)
                    .trail(true)
                    .build();
            FireworkEffect effect3 = FireworkEffect.builder()
                    .with(Type.STAR)
                    .withColor(Color.RED,Color.PURPLE)
                    .withFade(Color.FUCHSIA)
                    .trail(true)
                    .build();

            fireworkmeta.addEffects(effect, effect2, effect3);
            fireworkmeta.setPower(1);
            firework.setFireworkMeta(fireworkmeta);

            AnnounceMSG.toWorld(p.getWorld(), Rank.getRankPrefix(UtilPlayer.getRank(p)) + CC.WHITE + " " + p.getName() + CC.tnInfo + CC.WHITE + " has joined!");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e)
    {
        Player p = e.getPlayer();
        e.setQuitMessage(null);
        VanityManager.handleEnvChange(p);
        TutorialManager.handleLogout(p);
        HubManager.clearAllowedChat(p);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e)
    {
        VanityManager.handleEnvChange(e.getEntity());
    }

    @EventHandler
    public void onThrowEgg(PlayerEggThrowEvent e)
    {
        if (e.getPlayer().getWorld().getName().startsWith("hub"))
        {
            e.setHatching(false);
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent e)
    {
        if (e.getPlayer().getWorld().getName().startsWith("hub"))
        {
            TutorialManager.handleInteract(e);
            GadgetManager.handleEvent(e);
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e)
    {
        if (!e.getPlayer().getWorld().getName().startsWith("hub"))
        {
            return;
        }
        GadgetManager.handleEvent(e);
        Player p = e.getPlayer();
        if (e.getItem().getItemStack().getType() == Material.MELON)
        {
            if (e.getItem().hasMetadata("Ability"))
            {
                e.setCancelled(true);
                e.getItem().remove();
                p.getWorld().playSound(p.getLocation(), Sound.EAT, 1F, 1F);
                PotionEffect effect = new PotionEffect(PotionEffectType.SPEED, 60, 2);
                p.addPotionEffect(effect, true);
            }
        }
        if (!UtilPlayer.hasRank(p, Rank.ADMIN))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent e)
    {
        final Player p = e.getPlayer();
        VanityManager.handleEnvChange(p);
        TutorialManager.handleLogout(p);
        if (p.getWorld().getName().startsWith("hub"))
        {
            UtilPlayer.clearInv(p);
            p.updateInventory();
            new BukkitRunnable() {

                @Override
                public void run()
                {
                    VanityManager.equipHotbar(p);
                }
            }.runTask(main);

        }
    }

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent e)
    {
        Player p = e.getPlayer();
        if ((!p.getWorld().getName().startsWith("hub")) || (p.getGameMode() == GameMode.CREATIVE))
        {
            return;
        }
        if (TutorialManager.inTutorial(p))
        {
            e.setCancelled(true);
            return;
        }

        e.setCancelled(true);
        p.setAllowFlight(false);
        p.setFlying(false);
        Vector vector = p.getLocation().getDirection().multiply(2.7D).setY(1.0D);
        p.setVelocity(vector);
        p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 5F, 0.5F);
        for (int a = 0; a < 5; a++)
        {
            p.getWorld().playEffect(p.getLocation(), Effect.SPELL, null);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e)
    {
        Player p = e.getPlayer();
        if ((DisguiseManager.hasActiveDisguise(p)) && (e.getCause() != TeleportCause.ENDER_PEARL))
        {
            DisguiseManager.removeDisguise(p, false);
        }
        if (!p.getWorld().getName().startsWith("hub"))
        {
            return;
        }
        if (e.getCause() == TeleportCause.ENDER_PEARL)
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        if (!e.getPlayer().getWorld().getName().startsWith("hub"))
        {
            return;
        }
        GadgetManager.handleEvent(e);
        DisguiseManager.handeInteractEvent(e);
        VanityManager.handleInteract(e);
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            Block clicked = e.getClickedBlock();
            if (clicked.getType() == Material.JUKEBOX)
            {
                e.setCancelled(true);
            }
            if (!UtilPlayer.hasRank(e.getPlayer(), Rank.ADMIN))
            {
                if ((clicked.getType() == Material.WOODEN_DOOR) ||
                        (clicked.getType() == Material.TRAP_DOOR) ||
                        (clicked.getType() == Material.WOOD_BUTTON) ||
                        (clicked.getType() == Material.LEVER) ||
                        (clicked.getType() == Material.STONE_BUTTON) ||
                        (clicked.getType() == Material.CHEST) ||
                        (clicked.getType() == Material.ENDER_CHEST))
                {
                    e.setCancelled(true);
                }
            }
        }

    }

    @EventHandler
    public void onMove(PlayerMoveEvent e)
    {
        Player p = e.getPlayer();
        if ((p.getGameMode() != GameMode.CREATIVE) && (p.getWorld().getName().startsWith("hub")) &&
                (p.getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() != Material.AIR) &&
                (!p.isFlying()))
        {
            p.setAllowFlight(true);
        }
        if (!HubManager.isAllowedChat(p))
        {
            if (e.getTo().distance(e.getFrom()) >= 0.2)
            {
                if ((HubManager.getChatRegion() != null) && (!HubManager.getChatRegion().containsBlock(p.getLocation())))
                {
                    HubManager.allowedChat(p);
                }
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e)
    {
        final Player p = e.getPlayer();
        if (p.getWorld().getName().startsWith("hub"))
        {
            if (UtilPlayer.hasRank(p, Rank.ADMIN))
            {
                return;
            }
            e.setCancelled(true);
            p.updateInventory();
            UtilPlayer.clearInv(p);
            new BukkitRunnable() {

                @Override
                public void run()
                {
                    VanityManager.equipHotbar(p);
                }
            }.runTask(main);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCMDProcess(PlayerCommandPreprocessEvent e)
    {
        Player p = e.getPlayer();
        String commandname = e.getMessage().substring(1);
        String[] args = null;
        if (commandname.contains(" "))
        {
            commandname = commandname.split(" ")[0];
            args = e.getMessage().substring(e.getMessage().indexOf(" ") + 1).split(" ");
        }
        if ((commandname.equalsIgnoreCase("hub")) || (commandname.equalsIgnoreCase("lobby")))
        {
            if ((p.getWorld().getName().startsWith("hub")) || (p.getWorld().getName().startsWith("Staff")))
            {
                if (args != null)
                {
                    main.getHubCMD().execute(p, args);
                }
                else
                {
                    main.getHubCMD().execute(p, new String[] {});
                }
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e)
    {
        Player p = e.getPlayer();
        if (UtilPlayer.hasRank(p, Rank.MODERATOR))
        {
            return;
        }
        if (!HubManager.isAllowedChat(p))
        {
            UtilPlayer.message(Category.CHAT, p, CC.tnError + "You must jump off the tower before you can chat!");
            e.setCancelled(true);
        }
    }

}
