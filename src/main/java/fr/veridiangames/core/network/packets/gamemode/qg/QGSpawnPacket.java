package fr.veridiangames.core.network.packets.gamemode.qg;

import fr.veridiangames.core.game.gamemodes.QGGameMode;
import fr.veridiangames.core.game.gamemodes.TDMGameMode;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.core.utils.Log;

import java.net.InetAddress;

public class QGSpawnPacket extends Packet
{

	private Vec3 redSpawn = new Vec3();
	private Vec3 blueSpawn = new Vec3();

	public QGSpawnPacket() {
		super(Packet.GAMEMODE_QG_SPAWN);
	}

	public QGSpawnPacket(Vec3 red, Vec3 blue) {
		super(Packet.GAMEMODE_QG_SPAWN);
		data.put(red.x);
		data.put(red.y);
		data.put(red.z);
		data.put(blue.x);
		data.put(blue.y);
		data.put(blue.z);
		data.flip();
	}

	@Override
	public void read(DataBuffer buffer) {
		redSpawn.x = buffer.getFloat();
		redSpawn.y = buffer.getFloat();
		redSpawn.z = buffer.getFloat();
		blueSpawn.x = buffer.getFloat();
		blueSpawn.y = buffer.getFloat();
		blueSpawn.z = buffer.getFloat();
	}

	@Override
	public void process(NetworkableServer server, InetAddress address, int port) {

	}

	@Override
	public void process(NetworkableClient client, InetAddress address, int port) {
		QGGameMode mode = (QGGameMode) client.getCore().getGame().getGameMode();
		mode.getRedTeam().setSpawn(redSpawn);
		mode.getBlueTeam().setSpawn(blueSpawn);
	}
}
