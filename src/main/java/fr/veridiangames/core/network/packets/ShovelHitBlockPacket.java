package fr.veridiangames.core.network.packets;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.particles.ParticleSystem;
import fr.veridiangames.core.game.entities.particles.ParticlesBulletHit;
import fr.veridiangames.core.maths.Vec3i;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.core.utils.Indexer;

import java.net.InetAddress;

/**
 * Created by Jimi Vacarians on 08/09/2017.
 */
public class ShovelHitBlockPacket extends Packet{
	private Vec3i position;
	private float damage;
	private int block;

	public ShovelHitBlockPacket()
	{
		super(SHOVEL_HIT_BLOCK);
	}

	public ShovelHitBlockPacket(Vec3i pos, float damage, int block)
	{
		super(SHOVEL_HIT_BLOCK);

		data.put(pos.x);
		data.put(pos.y);
		data.put(pos.z);

		data.put(damage);
		data.put(block);

		data.flip();
	}

	public ShovelHitBlockPacket(ShovelHitBlockPacket packet)
	{
		super(SHOVEL_HIT_BLOCK);

		data.put(packet.position.x);
		data.put(packet.position.y);
		data.put(packet.position.z);

		data.put(packet.damage);
		data.put(packet.block);

		data.flip();
	}

	public void read(DataBuffer data)
	{
		position = new Vec3i(data.getInt(), data.getInt(), data.getInt());
		damage = data.getFloat();
		block = data.getInt();
	}

	public void process(NetworkableServer server, InetAddress address, int port)
	{
		if(position.y <= 0) return;

		this.block = GameCore.getInstance().getGame().getWorld().applyBlockDamage(position.x, position.y, position.z, damage);
		if(Color4f.getColorFromARGB(block).getAlpha() <= 0)
			this.block = 0;
		server.getCore().getGame().getWorld().addModifiedBlock(position.x, position.y, position.z, block);
		server.tcpSendToAll(new ShovelHitBlockPacket(this));
	}

	public void process(NetworkableClient client, InetAddress address, int port)
	{
		client.getCore().getGame().getWorld().addBlock(position.x, position.y, position.z, block);
		client.getCore().getGame().getWorld().updateRequest(position.x, position.y, position.z);
		client.getCore().getGame().getWorld().addModifiedBlock(position.x, position.y, position.z, block);
	}
}