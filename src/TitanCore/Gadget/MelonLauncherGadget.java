package TitanCore.Gadget;

import TitanCore.TCHub;
import TitanCore.Vanity.VanityItem;
import TitanCore.Vanity.VanityManager;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MiscDisguise;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import titancoreapi.API.Cooldown.SilentCooldown;
import titancoreapi.API.Util.CC;
import titancoreapi.API.Util.ItemStackFactory;
import titancoreapi.Core.Items;
import titancoreapi.Core.Rank;

import java.util.Random;

public class MelonLauncherGadget extends Gadget implements VanityItem {

    private TCHub main = TCHub.getInstance();

    public MelonLauncherGadget(Player owner) {
        super(owner, new ItemStackFactory().createItemStack(Items.MELONBLOCK, CC.tnValue + "Melon Launcher"));
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
            if (!p.getName().equalsIgnoreCase(getOwner().getName()))
            {
                return;
            }
            if (p.getItemInHand().getType() != getItem().getType())
            {
                return;
            }
            if (SilentCooldown.isPlayeronSilentCooldown(p, "Melon Launcher"))
            {
                return;
            }
            if (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(getItem().getItemMeta().getDisplayName()))
            {
                p.launchProjectile(Snowball.class);
                p.getWorld().playSound(p.getLocation(), Sound.ENDERDRAGON_HIT, 1.2F, 2F);
                VanityManager.useAbility(p, "Melon Launcher");
                SilentCooldown.addSilentCooldown(p, "Melon Launcher", 8);
            }
        }
        if (e instanceof ProjectileLaunchEvent)
        {
            ProjectileLaunchEvent event = (ProjectileLaunchEvent)e;
            if (!(event.getEntity() instanceof Snowball))
            {
                return;
            }
            if (!(event.getEntity().getShooter() instanceof Player))
            {
                return;
            }
            Player p = (Player)event.getEntity().getShooter();
            if (!p.getItemInHand().hasItemMeta())
            {
                return;
            }
            if (!p.getName().equalsIgnoreCase(getOwner().getName()))
            {
                return;
            }
            if (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(getItem().getItemMeta().getDisplayName()))
            {
                Snowball snowball = (Snowball)event.getEntity();
                snowball.setMetadata("Ability", new FixedMetadataValue(main, "Melon Launcher"));
                MiscDisguise dis = new MiscDisguise(DisguiseType.DROPPED_ITEM, 103);
                DisguiseAPI.disguiseToAll(snowball, dis);
            }
        }
        if (e instanceof ProjectileHitEvent)
        {
            ProjectileHitEvent event = (ProjectileHitEvent)e;
            if (!(event.getEntity() instanceof Snowball))
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
            if (!event.getEntity().hasMetadata("Ability"))
            {
                return;
            }
            if (event.getEntity().getMetadata("Ability").get(0).asString().equalsIgnoreCase("Melon Launcher"))
            {
                Snowball snowball = (Snowball)event.getEntity();
                snowball.getLocation().getWorld().playEffect(snowball.getLocation(), Effect.STEP_SOUND, Material.MELON_BLOCK);
                ItemStack melon = new ItemStackFactory().createItemStackwithGlow("SpeedMelon", Items.MELON);
                for (int a = 0; a < 6; a++)
                {
                    Item melondrop = snowball.getLocation().getWorld().dropItemNaturally(snowball.getLocation(), melon);
                    melondrop.setMetadata("Ability", new FixedMetadataValue(main, "Melon Launcher"));
                    Vector dir = melondrop.getLocation().getDirection();
                    double x = new Random().nextDouble() * (0.5D - 0.1D) + 0.1D;
                    double z = new Random().nextDouble() * (0.5D - 0.1D) + 0.1D;
                    Vector vector = new Vector(dir.getX() * x, 0.3D, dir.getZ() * z);
                    melondrop.setVelocity(vector);
                    melondrop.setFallDistance(-80.0F);
                }
            }
        }

    }

    @Override
    public String getItemName() {
        return "Melon Launcher";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStackFactory().createItemStackwithLore(Items.MELONBLOCK, CC.tnGUIHead + "Equip " + getItemName(), getDescription());
    }

    @Override
    public ItemStack getActiveIcon() {
        return new ItemStackFactory().createItemStackwithGlow(CC.tnDisable + CC.BOLD + "Unequip " + CC.tnGUIHead + getItemName(), Items.MELONBLOCK);
    }

    @Override
    public String[] getDescription() {
        return new String [] {CC.tnInfo + "Launch melons around the", CC.tnInfo + "Hub that give a speed", CC.tnInfo + "effect on pickup!", CC.tnUse + "Right Click" + CC.tnInfo + " to use " + CC.tnAbility + "Melon Launcher"};
    }

    @Override
    public String getPermRequired() {
        return "melonlauncher";
    }

    @Override
    public Rank getExclusiveRank() {
        return Rank.MVP;
    }
}