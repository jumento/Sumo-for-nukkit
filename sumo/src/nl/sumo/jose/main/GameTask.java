package nl.sumo.jose.main;
import nl.sumo.jose.main.Sumo;
import java.io.File;
import cn.nukkit.utils.Config;
import cn.nukkit.level.Level;
import cn.nukkit.Player;
import nl.sumo.jose.main.Arena;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import nl.sumo.jose.main.Boss;
public class GameTask extends cn.nukkit.scheduler.Task{
	private Sumo sumo;
	public GameTask(Sumo pl){
		this.sumo = pl;
	}
	@Override
	public void onRun(int tick) {
String[] games =  new File(sumo.getDataFolder()+"/Arenas/").list();
for(String configs : games){
	Config arena = new Config(sumo.getDataFolder()+"/Arenas/"+configs,Config.YAML);
	Level levelArena = sumo.getServer().getLevelByName(arena.getString("level"));
	for(Player p : levelArena.getPlayers().values()){
	sumo.updateTextBoss(p, configs);
	sumo.setVida(p, configs);
	}
	if(levelArena instanceof Level){
		
	if(levelArena.getPlayers().size() == 0){
		arena.set("status", 0);
		arena.set("time", 60*3);
		arena.set("start", 10);
		arena.set("slot1", "undefine");
		arena.set("slot2", "undefine");
		arena.save();
	}
	
	if(levelArena.getPlayers().size() >= 2 && arena.getInt("start") > 0){
		if(this.countPlayersGM(configs) == 2){
		arena.set("status", 1);
		arena.save();
		int time = arena.getInt("start");
		if(time > 0){
			time--;
			arena.set("start", time);
			arena.save();
			for(Player p : levelArena.getPlayers().values()){
				p.sendTitle("§a"+time);
				p.getInventory().clearAll();
				sumo.getServer().getLevelByName(arena.getString("level")).addSound(new Vector3(p.getX(),p.getY(),p.getZ()), Sound.RANDOM_LEVELUP);
			}
			if(time == 0){
				arena.set("status", 1);
				arena.save();
				for(Player p : levelArena.getPlayers().values()){
sumo.getServer().getLevelByName(arena.getString("level")).addSound(new Vector3(p.getX(),p.getY(),p.getZ()), Sound.MOB_VILLAGER_YES);
					p.sendTitle("");
					p.setImmobile(false);
					p.getInventory().clearAll();
				}
			}
		}
		
	}
	}
	if(levelArena.getPlayers().size() >= 2 && arena.getInt("status") == 1 && arena.getInt("start") == 0){
		for(Player p : levelArena.getPlayers().values()){
			
			p.setHealth(20);
			
		}
		int time = arena.getInt("time");
		time--;
		arena.set("time", time);
		arena.save();
		//finsles
		if(time == 60*3-1){
			for(Player p : levelArena.getPlayers().values()){
				p.sendMessage(Arena.title+" §6El duelo a comenzado tira a tu oponente");
				p.sendTitle("");
			}
		}
		if(this.countPlayersGM(configs) == 1 && arena.getInt("status") == 1){
		
			String name = "";
			for(Player p : levelArena.getPlayers().values()){
				if(p.getGamemode() == 2){
					name = p.getName();
					
					
				}
				p.teleport(sumo.getServer().getDefaultLevel().getSafeSpawn());
				p.setGamemode(2);
				p.getInventory().clearAll();
				sumo.setFood(p,20);
				Boss.removeBossBar(p, arena.getInt("id"));
				
				
			}
	for(Player p : sumo.getServer().getOnlinePlayers().values()){
		p.sendMessage(Arena.title+"§a"+name+" §7Ha ganado en §8: §d"+arena.getString("nameArena"));		
			}
	arena.set("status", 0);
	arena.set("time", 60*3);
	arena.set("start", 10);
	arena.set("slot1", "undefine");
	arena.set("slot2", "undefine");
	arena.save();
	
		}
		if(this.countPlayersGM(configs) == 0 && arena.getInt("status") == 1){
			for(Player p : levelArena.getPlayers().values()){
				p.teleport(sumo.getServer().getDefaultLevel().getSafeSpawn());
				p.setGamemode(2);
				p.getInventory().clearAll();
				sumo.setFood(p,20);
				Boss.removeBossBar(p, arena.getInt("id"));
				
				
			}
	for(Player p : sumo.getServer().getOnlinePlayers().values()){
		p.sendMessage(Arena.title+"§7No hubo ganadores  en §8: §d"+arena.getString("nameArena"));		
			}
			
			arena.set("status", 0);
			arena.set("time", 60*3);
			arena.set("start", 10);
			arena.set("slot1", "undefine");
			arena.set("slot2", "undefine");
			arena.save();		
		}
		if(time == 0){
			for(Player p : levelArena.getPlayers().values()){
				p.teleport(sumo.getServer().getDefaultLevel().getSafeSpawn());
				p.setGamemode(2);
				p.getInventory().clearAll();
				sumo.setFood(p,20);
				Boss.removeBossBar(p, arena.getInt("id"));
				
				
			}
	for(Player p : sumo.getServer().getOnlinePlayers().values()){
		p.sendMessage(Arena.title+"§7No hubo ganadores  en §8: §d"+arena.getString("nameArena"));		
			}
			
			arena.set("status", 0);
			arena.set("time", 60*3);
			arena.set("start", 10);
			arena.set("slot1", "undefine");
			arena.set("slot2", "undefine");
			arena.save();	
		}
		
		
	}
		
	}
}
	}
	public int countPlayersGM(String name){
		int i = 0;
		Config arena = new Config(sumo.getDataFolder()+"/Arenas/"+name,Config.YAML);
		for(Player p :sumo.getServer().getLevelByName(arena.getString("level")).getPlayers().values()){
			if(p.getGamemode() == 2){
				i++;
			}
		}
		return i;
	}
	public String getReloj(int num){
		    int hor=num/3600;
	        int min=(num-(3600*hor))/60;
	        int seg=num-((hor*3600)+(min*60));
     return "§e"+min+"§a:§e"+seg;
	}
}
