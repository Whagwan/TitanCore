package TitanCore.Listeners;

import TitanCore.Gadget.GadgetManager;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;

import TitanCore.TCHub;

public class EntityListeners implements Listener {

    private TCHub main = TCHub.getInstance();

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamage(EntityDamageEvent e)
    {
        if (!e.getEntity().getWorld().getName().startsWith("hub"))
        {
            return;
        }
        if (e.getEntity().hasMetadata("Tutorial"))
        {
            if (e.getCause() != DamageCause.SUICIDE)
            {
                e.setCancelled(true);
            }
        }
        if (e.getCause() == DamageCause.VOID)
        {
            if (e.getEntity().getCustomName() != null)
            {
                if (!e.getEntity().getCustomName().contains("Pet"))
                {
                    e.setCancelled(true);
                    e.getEntity().teleport(e.getEntity().getLocation().add(0, 15, 0));
                }
            }
        }
        if ((e.getCause() != DamageCause.CUSTOM) && (e.getCause() != DamageCause.SUICIDE))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void combust(EntityCombustEvent e)
    {
        if (e.getEntity().hasMetadata("Tutorial"))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void chunkunload(ChunkUnloadEvent e)
    {
        for (Entity ent : e.getChunk().getEntities())
        {
            if (ent.hasMetadata("Tutorial"))
            {
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onDismount(EntityDismountEvent e)
    {
        if (e.getEntity() instanceof EnderPearl)
        {
            final EnderPearl ep = (EnderPearl)e.getEntity();
            new BukkitRunnable() {
                @Override
                public void run() {
                    ep.remove();
                }
            }.runTaskLater(main, 3L);
        }
    }

    @EventHandler
    public void onEntityDamagebyEntity(EntityDamageByEntityEvent e)
    {
        if (!e.getEntity().getWorld().getName().startsWith("hub"))
        {
            return;
        }
        GadgetManager.handleEvent(e);

    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e)
    {
        if (e.getEntity() instanceof WitherSkull)
        {
            e.blockList().clear();
        }
    }

}
