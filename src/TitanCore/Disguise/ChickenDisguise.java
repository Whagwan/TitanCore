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

public class ChickenDisguise extends Disguise implements VanityItem {

    public ChickenDisguise(Player owner) {
        super(owner, DisguiseType.CHICKEN);
    }

    @Override
    public void handleRightClick() {

        getOwner().getWorld().playSound(getOwner().getLocation(), Sound.CHICKEN_IDLE, 1.2F, 1F);

    }

    @Override
    public String getItemName() {
        return "Chicken Disguise";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStackFactory().createItemStackwithLore(Items.SPAWNCHICKEN, CC.tnGUIHead + "Activate " + getItemName(), getDescription());
    }

    @Override
    public ItemStack getActiveIcon() {
        return new ItemStackFactory().createItemStackwithGlow(CC.tnDisable + CC.BOLD + "Deactivate " + CC.tnGUIHead + getItemName(), Items.SPAWNCHICKEN);
    }

    @Override
    public String[] getDescription() {
        return new String[] {CC.tnInfo + "Disguises you as a chicken!", CC.tnUse + "Left Click" + CC.tnInfo + " to make sounds."};
    }

    @Override
    public String getPermRequired() {
        return "chickendisguise";
    }

    @Override
    public Rank getExclusiveRank() {
        return Rank.VIP;
    }

}