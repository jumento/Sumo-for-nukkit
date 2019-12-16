package nl.sumo.jose.main.Entity;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.data.EntityData;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.data.FloatEntityData;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector2;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.MoveEntityAbsolutePacket;
import java.util.Map;
import java.util.UUID;

public class SumoEntity
extends EntityHuman {
    public SumoEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.setDataProperty((EntityData)new FloatEntityData(38, this.namedTag.getFloat("scale")));
    }

    public void spawnTo(Player player) {
        if (!this.hasSpawned.containsKey(player.getLoaderId())) {
            this.hasSpawned.put(player.getLoaderId(), player);
            this.server.updatePlayerListData(this.getUniqueId(), this.getId(), this.getName(), this.skin, new Player[]{player});
            AddPlayerPacket pk = new AddPlayerPacket();
            pk.uuid = this.getUniqueId();
            pk.username = this.getName();
            pk.entityUniqueId = this.getId();
            pk.entityRuntimeId = this.getId();
            pk.x = (float)this.x;
            pk.y = (float)this.y;
            pk.z = (float)this.z;
            pk.speedX = (float)this.motionX;
            pk.speedY = (float)this.motionY;
            pk.speedZ = (float)this.motionZ;
            pk.yaw = (float)this.yaw;
            pk.pitch = (float)this.pitch;
            this.inventory.setItemInHand(Item.fromString((String)this.namedTag.getString("Item")));
            pk.item = this.getInventory().getItemInHand();
            pk.metadata = this.dataProperties;
            player.dataPacket((DataPacket)pk);
            this.inventory.setHelmet(Item.fromString((String)this.namedTag.getString("Helmet")));
            this.inventory.setChestplate(Item.fromString((String)this.namedTag.getString("Chestplate")));
            this.inventory.setLeggings(Item.fromString((String)this.namedTag.getString("Leggings")));
            this.inventory.setBoots(Item.fromString((String)this.namedTag.getString("Boots")));
            this.inventory.sendArmorContents(player);
            if (this instanceof SumoEntity) {
                this.server.removePlayerListData(this.getUniqueId(), new Player[]{player});
            }
            super.spawnTo(player);
        }
    }

    public void MovePlayerNpc(Player player) {
        int x = (int)player.getX() - (int)this.getX();
        int y = (int)player.getY() - (int)this.getY();
        int z = (int)player.getZ() - (int)this.getZ();
        Double angle = Math.atan2(z, x);
        Double yaw = angle * 180.0 / 3.141592653589793 - 90.0;
        Vector2 vector = new Vector2((double)x, (double)z);
        Double distance = vector.distance(player.getX(), player.getZ());
        Double angle1 = Math.atan2(distance, y);
        Double pitch = angle1 * 180.0 / 3.141592653589793 - 90.0;
        MoveEntityAbsolutePacket pk = new MoveEntityAbsolutePacket();
        pk.eid = this.getId();
        pk.x = this.getX();
        pk.y = this.getY() + 1.5;
        pk.z = this.getZ();
        pk.yaw = yaw;
        pk.pitch = pitch;
        pk.headYaw = yaw;
        player.dataPacket((DataPacket)pk);
    }
}

