package fr.veridiangames.core.network.packets;

import java.net.InetAddress;

import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;

public class CurrentBlockPacket extends Packet
{
	int block, player;
	public CurrentBlockPacket()
	{
		super(CURRENT_BLOCK);
	}
	public CurrentBlockPacket(int blockID, int playerID)
	{
		super(CURRENT_BLOCK);
		this.data.put(blockID);
		this.data.put(playerID);
	}

	@Override
	public void read(DataBuffer data)
	{
		this.block = data.getInt();
		this.player = data.getInt();
	}

	@Override
	public void process(NetworkableServer server, InetAddress address, int port)
	{
		Entity e = server.getCore().getGame().getEntityManager().get(this.player);
		if (e != null)
			if (e instanceof Player)
			{
				((Player)e).setCurrentBlock(this.block);
				server.tcpSendToAll(new CurrentBlockPacket(block, player));
			}
	}

	@Override
	public void process(NetworkableClient client, InetAddress address, int port)
	{
		Entity e = client.getCore().getGame().getEntityManager().get(this.player);
		if (e != null)
			if (e instanceof Player)
				((Player)e).setCurrentBlock(this.block);
	}
}
