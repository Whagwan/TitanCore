package TitanCore.GUIs;

import TitanCore.Vanity.VanityManager;
import TitanCore.Vanity.WardrobeManager;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import titancoreapi.API.Util.CC;
import titancoreapi.API.Util.ItemStackFactory;
import titancoreapi.Core.Items;

import java.util.ArrayList;
import java.util.List;

public class Wardrobe extends GUI{

    public Wardrobe(Player p) {
        super("Wardrobe", 44, p);
    }

    @Override
    public void open() {
        Color[] array = WardrobeManager.getColors();
        int index = 9;
        for (WardrobeManager.ArmorPart part : WardrobeManager.ArmorPart.values())
        {
            for (Color color : array)
            {
                setIcon(index, createLeatherArmor(color, part));
                index++;
                if (index == 17)
                {
                    index = 18;
                }
                if (index == 26)
                {
                    index = 27;
                }
                if (index == 35)
                {
                    index = 36;
                }
            }
        }
        setIcon(8, new ItemStackFactory().createItemStack(Items.BARRIER, CC.tnDisable + CC.BOLD + "Unequip " + CC.tnGUIHead + "all armor!"));
        setIcon(4, VanityManager.vanityBack());
        getViewer().openInventory(getMenu());
        getViewer().updateInventory();
    }


    private ItemStack createLeatherArmor(Color color, WardrobeManager.ArmorPart part)
    {
        Material mat = null;
        String partname = null;
        switch(part)
        {
            case HELMET:
                mat = Material.LEATHER_HELMET;
                partname = "Helmet";
                break;
            case CHESTPLATE:
                mat = Material.LEATHER_CHESTPLATE;
                partname = "Chestplate";
                break;
            case LEGGINGS:
                mat = Material.LEATHER_LEGGINGS;
                partname = "Leggings";
                break;
            case BOOTS:
                mat = Material.LEATHER_BOOTS;
                partname = "Boots";
                break;
        }
        if ((mat == null) || (partname == null))
        {
            return new ItemStackFactory().createItemStack(Items.REDSTONECOMPARATOR, CC.RED + ":( There was an error!");
        }
        ItemStack is = new ItemStack(mat);
        ItemMeta im = is.getItemMeta();
        ((LeatherArmorMeta)im).setColor(color);
        im.setDisplayName(getChar(color) + " §a§l" + partname);
        List<String> lore = new ArrayList<String>();
        lore.add(CC.tnInfo + "Click to equip!");
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    private String getChar(Color color)
    {

        if (color == Color.RED)
        {
            return "§4§lRed";
        }
        if (color == Color.YELLOW)
        {
            return "§e§lYellow";
        }
        if (color == Color.GREEN)
        {
            return "§2§lGreen";
        }
        if (color == Color.BLUE)
        {
            return "§1§lBlue";
        }
        if (color == Color.BLACK)
        {
            return "§f§lBlack";
        }
        if (color == Color.WHITE)
        {
            return "§f§lWhite";
        }
        if (color == Color.PURPLE)
        {
            return "§5§lPurple";
        }
        if (color == Color.ORANGE)
        {
            return "§6§lOrange";
        }
        return "§a";
    }


}
