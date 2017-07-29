package TitanCore.Disguise;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;

import org.bukkit.Effect;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public abstract class Disguise {

    private Player owner;
    private DisguiseType type;
    private int nms_holo_id;

    public Disguise(Player owner, DisguiseType type)
    {
        this.type = type;
        this.owner = owner;
    }

    public Player getOwner()
    {
        return this.owner;
    }

    public int getNMS_HOLO_ID()
    {
        return this.nms_holo_id;
    }

    public DisguiseType getType()
    {
        return this.type;
    }

    public abstract void handleRightClick();

    public void disguise()
    {
        MobDisguise disguise = new MobDisguise(this.type);
        DisguiseAPI.disguiseToAll(this.owner, disguise);
        ArmorStand stand = (ArmorStand)this.owner.getWorld().spawnEntity(this.owner.getLocation(), EntityType.ARMOR_STAND);
        EntityArmorStand nms_holo = ((CraftArmorStand)stand).getHandle();
        nms_holo.setInvisible(true);
        nms_holo.setBasePlate(false);
        nms_holo.setSmall(true);
        nms_holo.setGravity(true);
        nms_holo.setCustomName(this.owner.getDisplayName());
        nms_holo.setCustomNameVisible(true);
        this.owner.setPassenger(stand);
        this.nms_holo_id = nms_holo.getId();
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(this.nms_holo_id);
        ((CraftPlayer)this.owner).getHandle().playerConnection.sendPacket(packet);
        this.owner.getWorld().spigot().playEffect(this.owner.getLocation(),Effect.LAVA_POP, 0, 0, 0.5F, 0.5F, 0.5F, 2F, 10, 50);
    }

    public void undisguise()
    {
        if (DisguiseAPI.isDisguised(this.owner))
        {
            DisguiseAPI.undisguiseToAll(this.owner);
        }
        if (this.owner.getPassenger() != null)
        {
            if (this.owner.getPassenger() instanceof ArmorStand)
            {
                this.owner.getPassenger().remove();
            }
        }
    }

}