package fr.veridiangames.server.server.tcp;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.network.PacketManager;
import fr.veridiangames.core.network.packets.DisconnectPacket;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.core.utils.DataStream;
import fr.veridiangames.server.server.tcp.client.RemoteClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static fr.veridiangames.core.utils.Time.getTime;

public class TcpReceiver implements Runnable
{
	private NetworkServerTCP server;
	private Thread thread;

	public TcpReceiver(NetworkServerTCP server)
	{
		this.server = server;
		this.thread = new Thread(this, "tcp-receiver");
	}

	public TcpReceiver start()
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
		List<RemoteClient> receiverThreadClients = new ArrayList<>();

		while (server.getSocket() != null)
		{
			server.getClientsToAdd().removeAll(receiverThreadClients); // java.lang.ArrayIndexOutOfBoundsException
			receiverThreadClients.addAll(server.getClientsToAdd());

			ArrayList<RemoteClient> toRemove = new ArrayList<>();
			for (RemoteClient client : receiverThreadClients) {
				if (!server.getClients().contains(client))
					toRemove.add(client);
			}

			receiverThreadClients.removeAll(toRemove);

			for (RemoteClient client : receiverThreadClients)
			{
				try
				{
					if (client.getInputStream() == null)
					{
						server.disconnectClient(client.getSocket().getInetAddress(), client.getSocket().getPort());
						server.getServer().tcpSendToAll(new DisconnectPacket(client.getId(), "Client disconnected by the server"));
						continue;
					}

					if (client.getInputStream().available() < Packet.MAX_SIZE) {
						continue;
					}

					byte[] bytes = DataStream.readPacket(client.getInputStream());
					DataBuffer data = new DataBuffer(bytes);
					Packet packet = PacketManager.getPacket(data.getInt());

					if (packet == null)
					{
						server.log("TCP: " + getTime() + " [ERROR]-> Received empty packet");
						continue;
					}

					if (GameCore.isDisplayNetworkDebug())
						server.log("TCP: " + getTime() + " [IN]-> received: " + packet);

					packet.read(data);
					packet.process(server.getServer(), client.getSocket().getInetAddress(), client.getSocket().getPort());
				} catch (IOException e)
				{
					server.disconnectClient(client.getSocket().getInetAddress(), client.getSocket().getPort());
					server.getServer().tcpSendToAll(new DisconnectPacket(client.getId(), "Client disconnected by the server"));
				}
			}
		}
		server.log("TCP: Stopping tcp-receiver Thread");
	}
}
