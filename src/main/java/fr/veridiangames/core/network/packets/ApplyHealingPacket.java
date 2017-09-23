package fr.veridiangames.core.network.packets;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.game.entities.player.NetworkedPlayer;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.game.entities.player.ServerPlayer;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.network.Protocol;
import fr.veridiangames.core.utils.DataBuffer;

import java.net.InetAddress;

/**
 * Created by Jimi Vacarians on 23/09/2017.
 */
public class ApplyHealingPacket extends Packet
{
	private int id;
	private int heal;

	public ApplyHealingPacket()
	{
		super(APPLY_HEALING);
	}

	public ApplyHealingPacket(Player p, int heal)
	{
		super(APPLY_HEALING);

		data.put(p.getID());
		data.put(heal);

		data.flip();
	}

	public ApplyHealingPacket(int i, int heal)
	{
		super(APPLY_HEALING);

		data.put(i);
		data.put(heal);

		data.flip();
	}

	public void read(DataBuffer buffer)
	{
		this.id = buffer.getInt();
		this.heal = buffer.getInt();
	}

	public void process(NetworkableServer server, InetAddress address, int port)
	{
		ServerPlayer p = (ServerPlayer) GameCore.getInstance().getGame().getEntityManager().getEntities().get(id);
		p.setLife(p.getLife() + 10);
		server.tcpSendToAll(new ApplyHealingPacket(id, 10));
	}

	public void process(NetworkableClient client, InetAddress address, int port)
	{
		ClientPlayer p = client.getCore().getGame().getPlayer();
		if(p.getID() == id){
			p.setLife(p.getLife() + this.heal);
		}
	}
}
