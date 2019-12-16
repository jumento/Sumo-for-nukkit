package nl.sumo.jose.main;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import java.util.Map;

public class Arena {
    public int id = 0;
    public int players = 0;
    public String namefinal = "default";
    public static String title = "\u00a77[\u00a7l\u00a7aSumo\u00a7r\u00a77] \u00a79: \u00a77";

    public int getId() {
        return this.id;
    }

    public void setId(int idvalue) {
        this.id = idvalue;
    }

    public String getName() {
        return this.namefinal;
    }

    public int getPlayersArena() {
        return this.players;
    }

    public void setPlayers(int value) {
        this.players = value;
    }

    public int getPlayersArenaById(int idvalue) {
        return Server.getInstance().getLevelByName("sumo-" + idvalue).getPlayers().size();
    }

    public int getMaxPlayers() {
        return 2;
    }
}

