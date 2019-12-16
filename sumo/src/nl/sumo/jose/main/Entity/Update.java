package nl.sumo.jose.main.Entity;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.scheduler.Task;
import java.util.Collection;
import java.util.Map;
import nl.sumo.jose.main.Entity.SumoEntity;
import nl.sumo.jose.main.Sumo;

public class Update
extends Task {
    private Sumo sumo;

    public Update(Sumo pl) {
        this.sumo = pl;
    }

    public void onRun(int tick) {
        this.sumo.updateSystem();
        this.sumo.setPlayersArena();
        for (Level lv : this.sumo.getServer().getLevels().values()) {
            for (Entity sumogame : lv.getEntities()) {
                if (!(sumogame instanceof SumoEntity)) continue;
                sumogame.setNameTag(this.sumo.getNameNPC());
                sumogame.setNameTagAlwaysVisible(true);
                sumogame.setNameTagVisible(true);
            }
        }
    }
}

