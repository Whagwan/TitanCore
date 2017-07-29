package TitanCore.Disguise;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import titancoreapi.API.Util.CC;
import titancoreapi.API.Util.ItemStackFactory;
import titancoreapi.Core.Items;
import titancoreapi.Core.Rank;
import TitanCore.Vanity.VanityItem;

public class CowDisguise extends Disguise implements VanityItem{

    public CowDisguise(Player owner) {
        super(owner, DisguiseType.COW);
    }

    @Override
    public void handleRightClick() {

        getOwner().getWorld().playSound(getOwner().getLocation(), Sound.COW_IDLE, 1.2F, 1F);

    }

    @Override
    public String getItemName() {
        return "Cow Disguise";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStackFactory().createItemStackwithLore(Items.SPAWNCOW, CC.tnGUIHead + "Activate " + getItemName(), getDescription());
    }

    @Override
    public ItemStack getActiveIcon() {
        return new ItemStackFactory().createItemStackwithGlow(CC.tnDisable + CC.BOLD + "Deactivate " + CC.tnGUIHead + getItemName(), Items.SPAWNCOW);
    }

    @Override
    public String[] getDescription() {
        return new String[] {CC.tnInfo + "Disguises you as a cow!", CC.tnUse + "Left Click" + CC.tnInfo + " to make sounds."};
    }

    @Override
    public String getPermRequired() {
        return "cowdisguise";
    }

    @Override
    public Rank getExclusiveRank() {
        return Rank.VIP;
    }

}