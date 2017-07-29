package TitanCore.Hub;

import org.bukkit.Location;

public class TutorialStep {

    private Location loc;
    private String title;
    private String subtitle;

    public TutorialStep(String title, String subtitle)
    {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getSubtitle()
    {
        return this.subtitle;
    }

    public void setLocation(Location loc)
    {
        this.loc = loc;
    }

    public Location getLocation()
    {
        return this.loc;
    }

}