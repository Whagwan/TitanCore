package TitanCore.Gadget;

import TitanCore.TCHub;
import TitanCore.Vanity.VanityItem;
import TitanCore.Vanity.VanityManager;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import titancoreapi.API.Cooldown.SilentCooldown;
import titancoreapi.API.Util.CC;
import titancoreapi.API.Util.ItemStackFactory;
import titancoreapi.Core.Items;
import titancoreapi.Core.Rank;

public class FunGunGadget extends Gadget implements VanityItem {

    public FunGunGadget(Player owner) {
        super(owner, new ItemStackFactory().createItemStack(Items.DIAMONDBARDING, CC.tnValue + "Fun Gun"));
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
            if (!p.getName().equalsIgnoreCase(getOwner().getName()))
            {
                return;
            }
            if ((!p.getItemInHand().hasItemMeta()) || (p.getItemInHand().getType() != getItem().getType()) || (p.getItemInHand() == null))
            {
                return;
            }
            if (SilentCooldown.isPlayeronSilentCooldown(p, "Fun Gun"))
            {
                return;
            }
            if (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(getItem().getItemMeta().getDisplayName()))
            {
                for (int i = 0; i < 4; i++)
                {
                    p.launchProjectile(Snowball.class);
                }
                p.playSound(p.getLocation(), Sound.PISTON_EXTEND, 4F, 2F);
                VanityManager.useAbility(p, "Fun Gun");
                SilentCooldown.addSilentCooldown(p, "Fun Gun", 8);
            }
        }

        if (e instanceof ProjectileLaunchEvent)
        {
            ProjectileLaunchEvent event = (ProjectileLaunchEvent)e;
            if (!(event.getEntity().getShooter() instanceof Player))
            {
                return;
            }
            if (!(event.getEntity() instanceof Snowball))
            {
                return;
            }
            Player p = (Player)event.getEntity().getShooter();
            if (!p.getName().equalsIgnoreCase(getOwner().getName()))
            {
                return;
            }
            if ((!p.getItemInHand().hasItemMeta()) || (p.getItemInHand().getType() != getItem().getType()) || (p.getItemInHand() == null))
            {
                return;
            }
            if (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(getItem().getItemMeta().getDisplayName()))
            {
                event.getEntity().setMetadata("Ability", new FixedMetadataValue(TCHub.getInstance(), "Fun Gun"));
            }
        }

        if (e instanceof ProjectileHitEvent)
        {
            ProjectileHitEvent event = (ProjectileHitEvent)e;
            if (!(event.getEntity() instanceof Snowball))
            {
                return;
            }
            if (!event.getEntity().hasMetadata("Ability"))
            {
                return;
            }
            Snowball snowball = (Snowball)event.getEntity();
            if (!(snowball.getShooter() instanceof Player))
            {
                return;
            }
            Player p = (Player)snowball.getShooter();
            if (!p.getName().equalsIgnoreCase(getOwner().getName()))
            {
                return;
            }
            if (!snowball.getMetadata("Ability").get(0).asString().equalsIgnoreCase("Fun Gun"))
            {
                return;
            }
            Location loc = snowball.getLocation();
            loc.getWorld().spigot().playEffect(loc, Effect.LAVA_POP, 0, 0, 0.5F, 0F, 0.5F, 1F, 20, 30);
            loc.getWorld().spigot().playEffect(loc, Effect.PORTAL, 0, 0, 0.5F, 0F, 0.5F, 1F, 20, 30);
            loc.getWorld().spigot().playEffect(loc, Effect.FIREWORKS_SPARK, 0, 0, 0.5F, 0F, 0.5F, 1F, 20, 30);
            loc.getWorld().playSound(loc, Sound.FIREWORK_TWINKLE, 2F, 2F);
        }
    }

    @Override
    public String getItemName() {
        return "Fun Gun";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStackFactory().createItemStackwithLore(Items.DIAMONDBARDING, CC.tnGUIHead + "Equip " + getItemName(), getDescription());
    }

    @Override
    public ItemStack getActiveIcon() {
        return new ItemStackFactory().createItemStackwithGlow(CC.tnDisable + CC.BOLD + "Unequip " + CC.tnGUIHead + getItemName(), Items.DIAMONDBARDING);
    }

    @Override
    public String[] getDescription() {
        return new String[] {CC.tnInfo + "This nifty little gadget", CC.tnInfo + "allows you to shoot", CC.tnInfo + "snowballs that give off", CC.tnInfo + "cool particles on hit.", CC.tnUse + "Right Click" + CC.tnInfo + " to use " + CC.tnAbility + "Fun Gun"};
    }

    @Override
    public String getPermRequired() {
        return "fungun";
    }

    @Override
    public Rank getExclusiveRank() {
        return Rank.VIP;
    }
}

