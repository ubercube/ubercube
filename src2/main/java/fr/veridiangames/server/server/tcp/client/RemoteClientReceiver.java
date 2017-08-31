package fr.veridiangames.server.server.tcp.client;

import static fr.veridiangames.core.utils.Time.getTime;

import java.io.IOException;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.network.PacketManager;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.core.utils.Log;

public class RemoteClientReceiver extends Thread
{
	private RemoteClient client;

	public RemoteClientReceiver(RemoteClient client)
	{
		this.client = client;
	}

	@Override
	public void run()
	{
		while (this.client.getSocket() != null && !this.client.getSocket().isClosed())
			try {
				int length = this.client.getIn().readInt();
				byte[] bytes = new byte[length];
				this.client.getIn().readFully(bytes);

				DataBuffer data = new DataBuffer(bytes);
				Packet packet = PacketManager.getPacket(data.getInt());

				if (packet == null)
				{
					this.client.getServer().log("TCP: " + getTime() + " [ERROR]-> Received empty packet");
					return;
				}

				if (GameCore.isDisplayNetworkDebug())
					this.client.getServer().log("TCP: " + getTime() + " [IN]-> received: " + packet);

				packet.read(data);
				packet.process(this.client.getServer(), this.client.getSocket().getInetAddress(), this.client.getSocket().getPort());
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
