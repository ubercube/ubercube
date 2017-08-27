package fr.veridiangames.client.network.tcp;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataStream;
import fr.veridiangames.core.utils.Time;

import java.io.DataOutputStream;
import java.io.IOException;

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
		running = false;
	}

	public void run()
	{
		while (running && client.getSocket() != null)
		{
			while (!client.getSendQueue().isEmpty())
			{
				Packet packet = client.getSendQueue().poll();
				try {
					if (packet.getData() == null)
					{
						client.log("TCP: " + Time.getTime() + " [ERROR]-> Tried to send an empty packet");
						client.log("TCP: " + Time.getTime() + " [ERROR]-> " + packet);
						return;
					}
					byte[] bytes = packet.getData().getData();
					if (bytes.length == 0)
					{
						client.log("TCP: " + Time.getTime() + " [ERROR]-> Tried to send an empty packet");
						return;
					}
					if (GameCore.isDisplayNetworkDebug())
						client.log("TCP: " + Time.getTime() + " [OUT]-> sending: " + packet);

					if (!client.getSocket().isClosed() && client.getSocket().isConnected())
					{
						DataOutputStream outputStream = new DataOutputStream(client.getOut());
						outputStream.write(bytes);
					}
				} catch (IOException e) {
					e.printStackTrace();
					try {
						client.getSocket().close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}
}
