package fr.veridiangames.core.game.gamemodes;

import java.util.HashMap;

/**
 * Created by Jimi Vacarians on 04/09/2017.
 */
public class PlayerStats {
	private HashMap<Integer, HashMap<Stats, Object>> stats = new HashMap<>();

	public HashMap<Stats, Object> get(int id){
		return stats.get(id);
	}

	public HashMap<Integer, HashMap<Stats, Object>> get(){
		return stats;
	}

	public void set(int id, Stats statName, Object value){
		if(!stats.containsKey(id)){
			stats.put(id, new HashMap<>());
		}

		stats.get(id).put(statName, value);
	}

	public void set(HashMap<Integer, HashMap<Stats, Object>> h){
		this.stats = h;
	}

	public void remove(int id){
		if(stats.containsKey(id)) {
			stats.remove(id);
		}
	}

	public enum Stats{
		KILLS, //int
		DEATHS //int
	}
}
