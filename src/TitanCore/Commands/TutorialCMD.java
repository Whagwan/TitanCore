package TitanCore.Commands;

import TitanCore.Hub.TutorialManager;
import TitanCore.Hub.TutorialStep;
import TitanCore.TCHub;
import titancoreapi.API.Util.UtilPlayer;
import titancoreapi.Core.Category;
import titancoreapi.Core.Messages.CategoryMSG;
import titancoreapi.Core.Messages.PermMSG;
import titancoreapi.Core.Rank;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import titancoreapi.API.Util.CC;

public class TutorialCMD implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player)
        {
            Player p = (Player)sender;
            if (UtilPlayer.hasRank(p, Rank.ADMIN))
            {
                if (args.length > 0)
                {
                    if (args[0].equalsIgnoreCase("add"))
                    {
                        if (args.length >= 4)
                        {
                            Location loc = p.getLocation();
                            String stepname = args[1];

                            String title = args[2].replace("_", " ");
                            String subtitle = args[3].replace("_", " ");
                            TutorialStep step = new TutorialStep(title, subtitle);
                            step.setLocation(loc);
                            TutorialManager.addTutorialStep(stepname, step);
                            UtilPlayer.message(Category.HUB, p, CC.tnInfo + "Successfully added step " + CC.tnValue + stepname + CC.end);
                        }
                        else
                        {
                            showHelp(p);
                        }
                    }
                    else if (args[0].equalsIgnoreCase("remove"))
                    {
                        if (args.length > 1)
                        {
                            if (TutorialManager.removeTutorialStep(args[1]))
                            {
                                UtilPlayer.message(Category.HUB, p, CC.tnInfo + "Successfully removed step " + CC.tnValue + args[1]);
                            }
                        }
                        else
                        {
                            showHelp(p);
                        }
                    }
                    else if (args[0].equalsIgnoreCase("npc"))
                    {
                        TutorialManager.setNPCPosition(p.getLocation());
                        TutorialManager.spawnNPC();
                        UtilPlayer.message(Category.HUB, p, CC.tnInfo + "The tutorial npc has been spawned!");
                    }
                    else if (args[0].equalsIgnoreCase("reload"))
                    {
                        TCHub.getInstance().getConfigManager().reloadTutorial();
                        TutorialManager.load();
                        UtilPlayer.message(Category.HUB, p, CC.tnInfo + "Reloaded the tutorial system!");
                    }
                    else
                    {
                        showHelp(p);
                    }
                }
                else
                {
                    showHelp(p);
                }
            }
            else
            {
                PermMSG.noPerm(sender, Rank.ADMIN);
            }
        }
        else
        {
            CategoryMSG.senderMessagePlayersOnly(sender, Category.HUB);
        }

        return false;
    }

    private void showHelp(Player p)
    {
        CategoryMSG.senderArgsErr(p, Category.HUB, "");
        UtilPlayer.messageHeader(Category.HUB, p, "Tutorial Command Usage");
        UtilPlayer.messageNoCategory(p, CC.tnUse + "/tutorial add <stepname> <title> <subtitle>" + CC.tnInfo + " Add a tutorial step with your current location and specified info. _ for space.");
        UtilPlayer.messageNoCategory(p, CC.tnUse + "/tutorial remove <stepname>" + CC.tnInfo + " Remove a tutorial step.");
        UtilPlayer.messageNoCategory(p, CC.tnUse + "/tutorial npc" + CC.tnInfo + " Set the location of and spawn the tutorial npc.");
        UtilPlayer.messageNoCategory(p, CC.tnUse + "/tutorial reload" + CC.tnInfo + " Reload the tutorial system.");
        UtilPlayer.messageFooter(p);
    }

}