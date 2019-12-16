package nl.sumo.jose.main;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.event.Listener;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.UpdateAttributesPacket;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.scheduler.ServerScheduler;
import cn.nukkit.scheduler.Task;
import cn.nukkit.scheduler.TaskHandler;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.MainLogger;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

//import nl.cuboscraft.plugin.main.CubosCore;
import nl.sumo.jose.main.AntiCheat.Comandos;
import nl.sumo.jose.main.Arena;
import nl.sumo.jose.main.Boss;
import nl.sumo.jose.main.Entity.SumoEntity;
import nl.sumo.jose.main.Entity.Update;
import nl.sumo.jose.main.Events.ArenaManager;
import nl.sumo.jose.main.Events.NPC;
import nl.sumo.jose.main.GameTask;

public class Sumo
extends PluginBase {
    public String namefinal = "";
    public int id = 0;
    public int players = 0;
 //   public CubosCore core;

    public void onEnable() {
        Entity.registerEntity((String)SumoEntity.class.getSimpleName(), SumoEntity.class);
        new File(this.getDataFolder() + "/Arenas/").mkdirs();
        this.getServer().getLogger().info(String.valueOf(Arena.title) + " Cargando resources .....");
        this.getServer().getLogger().info(String.valueOf(Arena.title) + " Hay " + this.countArchivos() + " arenas configuradas");
        this.loadArenas();
        this.getServer().getScheduler().scheduleRepeatingTask((Task)new Update(this), 5);
        this.getServer().getScheduler().scheduleRepeatingTask((Task)new GameTask(this), 20);
        this.getServer().getPluginManager().registerEvents((Listener)new NPC(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new ArenaManager(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new Comandos(this), (Plugin)this);
    }

    public void loadArenas() {
        if (this.countArchivos() > 0) {
            String[] gameLevel;
            for (String w : gameLevel = new File(this.getDataFolder() + "/Arenas/").list()) {
                Config arena = new Config(this.getDataFolder() + "/Arenas/" + w, 2);
                this.getServer().loadLevel(arena.getString("level"));
                arena.set("status", (Object)0);
                arena.set("time", (Object)180);
                arena.set("start", (Object)10);
                arena.set("slot1", (Object)"undefine");
                arena.set("slot2", (Object)"undefine");
                arena.save();
                this.getServer().getLevelByName(arena.getString("level")).setTime(0);
                this.getServer().getLevelByName(arena.getString("level")).stopTime();
                this.getServer().getLogger().info(String.valueOf(Arena.title) + " Cargando " + w.replace(".yml", ""));
            }
        } else {
            this.getServer().getLogger().info(String.valueOf(Arena.title) + " Se cargaron 0 arenas correctamente");
        }
    }

    public int countArchivos() {
        int total = 0;
        File archivos = new File(this.getDataFolder() + "/Arenas/");
        String[] isArchivo = archivos.list();
        return total += isArchivo.length;
    }

    public CompoundTag nbt(Player sender, String name) {
    	 CompoundTag nbt = new CompoundTag()
                 .putList(new ListTag<>("Pos")
                         .add(new DoubleTag("", sender.x))
                         .add(new DoubleTag("", sender.y))
                         .add(new DoubleTag("", sender.z)))
                 .putList(new ListTag<DoubleTag>("Motion")
                         .add(new DoubleTag("", 0))
                         .add(new DoubleTag("", 0))
                         .add(new DoubleTag("", 0)))
                 .putList(new ListTag<FloatTag>("Rotation")
                         .add(new FloatTag("", (float) sender.getYaw()))
                         .add(new FloatTag("", (float) sender.getPitch())))
                 .putBoolean("Invulnerable", true)
                 .putString("NameTag", name)
                 .putBoolean("npc", true)
                 .putFloat("scale", 1);
    	 nbt.putCompound("Skin", new CompoundTag()
                 .putByteArray("Data", sender.getSkin().getSkinData().data)
                 .putString("ModelId", UUID.randomUUID().toString())
                 .putString("GeometryName", "geometry.humanoid.custom")
                 .putByteArray("GeometryData", sender.getSkin().getGeometryData().getBytes(StandardCharsets.UTF_8)));
        nbt.putBoolean("ishuman", true);
        nbt.putString("Item", sender.getInventory().getItemInHand().getName());
        nbt.putString("Helmet", sender.getInventory().getHelmet().getName());
        nbt.putString("Chestplate", sender.getInventory().getChestplate().getName());
        nbt.putString("Leggings", sender.getInventory().getLeggings().getName());
        nbt.putString("Boots", sender.getInventory().getBoots().getName());
        return nbt;
    }

    public void createArena(Player player, String id, String name, String level) {
        if (new File(this.getDataFolder() + "/Arenas/sumo-" + id + ".yml").exists()) {
            player.sendMessage(String.valueOf(Arena.title) + " Esta id ya esta registrada intenta usar otra");
        } else {
            Config arena = new Config(this.getDataFolder() + "/Arenas/sumo-" + id + ".yml", 2);
            arena.set("nameArena", (Object)name);
            arena.set("level", (Object)level);
            arena.set("status", (Object)0);
            arena.set("time", (Object)180);
            arena.set("game1x", (Object)0);
            arena.set("game1y", (Object)0);
            arena.set("game1z", (Object)0);
            arena.set("game2x", (Object)0);
            arena.set("game2y", (Object)0);
            arena.set("game2z", (Object)0);
            arena.set("minvoidx", (Object)0);
            arena.set("minvoidy", (Object)0);
            arena.set("minvoidz", (Object)0);
            arena.set("start", (Object)10);
            arena.set("id", (Object)Boss.getIDBoss());
            arena.set("slot1", (Object)"undefine");
            arena.set("slot2", (Object)"undefine");
            arena.save();
            this.getServer().loadLevel(level);
            player.teleport(this.getServer().getLevelByName(level).getSafeSpawn());
            player.sendMessage(String.valueOf(Arena.title) + " Configura la arena");
        }
    }

    public void setGamePos1(Player player, String id) {
        if (!new File(this.getDataFolder() + "/Arenas/sumo-" + id + ".yml").exists()) {
            player.sendMessage(String.valueOf(Arena.title) + " Esta id no esta registrada");
        } else {
            Config arena = new Config(this.getDataFolder() + "/Arenas/sumo-" + id + ".yml", 2);
            int x = (int)player.getX();
            int y = (int)player.getY();
            int z = (int)player.getZ();
            int[] valores = new int[]{x, y, z};
            arena.set("game1x", (Object)valores[0]);
            arena.set("game1y", (Object)valores[1]);
            arena.set("game1z", (Object)valores[2]);
            arena.save();
            player.sendMessage(String.valueOf(Arena.title) + " Se ha seleccionado el punto 1 id : sumo-" + id);
        }
    }

    public void setGamePos2(Player player, String id) {
        if (!new File(this.getDataFolder() + "/Arenas/sumo-" + id + ".yml").exists()) {
            player.sendMessage(String.valueOf(Arena.title) + " Esta id no esta registrada");
        } else {
            Config arena = new Config(this.getDataFolder() + "/Arenas/sumo-" + id + ".yml", 2);
            int x = (int)player.getX();
            int y = (int)player.getY();
            int z = (int)player.getZ();
            int[] valores = new int[]{x, y, z};
            arena.set("game2x", (Object)valores[0]);
            arena.set("game2y", (Object)valores[1]);
            arena.set("game2z", (Object)valores[2]);
            arena.save();
            player.sendMessage(String.valueOf(Arena.title) + " Se ha seleccionado el punto 2 id : sumo-" + id);
        }
    }

    public void minvoid(Player player, String id) {
        if (!new File(this.getDataFolder() + "/Arenas/sumo-" + id + ".yml").exists()) {
            player.sendMessage(String.valueOf(Arena.title) + " Esta id no esta registrada");
        } else {
            Config arena = new Config(this.getDataFolder() + "/Arenas/sumo-" + id + ".yml", 2);
            int x = (int)player.getX();
            int y = (int)player.getY();
            int z = (int)player.getZ();
            int[] valores = new int[]{x, y, z};
            arena.set("minvoidx", (Object)valores[0]);
            arena.set("minvoidy", (Object)valores[1]);
            arena.set("minvoidz", (Object)valores[2]);
            arena.save();
            player.sendMessage(String.valueOf(Arena.title) + " Se ha seleccionado el minVoid id : sumo-" + id);
        }
    }

    public void saveArena(Player player, String id) {
        if (!new File(this.getDataFolder() + "/Arenas/sumo-" + id + ".yml").exists()) {
            player.sendMessage(String.valueOf(Arena.title) + " Esta id no esta registrada");
        } else {
            player.teleport(this.getServer().getDefaultLevel().getSafeSpawn());
            player.sendMessage(String.valueOf(Arena.title) + " Se ha terminado la configuracion en id : sumo-" + id);
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player)sender;
        String string = command.getName().toLowerCase();
        switch (string.hashCode()) {
            case 3541892: {
                if (!string.equals("sumo")) break;
                if (args[0].equals("create") && player.isOp()) {
                    this.createArena(player, args[2], args[1], args[3]);
                }
                if (args[0].equals("setpos1") && player.isOp()) {
                    this.setGamePos1(player, args[1]);
                }
                if (args[0].equals("setpos2") && player.isOp()) {
                    this.setGamePos2(player, args[1]);
                }
                if (args[0].equals("minvoid") && player.isOp()) {
                    this.minvoid(player, args[1]);
                }
                if (args[0].equals("save") && player.isOp()) {
                    this.saveArena(player, args[1]);
                }
                if (args[0].equals("npcyaw") && player.isOp()) {
                    player.sendMessage("Tu yaw es : " + player.yaw);
                }
                if (args[0].equals("setnpc") && player.isOp()) {
                    CompoundTag nbt = this.nbt(player, this.getNameNPC());
                    Entity ent = Entity.createEntity((String)"SumoEntity", (FullChunk)player.chunk, (CompoundTag)nbt, (Object[])new Object[0]);
                    ent.setNameTag("Loading...");
                    ent.setNameTagAlwaysVisible(true);
                    ent.setNameTagVisible(true);
                    ent.spawnTo(player);
                }
                if (!args[0].equals("removenpc")) break;
                for (Level lv : this.getServer().getLevels().values()) {
                    for (Entity game : lv.getEntities()) {
                        if (!(game instanceof SumoEntity)) continue;
                        game.kill();
                    }
                }
            }
        }
        return false;
    }

    public String getNameNPC() {
        String prefix = "\u00a7l\u00a7bSumo";
        String br = "\n\u00a7r";
        return String.valueOf(prefix) + br + "\u00a7a" + this.players + " \u00a7eJugando";
    }

    public void setPlayersArena() {
        int total;
        if (this.countArchivos() > 0) {
            String[] games;
            total = 0;
            for (String w : games = new File(this.getDataFolder() + "/Arenas/").list()) {
                Config game = new Config(this.getDataFolder() + "/Arenas/" + w, 2);
                for (Player p : this.getServer().getLevelByName(game.getString("level")).getPlayers().values()) {
                    if (p.getGamemode() != 2) continue;
                    ++total;
                }
            }
        } else {
            total = 0;
        }
        this.players = total;
    }

    public void updateSystem() {
        int ingame = 1;
        int wating = 0;
        if (this.id >= this.countArchivos()) {
            this.id = 0;
            this.namefinal = "ARENA.NO.FOUND";
        }
        if (this.countArchivos() > 0) {
            Config game = new Config(this.getDataFolder() + "/Arenas/sumo-" + this.id + ".yml", 2);
            if (game.getInt("status") == ingame) {
                ++this.id;
            } else if (game.getInt("status") == wating) {
                this.namefinal = "sumo-" + this.id;
                this.id = 0;
            }
        }
    }

    public void joinGame(Player player, String game) {
        String name = "\u00a77" + player.getName();
        Config arena = new Config(this.getDataFolder() + "/Arenas/" + game + ".yml", 2);
        if (arena.getInt("status") == 0) {
            int x1 = arena.getInt("game1x");
            int y1 = arena.getInt("game1y");
            int z1 = arena.getInt("game1z");
            int x2 = arena.getInt("game2x");
            int y2 = arena.getInt("game2y");
            int z2 = arena.getInt("game2z");
            if (arena.getString("slot1").equals("undefine")) {
                arena.set("slot1", (Object)player.getName());
                arena.save();
                player.teleport(this.getServer().getLevelByName(arena.getString("level")).getSafeSpawn());
                player.teleport(new Vector3((double)x1, (double)y1, (double)z1));
                player.setImmobile(true);
                player.getInventory().clearAll();
                player.getInventory().setItem(8, Item.get((int)152, (Integer)0, (int)1).setCustomName("\u00a7l\u00a7bSalir"));
                player.setGamemode(2);
                player.setNameTag("\u00a7l\u00a76" + player.getName());
                Boss.sendBossBarToPlayer(player, arena.getInt("id"), "Sumo");
                Boss.setVida(player, arena.getInt("id"), 100);
                Boss.removeBossBar(player, 3984549);
                this.setFood(player, 20);
                player.setFoodEnabled(false);
            } else if (arena.getString("slot2").equals("undefine")) {
                arena.set("slot2", (Object)player.getName());
                arena.save();
                player.teleport(this.getServer().getLevelByName(arena.getString("level")).getSafeSpawn());
                player.teleport(new Vector3((double)x2, (double)y2, (double)z2));
                player.setImmobile(true);
                player.getInventory().clearAll();
                player.getInventory().setItem(8, Item.get((int)152, (Integer)0, (int)1).setCustomName("\u00a7l\u00a7bSalir"));
                player.setGamemode(2);
                player.setNameTag("\u00a7l\u00a76" + player.getName());
                Boss.sendBossBarToPlayer(player, arena.getInt("id"), "Sumo");
                Boss.setVida(player, arena.getInt("id"), 100);
                Boss.removeBossBar(player, 3984549);
                this.setFood(player, 20);
                player.setFoodEnabled(false);
            }
            for (Player p : this.getServer().getLevelByName(arena.getString("level")).getPlayers().values()) {
                this.getServer().getLevelByName(arena.getString("level")).addSound(new Vector3(p.getX(), p.getY(), p.getZ()), Sound.MOB_ENDERMEN_PORTAL);
                p.sendMessage(String.valueOf(Arena.title) + name + " \u00a7ase ha unido \u00a7e(\u00a7b" + this.countPlayersGM(game) + "\u00a7e/\u00a7b2\u00a7e)");
            }
        }
    }

    public void getMessagePlayerError(Player player) {
        player.sendMessage(String.valueOf(Arena.title) + "Comando no valido en Arena");
    }

    public void updateTextBoss(Player player, String name) {
        String text = "";
        Config config = new Config(this.getDataFolder() + "/Arenas/" + name, 2);
        int id = config.getInt("id");
        int status = config.getInt("status");
        int players = this.getServer().getLevelByName(config.getString("level")).getPlayers().size();
        if (players < 2 && status == 0) {
            text = "\n\n\u00a76Buscando oponente\u00a7b....";
        }
        if (players >= 2 && status == 1 && config.getInt("start") > 0) {
            text = "\n\n";
        }
        if (players >= 2 && status == 1 && config.getInt("start") == 0) {
            text = "\n\n\u00a7bEl duelo temrina en \u00a77: \u00a7e" + this.getReloj(config.getInt("time")) + " \u00a7bsegundos";
        }
        Boss.sendTitle(player, id, text);
    }

    public String getReloj(int num) {
        int hor = num / 3600;
        int min = (num - 3600 * hor) / 60;
        int seg = num - (hor * 3600 + min * 60);
        return "\u00a7e" + min + "\u00a7a:\u00a7e" + seg;
    }

    public void setFood(Player player, int food) {
        UpdateAttributesPacket upk = new UpdateAttributesPacket();
        upk.entityId = player.getId();
        Attribute attr = Attribute.getAttribute((int)7);
        attr.setMaxValue(20.0f);
        attr.setMinValue(0.0f);
        attr.setValue((float)food);
        upk.entries = new Attribute[]{attr};
        player.dataPacket((DataPacket)upk);
    }

    public void setVida(Player player, String name) {
        int i = 100;
        Config config = new Config(this.getDataFolder() + "/Arenas/" + name, 2);
        int id = config.getInt("id");
        int status = config.getInt("status");
        int value = config.getInt("start");
        if (this.players >= 2 && status == 1 && value > 0) {
            if (value == 10) {
                i = 100;
            }
            if (value == 9) {
                i = 90;
            }
            if (value == 8) {
                i = 80;
            }
            if (value == 7) {
                i = 70;
            }
            if (value == 6) {
                i = 60;
            }
            if (value == 5) {
                i = 50;
            }
            if (value == 4) {
                i = 40;
            }
            if (value == 3) {
                i = 25;
            }
            if (value == 2) {
                i = 10;
            }
            if (value == 1) {
                i = 0;
            }
            if (value == 0) {
                i = 0;
            }
        } else {
            i = 100;
        }
        Boss.setVida(player, id, i);
    }

    public int countPlayersGM(String name) {
        int i = 0;
        Config arena = new Config(this.getDataFolder() + "/Arenas/" + name + ".yml", 2);
        for (Player p : this.getServer().getLevelByName(arena.getString("level")).getPlayers().values()) {
            if (p.getGamemode() != 2) continue;
            ++i;
        }
        return i;
    }
}

