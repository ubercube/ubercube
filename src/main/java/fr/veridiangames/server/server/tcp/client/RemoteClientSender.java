package fr.veridiangames.server.server.tcp.client;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataStream;
import fr.veridiangames.core.utils.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketException;

public class RemoteClientSender extends Thread
{
	private RemoteClient client;

	public RemoteClientSender(RemoteClient client)
	{
		this.client = client;
	}

	public void run()
	{
		while (client.getSocket() != null && !client.getSocket().isClosed())
		{
			while (!client.getSendingQueue().isEmpty())
			{
				try {
					Packet p = client.getSendingQueue().poll();
					if (p == null)
					{
						client.getServer().log("TCP: SENDER: null packet for client ID: " + client.getId());
						break;
					}
					byte[] packetData = p.getData().getData();
					if (packetData.length == 0)
					{
						client.getServer().log("TCP: SENDER: empty packet for client ID: " + client.getId());
						break;
					}
					client.getOut().writeInt(packetData.length);
					client.getOut().write(packetData);
					client.getOut().flush();
				} catch (IOException e) {
					Log.exception(e);
					GameCore.getInstance().getGame().getGameMode().onPlayerDisconnect(client.getId(), client.getServer());
					client.stop();
					return;
				}
			}
		}
	}
}
