package TitanCore.Disguise;

import TitanCore.GUIs.VanityMenu;
import TitanCore.TCHub;
import TitanCore.Util.PermsHandler;
import TitanCore.Vanity.VanityItem;
import TitanCore.Vanity.VanityManager;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import titancoreapi.API.Cooldown.SilentCooldown;

import java.util.HashMap;

public class DisguiseManager {

    private static TCHub main = TCHub.getInstance();
    private static HashMap<String, Disguise> disguises = new HashMap<String, Disguise>();

    public static void addDisguise(Player p, Disguise disguise)
    {
        if (disguises.containsKey(p.getName()))
        {
            removeDisguise(p, true);
        }
        if (PermsHandler.checkAccess(p, ((VanityItem)disguise).getPermRequired()))
        {
            disguises.put(p.getName(), disguise);
            disguises.get(p.getName()).disguise();
            String newdisguise = ((VanityItem)disguise).getItemName();
            VanityManager.equipMessage(p, newdisguise);
        }
        else
        {
            VanityManager.notUnlockedMessage(p);
        }
    }

    public static void removeDisguise(Player p, boolean silent)
    {
        if (!disguises.containsKey(p.getName()))
        {
            return;
        }
        String previousdisguise = ((VanityItem)disguises.get(p.getName())).getItemName();
        disguises.get(p.getName()).undisguise();
        if (!silent)
        {
            VanityManager.unequipMessage(p, previousdisguise);
        }
        disguises.remove(p.getName());
    }

    public static boolean hasActiveDisguise(Player p)
    {
        return disguises.containsKey(p.getName());
    }

    public static Disguise getActiveDisguise(Player p)
    {
        if (disguises.containsKey(p.getName()))
        {
            return disguises.get(p.getName());
        }
        return null;
    }

    public static VanityItem getActiveItem(Player p)
    {
        if (disguises.containsKey(p.getName()))
        {
            return (VanityItem)disguises.get(p.getName());
        }
        return null;
    }

    public static void handeInteractEvent(PlayerInteractEvent e)
    {
        if ((e.getAction() != Action.LEFT_CLICK_AIR) && (e.getAction() != Action.LEFT_CLICK_BLOCK))
        {
            return;
        }
        Player p = e.getPlayer();
        if (SilentCooldown.isPlayeronSilentCooldown(p, "DisguiseSound"))
        {
            return;
        }
        for (Disguise disguise : disguises.values())
        {
            if (disguise.getOwner().getName().equalsIgnoreCase(p.getName()))
            {
                disguise.handleRightClick();
                SilentCooldown.addSilentCooldown(p, "DisguiseSound", 10);
                return;
            }
        }
    }

    public static void handleInvClick(InventoryClickEvent e)
    {
        if (e.getInventory().getTitle().equalsIgnoreCase("Disguises"))
        {
            e.setCancelled(true);
            if (!(e.getWhoClicked() instanceof Player))
            {
                return;
            }
            final Player p = (Player)e.getWhoClicked();
            ItemStack clicked = e.getCurrentItem();
            if ((clicked != null) && (clicked.hasItemMeta()))
            {
                String disguise = null;
                for (VanityItem item : VanityManager.getAvailableDisguises())
                {
                    if (item.getIcon().getItemMeta().getDisplayName().equalsIgnoreCase(clicked.getItemMeta().getDisplayName()))
                    {
                        disguise = item.getItemName();
                        break;
                    }
                    else if (item.getActiveIcon().getItemMeta().getDisplayName().equalsIgnoreCase(clicked.getItemMeta().getDisplayName()))
                    {
                        disguise = "rem";
                        break;
                    }
                    else if (VanityManager.vanityBack().getItemMeta().getDisplayName().equalsIgnoreCase(clicked.getItemMeta().getDisplayName()))
                    {
                        VanityManager.openGUI(p, new VanityMenu(p));
                        break;
                    }
                }
                if (disguise != null)
                {
                    switch(disguise.toLowerCase().replace(" ", ""))
                    {
                        case("blazedisguise"):
                            addDisguise(p, new BlazeDisguise(p));
                            break;
                        case("chickendisguise"):
                            addDisguise(p, new ChickenDisguise(p));
                            break;
                        case("cowdisguise"):
                            addDisguise(p, new CowDisguise(p));
                            break;
                        case("creeperdisguise"):
                            addDisguise(p, new CreeperDisguise(p));
                            break;
                        case("endermandisguise"):
                            addDisguise(p, new EndermanDisguise(p));
                            break;
                        case("pigdisguise"):
                            addDisguise(p, new PigDisguise(p));
                            break;
                        case("rem"):
                            removeDisguise(p, false);
                            break;
                    }
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            p.closeInventory();
                        }
                    }.runTask(main);
                }

            }
        }
    }

}