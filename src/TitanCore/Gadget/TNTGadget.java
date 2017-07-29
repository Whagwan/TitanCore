package TitanCore.Gadget;

import TitanCore.Hub.TutorialManager;
import TitanCore.Vanity.VanityItem;
import TitanCore.Vanity.VanityManager;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import titancoreapi.API.Cooldown.Cooldown;
import titancoreapi.API.Util.CC;
import titancoreapi.API.Util.ItemStackFactory;
import titancoreapi.API.Util.UtilPlayer;
import titancoreapi.Core.Items;
import titancoreapi.Core.Rank;

public class TNTGadget extends Gadget implements VanityItem {

    public TNTGadget(Player owner) {
        super(owner, new ItemStackFactory().createItemStack(Items.TNT, CC.tnAbility + "TNT"));
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
            if (Cooldown.isPlayeronCooldown(p, "TNT"))
            {
                UtilPlayer.onc(p, "TNT");
                return;
            }
            if (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(getItem().getItemMeta().getDisplayName()))
            {
                TNTPrimed tnt = (TNTPrimed)p.getWorld().spawnEntity(p.getLocation(), EntityType.PRIMED_TNT);
                tnt.setVelocity(p.getLocation().getDirection().multiply(0.8D).setY(0.2D));
                VanityManager.useAbility(p, "TNT");
                Cooldown.addCooldown(p, "TNT", 5);
            }
        }
        if (e instanceof EntityDamageByEntityEvent)
        {
            EntityDamageByEntityEvent event = (EntityDamageByEntityEvent)e;
            if (event.getCause() != DamageCause.ENTITY_EXPLOSION)
            {
                return;
            }
            if (!(event.getEntity() instanceof Player))
            {
                return;
            }
            if (!(event.getDamager() instanceof TNTPrimed))
            {
                return;
            }
            Player p = (Player)event.getEntity();
            if (TutorialManager.inTutorial(p))
            {
                event.setCancelled(true);
                return;
            }
            event.setCancelled(true);

            Location tntloc = event.getDamager().getLocation();
            Location ploc = p.getLocation();

            double x = ploc.getX() - tntloc.getX();
            double y = ploc.getY() - tntloc.getY();
            double z = ploc.getZ() - tntloc.getZ();

            Vector vec = new Vector(x, y, z);
            vec.normalize();
            vec.multiply(2.5D);
            vec.setY(2.5D);
            p.setVelocity(vec);
        }
        return;

    }

    @Override
    public String getItemName() {
        return "TNT";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStackFactory().createItemStackwithLore(Items.TNT, CC.tnGUIHead + "Equip " + getItemName(), getDescription());
    }

    @Override
    public ItemStack getActiveIcon() {
        return new ItemStackFactory().createItemStackwithGlow(CC.tnDisable + CC.BOLD + "Unequip " + CC.tnGUIHead + getItemName(), Items.TNT);
    }

    @Override
    public String[] getDescription() {
        return new String[] {CC.tnInfo + "Equips you with throwable TNT", CC.tnInfo + "which blasts players into", CC.tnInfo + "the sky on explosion!", CC.tnUse + "Right Click" + CC.tnInfo + " to use " + CC.tnAbility + "TNT"};
    }

    @Override
    public String getPermRequired() {
        return "tnt";
    }

    @Override
    public Rank getExclusiveRank() {
        return Rank.VIP;
    }

}
