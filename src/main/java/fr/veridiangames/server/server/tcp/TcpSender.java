package fr.veridiangames.server.server.tcp;

import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataStream;
import fr.veridiangames.server.server.tcp.client.RemoteClient;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TcpSender implements Runnable
{

	private NetworkServerTCP server;
	private Thread thread;

	public TcpSender(NetworkServerTCP server)
	{
		this.server = server;
		this.thread = new Thread(this, "tcp-sender");
	}

	public TcpSender start()
	{
		this.thread.start();
		return this;
	}

	public void stop()
	{
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run()
	{
		server.log("TCP: tcp-sender thread started !");
		while (server.getSocket() != null)
		{
			synchronized (server.getClients())
			{
				if (server.getClients().size() == 0)
					continue;
				for (int i = 0; i < server.getClients().size(); i++)
				{
					RemoteClient client = server.getClients().get(i);
					if (client == null)
					{
						server.log("TCP: SENDER: null client !");
						continue;
					}
					ConcurrentLinkedQueue<Packet> q = client.getSendingQueue();
					if (q == null)
					{
						server.log("TCP: SENDER: null send queue for client ID: " + client.getId());
						continue;
					}
					while (!q.isEmpty())
					{
						Packet p = q.poll();
						if (p == null)
						{
							server.log("TCP: SENDER: null packet for client ID: " + client.getId());
							break;
						}
						byte[] packetData = p.getData().getData();
						if (packetData.length == 0)
						{
							server.log("TCP: SENDER: empty packet for client ID: " + client.getId());
							break;
						}
						try {
							DataStream.writePacket(client.getOutputStream(), packetData);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		server.log("TCP: Stopping tcp-sender Thread");
	}
}
