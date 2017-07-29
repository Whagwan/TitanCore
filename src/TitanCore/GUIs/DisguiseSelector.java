package TitanCore.GUIs;

import TitanCore.Disguise.DisguiseManager;
import TitanCore.Util.PermsHandler;
import TitanCore.Vanity.VanityItem;
import TitanCore.Vanity.VanityManager;
import org.bukkit.entity.Player;
import titancoreapi.API.Util.CC;
import titancoreapi.API.Util.ItemStackFactory;
import titancoreapi.Core.Items;

import java.util.List;

public class DisguiseSelector extends GUI{

    public DisguiseSelector(Player p) {
        super("Disguises", 26, p);
    }

    @Override
    public void open() {
        Player p = getViewer();

        List<VanityItem> icons = VanityManager.getAvailableDisguises();

        int index = 11;

        for (VanityItem icon : icons)
        {
            if ((DisguiseManager.getActiveDisguise(p) != null) && (icon.getActiveIcon().getItemMeta().getDisplayName().equalsIgnoreCase(DisguiseManager.getActiveItem(p).getActiveIcon().getItemMeta().getDisplayName())))
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
        }


        setIcon(4, VanityManager.vanityBack());
        setIcon(16, new ItemStackFactory().createItemStack(Items.REDSTAINEDGLASSPANE, CC.tnInfo + "More coming soon..."));
        p.openInventory(getMenu());
        p.updateInventory();

    }



}