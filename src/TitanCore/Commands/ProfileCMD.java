package TitanCore.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import titancoreapi.API.Cooldown.SilentCooldown;
import titancoreapi.API.Stats.StatManager;
import titancoreapi.Core.Category;
import titancoreapi.Core.Messages.CategoryMSG;
import TitanCore.GUIs.Profile;

public class ProfileCMD implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player)
        {
            Player p = (Player)sender;
            if (p.getWorld().getName().startsWith("Hub"))
            {
                if (!SilentCooldown.isPlayeronSilentCooldown(p, "Profile"))
                {
                    SilentCooldown.addSilentCooldown(p, "Profile", 10);
                    if (!StatManager.isInCache(p))
                    {
                        return true;
                    }
                    Profile profilemenu = new Profile(p);
                    profilemenu.load();
                    profilemenu.open();
                }
            }
        }
        else
        {
            CategoryMSG.senderMessagePlayersOnly(sender, Category.HUB);
        }
        return false;
    }

}
