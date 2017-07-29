package TitanCore.Util;

import org.bukkit.entity.Player;

import titancoreapi.API.Permissions.PermissionsHandler;

public class PermsHandler {

    private static String sub_Base = "hub";

    public static boolean checkAccess(Player p, String perm)
    {
        if (perm != null)
        {
            return PermissionsHandler.checkPlayerPerm(p, sub_Base, perm);
        }
        return false;
    }

}
