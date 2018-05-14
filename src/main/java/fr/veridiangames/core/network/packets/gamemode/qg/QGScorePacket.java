package fr.veridiangames.core.network.packets.gamemode.qg;

import fr.veridiangames.core.game.gamemodes.QGGameMode;
import fr.veridiangames.core.game.gamemodes.TDMGameMode;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;

import java.net.InetAddress;

public class QGScorePacket extends Packet
{
	private int redTeam;
	private int blueTeam;

	public QGScorePacket() {
		super(Packet.GAMEMODE_QG_SCORE);
	}

	public QGScorePacket(int red, int blue) {
		super(Packet.GAMEMODE_QG_SCORE);
		data.put(red);
		data.put(blue);
		data.flip();
	}

	@Override
	public void read(DataBuffer buffer) {
		redTeam = buffer.getInt();
		blueTeam = buffer.getInt();
	}

	@Override
	public void process(NetworkableServer server, InetAddress address, int port) {

	}

	@Override
	public void process(NetworkableClient client, InetAddress address, int port) {
		QGGameMode mode = (QGGameMode) client.getCore().getGame().getGameMode();
		mode.setRedScore(redTeam);
		mode.setBlueScore(blueTeam);
	}
}
