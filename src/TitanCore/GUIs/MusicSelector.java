package TitanCore.GUIs;


import java.util.List;

import TitanCore.Util.PermsHandler;
import org.bukkit.entity.Player;

import TitanCore.Music.MusicManager;
import TitanCore.Vanity.VanityItem;
import TitanCore.Vanity.VanityManager;

public class MusicSelector extends GUI{

    public MusicSelector(Player p) {
        super("Music", 44, p);
    }

    @Override
    public void open() {
        Player p = getViewer();
        List<VanityItem> icons = VanityManager.getAvailableMusic();

        int index = 11;

        for (VanityItem icon : icons)
        {
            if ((MusicManager.getActiveItem(p) != null) && (icon.getActiveIcon().getItemMeta().getDisplayName().equalsIgnoreCase(MusicManager.getActiveItem(p).getActiveIcon().getItemMeta().getDisplayName())))
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
            if (index == 25)
            {
                index = 29;
            }
        }

        setIcon(4, VanityManager.vanityBack());
        p.openInventory(getMenu());
        p.updateInventory();
    }

}