package TitanCore.Gadget;

import TitanCore.GUIs.VanityMenu;
import TitanCore.TCHub;
import TitanCore.Util.PermsHandler;
import TitanCore.Vanity.VanityItem;
import TitanCore.Vanity.VanityManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class GadgetManager {

    private static TCHub main = TCHub.getInstance();
    private static HashMap<String, Gadget> gadgets = new HashMap<String, Gadget>();

    public static void addGadget(Player p, Gadget gadget)
    {
        if (gadgets.containsKey(p.getName()))
        {
            removeGadget(p, true);
        }
        if (PermsHandler.checkAccess(p, ((VanityItem)gadget).getPermRequired()))
        {
            gadgets.put(p.getName(), gadget);
            gadgets.get(p.getName()).equip();
            String newgadget = ((VanityItem)gadget).getItemName();
            VanityManager.equipMessage(p, newgadget);
        }
        else
        {
            VanityManager.notUnlockedMessage(p);
        }
    }

    public static void removeGadget(Player p, boolean silent)
    {
        if (!gadgets.containsKey(p.getName()))
        {
            return;
        }
        String previousgadget = ((VanityItem)gadgets.get(p.getName())).getItemName();
        gadgets.get(p.getName()).unequip();
        if (!silent)
        {
            VanityManager.unequipMessage(p, previousgadget);
        }
        gadgets.remove(p.getName());
    }

    public static boolean hasActiveGadget(Player p)
    {
        return gadgets.containsKey(p.getName());
    }

    public static Gadget getActiveGadget(Player p)
    {
        if (gadgets.containsKey(p.getName()))
        {
            return gadgets.get(p.getName());
        }
        return null;
    }

    public static VanityItem getActiveItem(Player p)
    {
        if (gadgets.containsKey(p.getName()))
        {
            return (VanityItem)gadgets.get(p.getName());
        }
        return null;
    }

    public static void handleEvent(Event e)
    {
        for (Gadget activegadgets : gadgets.values())
        {
            activegadgets.handleEvent(e);
        }
    }

    public static void handleInvClick(InventoryClickEvent e)
    {
        if (e.getInventory().getTitle().equalsIgnoreCase("Gadgets"))
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
                String gadget = null;
                for (VanityItem item : VanityManager.getAvailableGadgets())
                {
                    if (item.getIcon().getItemMeta().getDisplayName().equalsIgnoreCase(clicked.getItemMeta().getDisplayName()))
                    {
                        gadget = item.getItemName();
                        break;
                    }
                    else if (item.getActiveIcon().getItemMeta().getDisplayName().equalsIgnoreCase(clicked.getItemMeta().getDisplayName()))
                    {
                        gadget = "rem";
                        break;
                    }
                    else if (VanityManager.vanityBack().getItemMeta().getDisplayName().equalsIgnoreCase(clicked.getItemMeta().getDisplayName()))
                    {
                        VanityManager.openGUI(p, new VanityMenu(p));
                        break;
                    }
                }
                if (gadget != null)
                {
                    switch(gadget.toLowerCase().replace(" ", ""))
                    {
                        case("fireworklauncher"):
                            addGadget(p, new FireworkLauncherGadget(p));
                            break;
                        case("fungun"):
                            addGadget(p, new FunGunGadget(p));
                            break;
                        case("melonlauncher"):
                            addGadget(p, new MelonLauncherGadget(p));
                            break;
                        case("smite"):
                            addGadget(p, new SmiteGadget(p));
                            break;
                        case("speedgun"):
                            addGadget(p, new SpeedGunGadget(p));
                            break;
                        case("tnt"):
                            addGadget(p, new TNTGadget(p));
                            break;
                        case("witherlauncher"):
                            addGadget(p, new WitherLauncherGadget(p));
                            break;
                        case("pearlflight"):
                            addGadget(p, new PearlFlightGadget(p));
                            break;
                        case("rem"):
                            removeGadget(p, false);
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