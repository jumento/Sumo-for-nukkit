package nl.sumo.jose.main.Events;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import nl.sumo.jose.main.Arena;
import nl.sumo.jose.main.Entity.SumoEntity;
import nl.sumo.jose.main.Sumo;

public class NPC
implements Listener {
    private Sumo sumo;

    public NPC(Sumo pl) {
        this.sumo = pl;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof SumoEntity) {
            Player player = (Player)event.getDamager();
            if (this.sumo.countArchivos() == 0) {
                player.sendMessage(String.valueOf(Arena.title) + "No hay arenas configuradas");
            } else if (this.sumo.countArchivos() > 0) {
                if (this.sumo.namefinal == "ARENA.NO.FOUND") {
                    player.sendMessage(String.valueOf(Arena.title) + "No hay arenas disponibles");
                } else {
                    this.sumo.joinGame(player, this.sumo.namefinal);
                }
            }
        }
    }
}

