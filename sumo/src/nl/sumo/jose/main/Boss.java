package nl.sumo.jose.main;

import cn.nukkit.Player;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.BossEventPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.network.protocol.SetEntityDataPacket;
import cn.nukkit.network.protocol.UpdateAttributesPacket;

public class Boss {
    public static void sendBossBarToPlayer(Player player, long eid, String title) {
        EntityMetadata dataProperties;
        Boss.removeBossBar(player, (int)eid);
        AddEntityPacket packet = new AddEntityPacket();
        packet.entityUniqueId = eid;
        packet.entityRuntimeId = eid;
        packet.type = 52;
        packet.yaw = 0.0f;
        packet.pitch = 0.0f;
        long flags = 0L;
        flags |= 131072L;
        flags |= 32L;
        packet.metadata = dataProperties = new EntityMetadata().putLong(0, flags |= 65536L).putShort(7, 400).putShort(42, 400).putLong(37, -1L).putFloat(38, 1.0f).putString(4, title).putInt(38, 0);
        packet.x = (float)player.getX();
        packet.y = (float)player.getY() - 20.0f;
        packet.z = (float)player.getZ();
        packet.speedX = 0.0f;
        packet.speedY = 0.0f;
        packet.speedZ = 0.0f;
        player.dataPacket((DataPacket)packet);
        BossEventPacket bpk = new BossEventPacket();
        bpk.bossEid = eid;
        bpk.type = 0;
        player.dataPacket((DataPacket)bpk);
    }

    public static void setVida(Player player, int eid, int percentage) {
        if (percentage > 100) {
            percentage = 100;
        }
        if (percentage < 0) {
            percentage = 0;
        }
        UpdateAttributesPacket upk = new UpdateAttributesPacket();
        upk.entityId = eid;
        Attribute attr = Attribute.getAttribute((int)4);
        attr.setMaxValue(100.0f);
        attr.setValue((float)percentage);
        upk.entries = new Attribute[]{attr};
        player.dataPacket((DataPacket)upk);
        BossEventPacket bpk = new BossEventPacket();
        bpk.bossEid = eid;
        bpk.type = 1;
        player.dataPacket((DataPacket)bpk);
    }

    public static void sendTitle(Player player, int eid, String title) {
        EntityMetadata dataProperties;
        SetEntityDataPacket npk = new SetEntityDataPacket();
        long flags = 0L;
        flags |= 131072L;
        flags |= 32L;
        dataProperties = new EntityMetadata().putLong(0, flags |= 65536L).putShort(7, 400).putShort(42, 400).putLong(37, -1L).putFloat(38, 1.0f).putString(4, title).putInt(38, 0);
        npk.metadata = dataProperties;
        npk.eid = eid;
        player.dataPacket((DataPacket)npk);
        BossEventPacket bpk = new BossEventPacket();
        bpk.bossEid = eid;
        bpk.type = 1;
        player.dataPacket((DataPacket)bpk);
    }

    public static boolean removeBossBar(Player player, int eid) {
        RemoveEntityPacket pk = new RemoveEntityPacket();
        pk.eid = eid;
        player.dataPacket((DataPacket)pk);
        return true;
    }

    public static int getIDBoss() {
        int vector = 0;
        int numero = (int)(Math.random() * 1005.0);
        int numero1 = (int)(Math.random() * 20.0);
        int numero2 = (int)(Math.random() * 666.0);
        vector = numero + numero1 + numero2;
        return vector;
    }
}

