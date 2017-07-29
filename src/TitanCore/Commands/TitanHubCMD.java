package TitanCore.Commands;

import TitanCore.TCHub;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import titancoreapi.API.Permissions.PermissionsHandler;
import titancoreapi.API.Util.CC;
import titancoreapi.Core.Category;
import titancoreapi.Core.Rank;
import titancoreapi.Core.Messages.AuthorMSG;
import titancoreapi.Core.Messages.CategoryMSG;

public class TitanHubCMD implements CommandExecutor {

    private TCHub main = TCHub.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length >= 1)
        {
            if (args[0].equalsIgnoreCase("tchub"))
            {
                if (PermissionsHandler.hasRankSender(sender, Rank.ADMIN))
                {
                    main.getConfigManager().reloadConfig();
                    CategoryMSG.senderMessage(sender, Category.HUB, CC.tnInfo + "The config has been reloaded!");
                }
                else
                {
                    AuthorMSG.sendAuthorStamp("Hub", main.getDescription().getVersion(), sender);
                }
            }
        }
        else
        {
            AuthorMSG.sendAuthorStamp("Hub", main.getDescription().getVersion(), sender);
        }
        return false;
    }

}
