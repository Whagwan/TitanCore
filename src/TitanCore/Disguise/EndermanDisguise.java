package TitanCore.Disguise;

import TitanCore.Vanity.VanityItem;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import titancoreapi.API.Util.CC;
import titancoreapi.API.Util.ItemStackFactory;
import titancoreapi.Core.Items;
import titancoreapi.Core.Rank;

public class EndermanDisguise extends Disguise implements VanityItem {

    public EndermanDisguise(Player owner) {
        super(owner, DisguiseType.ENDERMAN);
    }

    @Override
    public void handleRightClick() {

        getOwner().getWorld().playSound(getOwner().getLocation(), Sound.ENDERMAN_IDLE, 1.2F, 1F);

    }

    @Override
    public String getItemName() {
        return "Enderman Disguise";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStackFactory().createItemStackwithLore(Items.SPAWNENDERMAN, CC.tnGUIHead + "Activate " + getItemName(), getDescription());
    }

    @Override
    public ItemStack getActiveIcon() {
        return new ItemStackFactory().createItemStackwithGlow(CC.tnDisable + CC.BOLD + "Deactivate " + CC.tnGUIHead + getItemName(), Items.SPAWNENDERMAN);
    }

    @Override
    public String[] getDescription() {
        return new String[] {CC.tnInfo + "Disguises you as a enderman!", CC.tnUse + "Left Click" + CC.tnInfo + " to make sounds."};
    }

    @Override
    public String getPermRequired() {
        return "endermandisguise";
    }

    @Override
    public Rank getExclusiveRank() {
        return Rank.TITAN;
    }

}