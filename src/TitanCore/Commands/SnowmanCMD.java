package TitanCore.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowman;

import titancoreapi.API.Util.CC;
import titancoreapi.API.Util.UtilPlayer;
import titancoreapi.Core.Category;
import titancoreapi.Core.Rank;
import titancoreapi.Core.Messages.CategoryMSG;
import titancoreapi.Core.Messages.PermMSG;

public class SnowmanCMD implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player p = (Player)sender;
            if (!p.getWorld().getName().startsWith("Hub"))
            {
                return true;
            }
            if (!UtilPlayer.hasRank(p, Rank.ADMIN))
            {
                PermMSG.noPerm(sender, Rank.ADMIN);
                return true;
            }
            Snowman snowman = (Snowman)p.getWorld().spawnEntity(p.getLocation(), EntityType.SNOWMAN);
            snowman.setCustomName("Mr Blobby");
            snowman.setCustomNameVisible(true);
            snowman.setCanPickupItems(false);
            UtilPlayer.message(Category.HUB, p, CC.tnInfo + "Spawned Mr BadEntities!");
        }
        else
        {
            CategoryMSG.senderMessagePlayersOnly(sender, Category.HUB);
        }
        return true;
    }

}