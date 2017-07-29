package TitanCore.Commands;

import TitanCore.Hub.HubManager;
import org.bukkit.entity.Player;

import titancoreapi.API.Util.CC;
import titancoreapi.API.Util.UtilPlayer;
import titancoreapi.Core.Category;

public class HubCMD {

    public boolean execute(Player caller, String[] args)
    {
        if (args.length > 0)
        {
            if (args[0].equalsIgnoreCase("borderline"))
            {
                HubManager.sendToBorderlineHub(caller);
                return true;
            }
        }
        if (caller.getWorld().getName().equalsIgnoreCase("Hub-main"))
        {
            UtilPlayer.message(Category.TRAVEL, caller, CC.tnError + "You are already in the Hub!");
            return false;
        }
        HubManager.sendtoHub(caller);
        return true;
    }
}