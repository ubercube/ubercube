package fr.veridiangames.core.network.packets.gamemode.tdm;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.gamemodes.PlayerStats;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jimi Vacarians on 04/09/2017.
 */
public class TDMPlayerStatsPacket extends Packet {
	private HashMap<Integer, HashMap<PlayerStats.Stats, Object>> stats;

	public TDMPlayerStatsPacket() { super(Packet.GAMEMODE_TDM_STATS);}

	public TDMPlayerStatsPacket(PlayerStats ps) {
		super(Packet.GAMEMODE_TDM_STATS);
		stats = ps.get();
		data.put(stats.size());
		for(Map.Entry<Integer, HashMap<PlayerStats.Stats, Object>> e : stats.entrySet()){
			data.put(e.getKey());
			data.put((int)e.getValue().get(PlayerStats.Stats.KILLS));
			data.put((int)e.getValue().get(PlayerStats.Stats.DEATHS));
		}
		data.flip();
	}

	@Override
	public void read(DataBuffer data) {
		stats = new HashMap<>();
		int size = data.getInt();
		HashMap<PlayerStats.Stats, Object> h = new HashMap<>();
		int id = data.getInt();
		int kills = data.getInt();
		int deaths = data.getInt();
		for(int i=0;i<size;i++){
			h.put(PlayerStats.Stats.KILLS, kills);
			h.put(PlayerStats.Stats.DEATHS, deaths);
			stats.put(id, h);
		}
	}

	@Override
	public void process(NetworkableServer server, InetAddress address, int port) {

	}

	@Override
	public void process(NetworkableClient client, InetAddress address, int port) {
		GameCore.getInstance().getGame().getGameMode().getPlayerStats().set(stats);
	}
}
