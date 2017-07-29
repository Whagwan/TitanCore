package TitanCore.GUIs;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import titancoreapi.API.Util.CC;
import titancoreapi.API.Util.ItemStackFactory;
import titancoreapi.Core.Items;
import titancoreapi.Core.Networking.ServerPlayerCounter;
import titancoreapi.Core.Networking.WorldPlayerCounter;

public class GamemodeSelector extends GUI{

    public GamemodeSelector(Player p) {
        super("Gamemode Selector", 26, p);
    }

    @Override
    public void load()
    {
        ItemStackFactory isf = new ItemStackFactory();
        ItemStack titanuhc = isf.createItemStackwithLore(Items.TRIPWIREHOOK, CC.tnGUIHead + "TitanUHC", new String[] {CC.tnInfo + "A new, custom, awesome gamemode", CC.tnInfo + "here on the TitanCore!", CC.tnInfo + "Use guns, grenade launchers and", CC.tnInfo + "other weapons to kill your enemies", CC.tnInfo + "in a slowly shrinking space!", " ", CC.tnInfo + "Players: " + CC.tnValue + "-"});
        ItemStack SkyWars = isf.createItemStackwithLore(Items.IRONSWORD, CC.tnGUIHead + "SkyWars", new String[] {CC.tnInfo + "The SkyWars you all love, but", CC.tnInfo + "completely reinvented!", CC.tnInfo + "Choose your kit with special perks and abilities", CC.tnInfo + "to knock down opponents in this continuous", CC.tnInfo + "free-for-all battle with amazing features", CC.tnInfo + "such as blood, killstreaks and more!", " ", CC.tnInfo + "Players: " + CC.tnValue + "-"});
        ItemStack hub = isf.createItemStackwithLore(Items.BEACON, CC.tnGUIHead + "Hub", new String[] {" ", CC.tnInfo + "Players: " + CC.tnValue + "-"});

        setIcon(11, SkyWars);
        setIcon(13, hub);
        setIcon(15, titanuhc);
        getViewer().openInventory(getMenu());
        getViewer().updateInventory();
    }

    @Override
    public void open() {
        int titanuhccount = 0;
        int SkyWarscount = 0;
        int hubcount = 0;

        int titanuhclobby = WorldPlayerCounter.getWorldCount("Hub-titanuhc");
        int titanuhcarenas = 0;
        for (World world : Bukkit.getWorlds())
        {
            if (world.getName().startsWith("titanuhc"))
            {
                titanuhcarenas = titanuhcarenas + world.getPlayers().size();
            }
        }

        titanuhccount = titanuhclobby + titanuhcarenas;
        SkyWarscount = ServerPlayerCounter.getPlayerCount("skywars");
        hubcount = WorldPlayerCounter.getWorldCount("Hub-skywars");

        ItemStackFactory isf = new ItemStackFactory();
        ItemStack titanuhc = isf.createItemStackwithLore(Items.TRIPWIREHOOK, CC.tnGUIHead + "TitanUHC", new String[] {CC.tnInfo + "A new, custom, awesome gamemode", CC.tnInfo + "here on the TitanCore!", CC.tnInfo + "Use guns, grenade launchers and", CC.tnInfo + "other weapons to kill your enemies", CC.tnInfo + "in a slowly shrinking space!", " ", CC.tnInfo + "Players: " + CC.tnValue + titanuhccount});
        ItemStack SkyWars = isf.createItemStackwithLore(Items.IRONSWORD, CC.tnGUIHead + "skywars", new String[] {CC.tnInfo + "The SkyWars you all love, but", CC.tnInfo + "completely reinvented!", CC.tnInfo + "Choose your kit with special perks and abilities", CC.tnInfo + "to knock down opponents in this continuous", CC.tnInfo + "free-for-all battle with amazing features", CC.tnInfo + "such as blood, killstreaks and more!", " ", CC.tnInfo + "Players: " + CC.tnValue + SkyWarscount});
        ItemStack hub = isf.createItemStackwithLore(Items.BEACON, CC.tnGUIHead + "Hub", new String[] {" ", CC.tnInfo + "Players: " + CC.tnValue + hubcount});

        setIcon(11, SkyWars);
        setIcon(13, hub);
        setIcon(15, titanuhc);
        getViewer().updateInventory();
    }

}