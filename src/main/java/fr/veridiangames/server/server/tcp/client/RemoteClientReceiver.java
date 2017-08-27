package fr.veridiangames.server.server.tcp.client;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.network.PacketManager;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.core.utils.DataStream;

import java.io.IOException;

import static fr.veridiangames.core.utils.Time.getTime;

public class RemoteClientReceiver extends Thread
{
	private RemoteClient client;

	public RemoteClientReceiver(RemoteClient client)
	{
		this.client = client;
	}

	public void run()
	{
		while (client.getSocket() != null && !client.getSocket().isClosed())
		{
			try {
				byte[] bytes = DataStream.readPacket(client.getInputStream());
				DataBuffer data = new DataBuffer(bytes);
				Packet packet = PacketManager.getPacket(data.getInt());

				if (packet == null)
				{
					client.getServer().log("TCP: " + getTime() + " [ERROR]-> Received empty packet");
					return;
				}

				if (GameCore.isDisplayNetworkDebug())
					client.getServer().log("TCP: " + getTime() + " [IN]-> received: " + packet);

				packet.read(data);
				packet.process(client.getServer(), client.getSocket().getInetAddress(), client.getSocket().getPort());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
