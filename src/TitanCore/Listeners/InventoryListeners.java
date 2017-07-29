package TitanCore.Listeners;

import TitanCore.Disguise.DisguiseManager;
import TitanCore.Gadget.GadgetManager;
import TitanCore.Hub.TutorialManager;
import TitanCore.Music.MusicManager;
import TitanCore.Particle.ParticleManager;
import TitanCore.Pet.PetManager;
import TitanCore.Vanity.VanityManager;
import TitanCore.Vanity.WardrobeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import titancoreapi.API.Util.UtilPlayer;
import titancoreapi.Core.Rank;

public class InventoryListeners implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInvClick(InventoryClickEvent e)
    {
        if (!e.getWhoClicked().getWorld().getName().startsWith("hub"))
        {
            return;
        }
        if (!(e.getWhoClicked() instanceof Player))
        {
            return;
        }
        Player p = (Player)e.getWhoClicked();
        if ((!UtilPlayer.hasRank(p, Rank.ADMIN)) && (e.getWhoClicked() instanceof Player) && (e.getWhoClicked().getWorld().getName().startsWith("hub")))
        {
            e.setCancelled(true);
        }
        if (e.getInventory().getTitle().equalsIgnoreCase("Pets"))
        {
            PetManager.handleInvClick(e);
        }
        if (e.getInventory().getTitle().equalsIgnoreCase("Particles"))
        {
            ParticleManager.handleInvClick(e);
        }
        if (e.getInventory().getTitle().equalsIgnoreCase("Music"))
        {
            MusicManager.handleInvClick(e);
        }
        if (e.getInventory().getTitle().equalsIgnoreCase("Gadgets"))
        {
            GadgetManager.handleInvClick(e);
        }
        if (e.getInventory().getTitle().equalsIgnoreCase("Disguises"))
        {
            DisguiseManager.handleInvClick(e);
        }
        if (e.getInventory().getTitle().equalsIgnoreCase("Wardrobe"))
        {
            WardrobeManager.handleInvClick(e);
        }
        if ((e.getInventory().getTitle().equalsIgnoreCase("Vanity Menu")) || (e.getInventory().getTitle().equalsIgnoreCase("Profile")) || (e.getInventory().getTitle().equalsIgnoreCase("Gamemode Selector")))
        {
            VanityManager.handleInvClick(e);
        }
    }

    @EventHandler
    public void onInvOpen(InventoryOpenEvent e)
    {
        if (!(e.getPlayer() instanceof Player))
        {
            return;
        }
        Player p = (Player)e.getPlayer();
        if (TutorialManager.inTutorial(p))
        {
            e.setCancelled(true);
        }
        if (e.getInventory().getType() == InventoryType.MERCHANT)
        {
            e.setCancelled(true);
        }
    }


}
