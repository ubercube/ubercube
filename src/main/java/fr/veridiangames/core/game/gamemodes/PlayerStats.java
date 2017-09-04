package fr.veridiangames.core.game.gamemodes;

import java.util.HashMap;

/**
 * Created by Jimi Vacarians on 04/09/2017.
 */
public class PlayerStats {
	private HashMap<Integer, HashMap<String, Object>> stats = new HashMap<>();

	public HashMap<String, Object> get(int id){
		return stats.get(id);
	}

	private void set(int id, String statName, Object value){
		if(!stats.containsKey(id)){
			stats.put(id, new HashMap<>());
		}

		stats.get(id).put(statName, value);
	}
}
