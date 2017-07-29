package TitanCore.Gadget;


import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import titancoreapi.API.Cooldown.SilentCooldown;
import titancoreapi.API.Util.CC;
import titancoreapi.API.Util.ItemStackFactory;
import titancoreapi.Core.Items;
import titancoreapi.Core.Rank;
import TitanCore.TCHub;
import TitanCore.Vanity.VanityItem;
import TitanCore.Vanity.VanityManager;

public class SpeedGunGadget extends Gadget implements VanityItem{

    private TCHub main = TCHub.getInstance();

    public SpeedGunGadget(Player owner) {
        super(owner, new ItemStackFactory().createItemStack(Items.GOLDENBARDING, CC.tnValue + "Speed Gun"));
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
            if (p.getItemInHand().getType() != getItem().getType())
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
            if (SilentCooldown.isPlayeronSilentCooldown(p, "Speed Gun"))
            {
                return;
            }
            if (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(getItem().getItemMeta().getDisplayName()))
            {
                p.launchProjectile(Egg.class, p.getLocation().getDirection().multiply(1.0D));
                p.getWorld().playSound(p.getLocation(), Sound.ITEM_PICKUP, 1.2F, 2F);
                VanityManager.useAbility(p, "Speed Gun");
                SilentCooldown.addSilentCooldown(p, "Speed Gun", 10);
            }
        }
        if (e instanceof ProjectileLaunchEvent)
        {
            ProjectileLaunchEvent event = (ProjectileLaunchEvent)e;
            if (!(event.getEntity() instanceof Egg))
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
            if (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(getItem().getItemMeta().getDisplayName()))
            {
                Egg egg = (Egg)event.getEntity();

                egg.setMetadata("Ability", new FixedMetadataValue(main, "Speed Gun"));
            }
        }
        if (e instanceof ProjectileHitEvent)
        {
            ProjectileHitEvent event = (ProjectileHitEvent)e;
            if (!(event.getEntity() instanceof Egg))
            {
                return;
            }
            if (!event.getEntity().hasMetadata("Ability"))
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
            if (event.getEntity().getMetadata("Ability").get(0).asString().equalsIgnoreCase("Speed Gun"))
            {
                Egg egg = (Egg)event.getEntity();
                egg.getLocation().getWorld().spigot().playEffect(egg.getLocation(), Effect.HAPPY_VILLAGER, 0, 0, 3.0F, 0F, 3.0F, 0, 15, 40);
                egg.getLocation().getWorld().playSound(egg.getLocation(), Sound.FIREWORK_LAUNCH, 1.5F, 2F);
                for (Entity speed : egg.getNearbyEntities(3.0, 2.0, 3.0))
                {
                    if (speed instanceof Player)
                    {
                        PotionEffect effect = new PotionEffect(PotionEffectType.SPEED, 80, 1);
                        ((Player)speed).addPotionEffect(effect, true);
                    }
                }
            }
        }
    }

    @Override
    public String getItemName() {
        return "Speed Gun";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStackFactory().createItemStackwithLore(Items.GOLDENBARDING, CC.tnGUIHead + "Equip " + getItemName(), getDescription());
    }

    @Override
    public ItemStack getActiveIcon() {
        return new ItemStackFactory().createItemStackwithGlow(CC.tnDisable + CC.BOLD + "Unequip " + CC.tnGUIHead + getItemName(), Items.GOLDENBARDING);
    }

    @Override
    public String[] getDescription() {
        return new String[] {CC.tnInfo + "The speed gun fires", CC.tnInfo + "eggs that give a speed", CC.tnInfo + "effect to nearby players", CC.tnInfo + "on hit.", CC.tnUse + "Right Click" + CC.tnInfo + " to use " + CC.tnAbility + "Speed Gun"};
    }

    @Override
    public String getPermRequired() {
        return "speedgun";
    }

    @Override
    public Rank getExclusiveRank() {
        return Rank.MVP;
    }

}