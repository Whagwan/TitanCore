package TitanCore.Gadget;

import TitanCore.Vanity.VanityItem;
import TitanCore.Vanity.VanityManager;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import titancoreapi.API.Cooldown.SilentCooldown;
import titancoreapi.API.Util.CC;
import titancoreapi.API.Util.ItemStackFactory;
import titancoreapi.Core.Items;
import titancoreapi.Core.Rank;

import java.util.Random;

public class FireworkLauncherGadget extends Gadget implements VanityItem{

    public FireworkLauncherGadget(Player owner) {
        super(owner, new ItemStackFactory().createItemStack(Items.BLAZEROD, CC.tnValue + "Firework Launcher"));
    }

    @Override
    public void handleEvent(Event e) {
        if (e instanceof PlayerInteractEvent)
        {
            PlayerInteractEvent event = (PlayerInteractEvent)e;
            Player p = event.getPlayer();
            if ((event.getAction() != Action.RIGHT_CLICK_AIR) && (event.getAction() != Action.RIGHT_CLICK_BLOCK))
            {
                return;
            }
            if (!p.getItemInHand().hasItemMeta())
            {
                return;
            }
            if (!p.getName().equalsIgnoreCase(getOwner().getName()))
            {
                return;
            }
            if (p.getItemInHand().getType() != getItem().getType())
            {
                return;
            }
            if (SilentCooldown.isPlayeronSilentCooldown(p, "Firework Launcher"))
            {
                return;
            }
            if (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(getItem().getItemMeta().getDisplayName()))
            {
                p.getWorld().playSound(p.getLocation(), Sound.FUSE, 1, 2F);
                Firework fw = (Firework)p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
                FireworkMeta fwm = fw.getFireworkMeta();

                Random r = new Random();

                int rt = r.nextInt(1) + 1;
                FireworkEffect.Type type2 = FireworkEffect.Type.BALL;
                if (rt == 1)
                {
                    type2 = FireworkEffect.Type.BALL_LARGE;
                }
                if (rt == 2)
                {
                    type2 = FireworkEffect.Type.STAR;
                }
                int r1i = r.nextInt(17) + 1;
                int r2i = r.nextInt(17) + 1;
                Color c1 = getColor(r1i);
                Color c2 = getColor(r2i);

                FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type2).trail(r.nextBoolean()).build();

                fwm.addEffect(effect);

                int rp = r.nextInt(2) + 1;
                fwm.setPower(rp);

                fw.setFireworkMeta(fwm);
                VanityManager.useAbility(p, "Firework Launcher");
                SilentCooldown.addSilentCooldown(p, "Firework Launcher", 10);
            }
        }

    }

    private Color getColor(int i)//for random fireworks
    {
        Color c = null;
        if (i == 1) {
            c = Color.AQUA;
        }
        if (i == 2) {
            c = Color.BLACK;
        }
        if (i == 3) {
            c = Color.BLUE;
        }
        if (i == 4) {
            c = Color.FUCHSIA;
        }
        if (i == 5) {
            c = Color.GRAY;
        }
        if (i == 6) {
            c = Color.GREEN;
        }
        if (i == 7) {
            c = Color.LIME;
        }
        if (i == 8) {
            c = Color.MAROON;
        }
        if (i == 9) {
            c = Color.NAVY;
        }
        if (i == 10) {
            c = Color.OLIVE;
        }
        if (i == 11) {
            c = Color.ORANGE;
        }
        if (i == 12) {
            c = Color.PURPLE;
        }
        if (i == 13) {
            c = Color.RED;
        }
        if (i == 14) {
            c = Color.SILVER;
        }
        if (i == 15) {
            c = Color.TEAL;
        }
        if (i == 16) {
            c = Color.WHITE;
        }
        if (i == 17) {
            c = Color.YELLOW;
        }
        return c;
    }


    @Override
    public String getItemName() {
        return "Firework Launcher";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStackFactory().createItemStackwithLore(Items.BLAZEROD, CC.tnGUIHead + "Equip " + getItemName(), getDescription());
    }

    @Override
    public ItemStack getActiveIcon() {
        return new ItemStackFactory().createItemStackwithGlow(CC.tnDisable + CC.BOLD + "Unequip " + CC.tnGUIHead + getItemName(), Items.BLAZEROD);
    }

    @Override
    public String[] getDescription() {
        return new String[] {CC.tnInfo + "Show off your rank", CC.tnInfo + "by launching a random", CC.tnInfo + "firework in the Hub!", CC.tnUse + "Right Click" + CC.tnInfo + " to use " + CC.tnAbility + "Firework Launcher"};
    }

    @Override
    public String getPermRequired() {
        return "fireworklauncher";
    }

    @Override
    public Rank getExclusiveRank() {
        return Rank.MVP;
    }

}
