package fr.veridiangames.server.server.tcp.client;

import java.io.IOException;

import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.Log;

public class RemoteClientSender extends Thread
{
	private RemoteClient client;

	public RemoteClientSender(RemoteClient client)
	{
		this.client = client;
	}

	@Override
	public void run()
	{
		while (this.client.getSocket() != null && !this.client.getSocket().isClosed())
			while (!this.client.getSendingQueue().isEmpty())
				try {
					Packet p = this.client.getSendingQueue().poll();
					if (p == null)
					{
						this.client.getServer().log("TCP: SENDER: null packet for client ID: " + this.client.getId());
						break;
					}
					byte[] packetData = p.getData().getData();
					if (packetData.length == 0)
					{
						this.client.getServer().log("TCP: SENDER: empty packet for client ID: " + this.client.getId());
						break;
					}
					this.client.getOut().writeInt(packetData.length);
					this.client.getOut().write(packetData);
					this.client.getOut().flush();
				} catch (IOException e) {
					Log.exception(e);
					try {
						this.client.stop();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
	}
}
