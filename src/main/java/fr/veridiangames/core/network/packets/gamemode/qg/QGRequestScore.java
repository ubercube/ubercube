package fr.veridiangames.core.network.packets.gamemode.qg;

import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;

import java.net.InetAddress;

public class QGRequestScore extends Packet
{
	public QGRequestScore() { super(Packet.GAMEMODE_QG_STATS); }

	@Override
	public void read(DataBuffer data)
	{

	}

	@Override
	public void process(NetworkableServer server, InetAddress address, int port)
	{

	}

	@Override
	public void process(NetworkableClient client, InetAddress address, int port)
	{

	}
}
