package fr.veridiangames.client.network.tcp;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.network.PacketManager;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.core.utils.DataStream;
import fr.veridiangames.core.utils.Time;

import java.io.DataInputStream;
import java.io.IOException;

public class TCPReceiverThread extends Thread
{
	private NetworkClientTCP client;
	private boolean running;

	public TCPReceiverThread(NetworkClientTCP client)
	{
		this.client = client;
		this.running = true;
	}

	public void stopRunning()
	{
		running = false;
	}

	public void run()
	{
		client.log("TCP: Starting tcp-receiver");
		while (running && client.getSocket() != null && !client.getSocket().isClosed())
		{
			try
			{
				int length = client.getIn().readInt();
				byte[] bytes = new byte[length];
				client.getIn().readFully(bytes);

				DataBuffer data = new DataBuffer(bytes);
				int id = data.getInt();
				Packet packet = PacketManager.getPacket(id);
				client.log("Received packet " + packet + " of size: " + length);
				if (packet == null)
				{
					client.log("TCP: " + Time.getTime() + " [ERROR]-> Received null packet, packet ID: " + id);
					continue;
				}
				if (GameCore.isDisplayNetworkDebug())
					client.log("TCP: " + Time.getTime() + " [IN]-> received: " + packet);

				packet.read(data);
				packet.process(client.getClient(), client.getAddress(), client.getPort());
			} catch (IOException e)
			{
				e.printStackTrace();
				try {
					client.getSocket().close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		client.log("TCP: Stopping tcp-receiver Thread");
	}
}
