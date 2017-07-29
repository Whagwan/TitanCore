package TitanCore.GUIs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import TitanCore.Vanity.VanityAccess;
import TitanCore.Vanity.VanityItem;

public abstract class GUI {

    private Inventory menu;
    private Player viewer;
    private int slots = 9;
    private String title;

    public GUI(String title, int slots, Player p)
    {
        this.viewer = p;
        this.slots = slots + 1;
        this.title = title;
        this.menu = Bukkit.createInventory(null, this.slots, this.title);
    }

    public void load() {}

    public abstract void open();

    protected void setActiveIcon(int index, VanityItem item)
    {
        this.menu.setItem(index, item.getActiveIcon());
    }

    protected void setIcon(int index, VanityItem item)
    {
        this.menu.setItem(index, item.getIcon());
    }

    protected void setIcon(int index, ItemStack icon)
    {
        this.menu.setItem(index, icon);
    }

    protected void setIconUnlocked(int index, VanityItem item)
    {
        this.menu.setItem(index, VanityAccess.unlocked(item.getIcon()));
    }

    protected void setIconUnlockStore(int index, VanityItem item)
    {
        this.menu.setItem(index, VanityAccess.unlockStore(item.getIcon()));
    }

    protected void setIconUnlockRank(int index, VanityItem item)
    {
        this.menu.setItem(index, VanityAccess.unlockRank(item.getIcon(), item.getExclusiveRank()));
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return this.title;
    }

    public Player getViewer()
    {
        return this.viewer;
    }

    public Inventory getMenu()
    {
        return this.menu;
    }

}

