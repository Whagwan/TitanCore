package TitanCore.Gadget;

import TitanCore.TCHub;
import TitanCore.Vanity.VanityItem;
import TitanCore.Vanity.VanityManager;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import titancoreapi.API.Cooldown.Cooldown;
import titancoreapi.API.Util.CC;
import titancoreapi.API.Util.ItemStackFactory;
import titancoreapi.API.Util.UtilPlayer;
import titancoreapi.Core.Items;
import titancoreapi.Core.Rank;

import java.util.Random;

public class WitherLauncherGadget extends Gadget implements VanityItem{

    private TCHub main = TCHub.getInstance();

    public WitherLauncherGadget(Player owner) {
        super(owner, new ItemStackFactory().createItemStack(Items.FIRECHARGE, CC.tnAbility + "Wither Launcher"));
    }

    @Override
    public void handleEvent(Event e) {
        if (e instanceof PlayerInteractEvent)
        {
            PlayerInteractEvent event = (PlayerInteractEvent)e;
            Player p = event.getPlayer();
            if ((event.getAction() != Action.RIGHT_CLICK_AIR) && (event.getAction() != Action.RIGHT_CLICK_BLOCK))
            {
                return;
            }
            if (!p.getItemInHand().hasItemMeta())
            {
                return;
            }
            if (p.getItemInHand().getType() != getItem().getType())
            {
                return;
            }
            if (!p.getName().equalsIgnoreCase(getOwner().getName()))
            {
                return;
            }
            if (Cooldown.isPlayeronCooldown(p, "Wither Launcher"))
            {
                UtilPlayer.onc(p, "Wither Launcher");
                return;
            }
            if (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(getItem().getItemMeta().getDisplayName()))
            {
                p.launchProjectile(WitherSkull.class, p.getLocation().getDirection().multiply(1.0D));
                p.getWorld().playSound(p.getLocation(), Sound.WITHER_SHOOT, 1.2F, 1F);
                VanityManager.useAbility(p, "Wither Launcher");
                Cooldown.addCooldown(p, "Wither Launcher", 8);
            }
        }
        if (e instanceof ProjectileLaunchEvent)
        {
            ProjectileLaunchEvent event = (ProjectileLaunchEvent)e;
            if (!(event.getEntity() instanceof WitherSkull))
            {
                return;
            }
            if (!(event.getEntity().getShooter() instanceof Player))
            {
                return;
            }
            Player p = (Player)event.getEntity().getShooter();
            if (!p.getName().equalsIgnoreCase(getOwner().getName()))
            {
                return;
            }
            if (!p.getItemInHand().hasItemMeta())
            {
                return;
            }
            if (p.getItemInHand().getType() == getItem().getType())
            {
                WitherSkull witherskull = (WitherSkull) event.getEntity();
                witherskull.setMetadata("Ability", new FixedMetadataValue(main, "Wither Launcher"));
            }
        }
        if (e instanceof ProjectileHitEvent)
        {
            ProjectileHitEvent event = (ProjectileHitEvent)e;
            if (!(event.getEntity() instanceof WitherSkull))
            {
                return;
            }
            WitherSkull witherskull = (WitherSkull)event.getEntity();
            if (!witherskull.hasMetadata("Ability"))
            {
                return;
            }
            if (!(witherskull.getShooter() instanceof Player))
            {
                return;
            }
            Player p = (Player)witherskull.getShooter();
            if (!p.getName().equalsIgnoreCase(getOwner().getName()))
            {
                return;
            }
            if (witherskull.getMetadata("Ability").get(0).asString().equalsIgnoreCase("Wither Launcher"))
            {
                final Location loc = witherskull.getLocation();
                new BukkitRunnable() {
                    int radius = 1;
                    double y = 0;
                    @Override
                    public void run() {

                        for (double i = 0; i < 4.0; i += 0.8)
                        {
                            double x = radius * Math.cos(y);
                            double z = radius * Math.sin(y);
                            Location part = new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y - i, loc.getZ() + z);
                            loc.getWorld().spigot().playEffect(part, Effect.WITCH_MAGIC, 0, 0, 0F, 0F, 0F, 0F, 1, 50);

                        }

                        Random r = new Random();
                        double randomy = 0.04 + (0.1 - 0.04) * r.nextDouble();
                        y += randomy;

                        if (y >= 6)
                        {
                            cancel();
                        }

                    }
                }.runTaskTimer(main, 0L, 1L);
                loc.getWorld().playSound(loc, Sound.ENDERDRAGON_HIT, 2F, 0.5F);
                loc.getWorld().playEffect(loc, Effect.EXPLOSION_LARGE, 0);
            }
        }
    }

    @Override
    public String getItemName() {
        return "Wither Launcher";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStackFactory().createItemStackwithLore(Items.FIRECHARGE, CC.tnGUIHead + "Equip " + getItemName(), getDescription());
    }

    @Override
    public ItemStack getActiveIcon() {
        return new ItemStackFactory().createItemStackwithGlow(CC.tnDisable + CC.BOLD + "Unequip " + CC.tnGUIHead + getItemName(), Items.FIRECHARGE);
    }

    @Override
    public String[] getDescription() {
        return new String[] {CC.tnInfo + "Launch wither skulls that", CC.tnInfo + "explode and give off", CC.tnInfo + "cool particles!", CC.tnUse + "Right Click" + CC.tnInfo + " to use " + CC.tnAbility + "Wither Launcher"};
    }

    @Override
    public String getPermRequired() {
        return "witherlauncher";
    }

    @Override
    public Rank getExclusiveRank() {
        return Rank.TITAN;
    }

}