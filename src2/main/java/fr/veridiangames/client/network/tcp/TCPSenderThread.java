package fr.veridiangames.client.network.tcp;

import java.io.IOException;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.Log;
import fr.veridiangames.core.utils.Time;

public class TCPSenderThread extends Thread
{
	private NetworkClientTCP client;
	private boolean running;

	public TCPSenderThread(NetworkClientTCP client)
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
		while (this.running && this.client.getSocket() != null)
			while (!this.client.getSendQueue().isEmpty())
			{
				Packet packet = this.client.getSendQueue().poll();
				try {
					if (packet.getData() == null)
					{
						this.client.log("TCP: " + Time.getTime() + " [ERROR]-> Tried to send an empty packet");
						this.client.log("TCP: " + Time.getTime() + " [ERROR]-> " + packet);
						return;
					}
					byte[] bytes = packet.getData().getData();
					if (bytes.length == 0)
					{
						this.client.log("TCP: " + Time.getTime() + " [ERROR]-> Tried to send an empty packet");
						return;
					}
					if (GameCore.isDisplayNetworkDebug())
						this.client.log("TCP: " + Time.getTime() + " [OUT]-> sending: " + packet);

					if (!this.client.getSocket().isClosed() && this.client.getSocket().isConnected())
					{
						this.client.getOut().writeInt(bytes.length);
						this.client.getOut().write(bytes);
					}
				} catch (IOException e) {
					Log.exception(e);
					this.client.close();
				}
			}
	}
}
