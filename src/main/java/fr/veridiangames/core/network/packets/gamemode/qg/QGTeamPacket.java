package fr.veridiangames.core.network.packets.gamemode.qg;

import fr.veridiangames.core.game.gamemodes.QGGameMode;
import fr.veridiangames.core.game.gamemodes.TDMGameMode;
import fr.veridiangames.core.game.gamemodes.Team;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class QGTeamPacket extends Packet
{
	private List<Integer> redTeam = new ArrayList<>();
	private List<Integer> blueTeam = new ArrayList<>();

	public QGTeamPacket() {
		super(Packet.GAMEMODE_QG_TEAM);
	}

	public QGTeamPacket(Team red, Team blue) {
		super(Packet.GAMEMODE_QG_TEAM);
		data.put(red.getPlayers().getSize());
		for(int id : red.getPlayers().getList()){
			data.put(id);
		}
		data.put(blue.getPlayers().getSize());
		for(int id : blue.getPlayers().getList()){
			data.put(id);
		}
		data.flip();
	}

	@Override
	public void read(DataBuffer buffer) {
		int size = buffer.getInt();
		for (int i=0;i<size;i++){
			redTeam.add(new Integer(buffer.getInt()));
		}

		size = buffer.getInt();
		for (int i=0;i<size;i++){
			blueTeam.add(new Integer(buffer.getInt()));
		}
	}

	@Override
	public void process(NetworkableServer server, InetAddress address, int port) {

	}

	@Override
	public void process(NetworkableClient client, InetAddress address, int port) {
		QGGameMode mode = (QGGameMode) client.getCore().getGame().getGameMode();
		mode.getRedTeam().getPlayers().clear();
		for(int i : redTeam){
			if(i != 0)
				mode.getRedTeam().getPlayers().add(i);
		}

		mode.getBlueTeam().getPlayers().clear();
		for(int i : blueTeam){
			if(i != 0)
				mode.getBlueTeam().getPlayers().add(i);
		}
	}
}
