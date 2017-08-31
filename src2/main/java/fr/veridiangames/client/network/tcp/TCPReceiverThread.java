package fr.veridiangames.client.network.tcp;

import java.io.IOException;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.network.PacketManager;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.core.utils.Log;
import fr.veridiangames.core.utils.Time;

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
		this.running = false;
	}

	@Override
	public void run()
	{
		this.client.log("TCP: Starting tcp-receiver");
		while (this.running && this.client.getSocket() != null && !this.client.getSocket().isClosed())
			try
			{
				int length = this.client.getIn().readInt();
				byte[] bytes = new byte[length];
				this.client.getIn().readFully(bytes);

				DataBuffer data = new DataBuffer(bytes);
				int id = data.getInt();
				Packet packet = PacketManager.getPacket(id);
				this.client.log("Received packet " + packet + " of size: " + length);
				if (packet == null)
				{
					this.client.log("TCP: " + Time.getTime() + " [ERROR]-> Received null packet, packet ID: " + id);
					continue;
				}
				if (GameCore.isDisplayNetworkDebug())
					this.client.log("TCP: " + Time.getTime() + " [IN]-> received: " + packet);

				packet.read(data);
				packet.process(this.client.getClient(), this.client.getAddress(), this.client.getPort());
			} catch (IOException e)
			{
				Log.exception(e);
				this.client.close();
			}
		this.client.log("TCP: Stopping tcp-receiver Thread");
	}
}
