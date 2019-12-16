package nl.sumo.jose.main.AntiCheat;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import java.io.File;
import nl.sumo.jose.main.Arena;
import nl.sumo.jose.main.Boss;
import nl.sumo.jose.main.Sumo;

public class Comandos
implements Listener {
    private Sumo sumo;

    public Comandos(Sumo pl) {
        this.sumo = pl;
    }

    @EventHandler
    public void antiCommands(PlayerCommandPreprocessEvent event) {
        String[] games;
        Player player = event.getPlayer();
        for (String w : games = new File(this.sumo.getDataFolder() + "/Arenas/").list()) {
            Config arena = new Config(this.sumo.getDataFolder() + "/Arenas/" + w, 2);
            if (event.getPlayer().getLevel() != this.sumo.getServer().getLevelByName(arena.getString("level"))) continue;
            String text = event.getMessage();
            if (text.startsWith("/lobby")) {
                event.setCancelled(true);
                if (arena.getInt("status") == 0) {
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
                    player.setImmobile(false);
                    Boss.removeBossBar(player, arena.getInt("id"));
                    player.sendMessage(String.valueOf(Arena.title) + "Has dejado el combate");
                } else {
                    this.sumo.getMessagePlayerError(event.getPlayer());
                }
            }
            if (text.startsWith("/hub")) {
                event.setCancelled(true);
                if (arena.getInt("status") == 0) {
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
                    player.setImmobile(false);
                    Boss.removeBossBar(player, arena.getInt("id"));
                    player.sendMessage(String.valueOf(Arena.title) + "Has dejado el combate");
                } else {
                    this.sumo.getMessagePlayerError(event.getPlayer());
                }
            }
            if (text.startsWith("/gamemode")) {
                event.setCancelled(true);
                this.sumo.getMessagePlayerError(event.getPlayer());
            }
            if (text.startsWith("/tp")) {
                event.setCancelled(true);
                this.sumo.getMessagePlayerError(event.getPlayer());
            }
            if (text.startsWith("/kick")) {
                event.setCancelled(true);
                this.sumo.getMessagePlayerError(event.getPlayer());
            }
            if (text.startsWith("/give")) {
                event.setCancelled(true);
                this.sumo.getMessagePlayerError(event.getPlayer());
            }
            if (text.startsWith("//pos1")) {
                event.setCancelled(true);
                this.sumo.getMessagePlayerError(event.getPlayer());
            }
            if (text.startsWith("//pos2")) {
                event.setCancelled(true);
                this.sumo.getMessagePlayerError(event.getPlayer());
            }
            if (text.startsWith("//set")) {
                event.setCancelled(true);
                this.sumo.getMessagePlayerError(event.getPlayer());
            }
            if (text.startsWith("/ban")) {
                event.setCancelled(true);
                this.sumo.getMessagePlayerError(event.getPlayer());
            }
            if (text.startsWith("/ban-ip")) {
                event.setCancelled(true);
                this.sumo.getMessagePlayerError(event.getPlayer());
            }
            if (text.startsWith("/say")) {
                event.setCancelled(true);
                this.sumo.getMessagePlayerError(event.getPlayer());
            }
            if (text.startsWith("/kill")) {
                event.setCancelled(true);
                this.sumo.getMessagePlayerError(event.getPlayer());
            }
            if (text.startsWith("/suicide")) {
                event.setCancelled(true);
                this.sumo.getMessagePlayerError(event.getPlayer());
            }
            if (!text.startsWith("/help")) continue;
            event.setCancelled(true);
            this.sumo.getMessagePlayerError(event.getPlayer());
        }
    }
}

