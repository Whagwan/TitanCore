package TitanCore.GUIs;

package TitanCore.GUIs;

        import java.util.List;

        import TitanCore.Gadget.GadgetManager;
        import org.bukkit.entity.Player;

        import TitanCore.Util.PermsHandler;
        import TitanCore.Vanity.VanityItem;
        import TitanCore.Vanity.VanityManager;

public class GadgetSelector extends GUI{

    public GadgetSelector(Player p) {
        super("Gadgets", 35, p);

    }

    @Override
    public void open() {

        Player p = getViewer();
        List<VanityItem> icons = VanityManager.getAvailableGadgets();

        int index = 11;

        for (VanityItem icon : icons)
        {
            if ((GadgetManager.getActiveItem(p) != null) && (icon.getActiveIcon().getItemMeta().getDisplayName().equalsIgnoreCase(GadgetManager.getActiveItem(p).getActiveIcon().getItemMeta().getDisplayName())))
            {
                setActiveIcon(index, icon);
            }
            else if (PermsHandler.checkAccess(p, icon.getPermRequired()))
            {
                setIconUnlocked(index, icon);
            }
            else
            {
                if (icon.getExclusiveRank() != null)
                {
                    setIconUnlockRank(index, icon);
                }
                else
                {
                    setIconUnlockStore(index, icon);
                }
            }
            index++;
            if (index == 16)
            {
                index = 20;
            }
        }

        setIcon(4, VanityManager.vanityBack());
        p.openInventory(getMenu());
        p.updateInventory();
    }

}