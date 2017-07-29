package TitanCore.Hub;

import TitanCore.TCHub;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import titancoreapi.API.Titles.ActionBar;

import java.util.List;

public class Announcer implements Runnable{

    private TCHub main = TCHub.getInstance();
    private int counter = 0;

    @Override
    public void run()
    {
        List<String> broadcasts = main.getConfigManager().getConfig().getStringList("announcer.messages");
        List<String> enabledworlds = main.getConfigManager().getConfig().getStringList("announcer.worlds");
        for (Player p : Bukkit.getOnlinePlayers())
        {
            for (String world : enabledworlds)
            {
                if (p.getWorld().getName().equalsIgnoreCase(world))
                {
                    ActionBar bc = new ActionBar(ChatColor.translateAlternateColorCodes('&', broadcasts.get(counter)));
                    bc.send(p);
                }
            }
        }
        counter++;
        if (counter >= broadcasts.size())
        {
            counter = 0;
        }
    }

}