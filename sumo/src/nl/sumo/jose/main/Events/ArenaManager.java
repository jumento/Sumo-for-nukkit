package nl.sumo.jose.main.Events;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.MoveEntityAbsolutePacket;
import cn.nukkit.utils.Config;
import java.io.File;
import nl.sumo.jose.main.Boss;
import nl.sumo.jose.main.Entity.SumoEntity;
import nl.sumo.jose.main.Sumo;

public class ArenaManager
implements Listener {
    private Sumo sumo;

    public ArenaManager(Sumo pl) {
        this.sumo = pl;
    }

    @EventHandler
    public void onMoveNPC(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        for (Entity en : player.getLevel().getNearbyEntities(player.getBoundingBox().grow(4.0, 4.0, 4.0), (Entity)player)) {
            if (!(en instanceof SumoEntity)) continue;
            if (player.distance((Vector3)en) < 4.0) {
                ((SumoEntity)en).MovePlayerNpc(player);
                continue;
            }
            if (!(player.distance((Vector3)en) > 4.0)) continue;
            MoveEntityAbsolutePacket pk = new MoveEntityAbsolutePacket();
            pk.eid = en.getId();
            pk.x = en.getX();
            pk.y = en.getY() + 1.5;
            pk.z = en.getZ();
            pk.yaw = 3.0;
            pk.pitch = 0.0;
            pk.headYaw = 3.0;
            player.dataPacket((DataPacket)pk);
        }
    }

    @EventHandler
    public void quitPlayer(PlayerQuitEvent event) {
        String[] files;
        Player player = event.getPlayer();
        for (String w : files = new File(this.sumo.getDataFolder() + "/Arenas/").list()) {
            Config arena = new Config(this.sumo.getDataFolder() + "/Arenas/" + w, 2);
            if (player.getLevel() != this.sumo.getServer().getLevelByName(arena.getString("level")) || arena.getInt("status") != 0) continue;
            if (arena.getString("slot1").equals(player.getName())) {
                arena.set("slot1", (Object)"undefine");
                arena.save();
            }
            if (!arena.getString("slot2").equals(player.getName())) continue;
            arena.set("slot1", (Object)"undefine");
            arena.save();
        }
    }

    @EventHandler
    public void MinVoid(PlayerMoveEvent event) {
        String[] files;
        Player player = event.getPlayer();
        for (String w : files = new File(this.sumo.getDataFolder() + "/Arenas/").list()) {
            Config arena = new Config(this.sumo.getDataFolder() + "/Arenas/" + w, 2);
            if (player.getLevel() != this.sumo.getServer().getLevelByName(arena.getString("level"))) continue;
            int minimo = arena.getInt("minvoidy");
            if (!(player.getY() <= (double)minimo) || arena.getInt("status") != 1 || player.getGamemode() != 2) continue;
            player.setGamemode(3);
            this.sumo.getServer().getLevelByName(arena.getString("level")).addSound(new Vector3(player.getX(), player.getY(), player.getZ()), Sound.MOB_VILLAGER_NO);
        }
    }

    @EventHandler
    public void noDestroy(BlockBreakEvent event) {
        String[] files;
        Player player = event.getPlayer();
        for (String w : files = new File(this.sumo.getDataFolder() + "/Arenas/").list()) {
            Config arena = new Config(this.sumo.getDataFolder() + "/Arenas/" + w, 2);
            if (player.getLevel() != this.sumo.getServer().getLevelByName(arena.getString("level"))) continue;
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void noPlace(BlockPlaceEvent event) {
        String[] files;
        Player player = event.getPlayer();
        for (String w : files = new File(this.sumo.getDataFolder() + "/Arenas/").list()) {
            Config arena = new Config(this.sumo.getDataFolder() + "/Arenas/" + w, 2);
            if (player.getLevel() != this.sumo.getServer().getLevelByName(arena.getString("level"))) continue;
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void NoDrop(PlayerDropItemEvent event) {
        String[] files;
        Player player = event.getPlayer();
        for (String w : files = new File(this.sumo.getDataFolder() + "/Arenas/").list()) {
            Config arena = new Config(this.sumo.getDataFolder() + "/Arenas/" + w, 2);
            if (player.getLevel() != this.sumo.getServer().getLevelByName(arena.getString("level"))) continue;
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSalir(PlayerInteractEvent event) {
        String[] files;
        Player player = event.getPlayer();
        Item item = event.getItem();
        for (String w : files = new File(this.sumo.getDataFolder() + "/Arenas/").list()) {
            Config arena = new Config(this.sumo.getDataFolder() + "/Arenas/" + w, 2);
            if (player.getLevel() != this.sumo.getServer().getLevelByName(arena.getString("level")) || item.getId() != 152 || item.getCustomName() != "\u00a7l\u00a7bSalir" || arena.getInt("status") != 0) continue;
            if (arena.getString("slot1").equals(player.getName())) {
                arena.set("slot1", (Object)"undefine");
                arena.save();
            }
            if (arena.getString("slot2").equals(player.getName())) {
                arena.set("slot1", (Object)"undefine");
                arena.save();
            }
            player.teleport(this.sumo.getServer().getDefaultLevel().getSafeSpawn());
            player.setGamemode(2);
            player.getInventory().clearAll();
            player.getInventory().setItem(1, new Item(345, Integer.valueOf(0), 1).setCustomName("\u00a7cGadgets"));
            Boss.removeBossBar(player, arena.getInt("id"));
            player.setImmobile(false);
        }
    }

    @EventHandler
    public void HitPlayer(EntityDamageByEntityEvent event) {
        String[] files;
        Player player = (Player)event.getDamager();
        for (String w : files = new File(this.sumo.getDataFolder() + "/Arenas/").list()) {
            Config arena = new Config(this.sumo.getDataFolder() + "/Arenas/" + w, 2);
            if (player.getLevel() != this.sumo.getServer().getLevelByName(arena.getString("level"))) continue;
            this.sumo.getServer().getLevelByName(arena.getString("level")).addSound(new Vector3(player.getX(), player.getY(), player.getZ()), Sound.MOB_BLAZE_HIT);
        }
    }
}

