/*
 * Copyright (C) 2016 Team Ubercube
 *
 * This file is part of Ubercube.
 *
 *     Ubercube is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Ubercube is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Ubercube.  If not, see http://www.gnu.org/licenses/.
 */

package fr.veridiangames.core.network.packets;

import java.net.InetAddress;

import fr.veridiangames.core.maths.Vec3i;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.core.utils.DataBuffer;

/**
 * Created by Marccspro on 26 f�vr. 2016.
 */
public class BulletHitBlockPacket extends Packet
{
	private Vec3i position;
	private float damage;
	private int block;
	
	public BulletHitBlockPacket()
	
	{
		super(BULLET_HIT_BLOCK);
	}
	
	public BulletHitBlockPacket(Vec3i pos, float damage, int block)
	{
		super(BULLET_HIT_BLOCK);
		
		data.put(pos.x);
		data.put(pos.y);
		data.put(pos.z);

		data.put(damage);
		data.put(block);

		data.flip();
	}
	
	public BulletHitBlockPacket(BulletHitBlockPacket packet)
	{
		super(BULLET_HIT_BLOCK);
		
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
		this.block = server.getCore().getGame().getWorld().applyBlockDamage(this.block, damage);
		if(Color4f.getColorFromARGB(block).getAlpha() <= 0)
			this.block = 0;
		server.getCore().getGame().getWorld().addModifiedBlock(position.x, position.y, position.z, block);
		server.sendToAll(new BulletHitBlockPacket(this));
	}

	public void process(NetworkableClient client, InetAddress address, int port)
	{
		client.getCore().getGame().getWorld().addBlock(position.x, position.y, position.z, block);
		client.getCore().getGame().getWorld().updateRequest(position.x, position.y, position.z);
		client.getCore().getGame().getWorld().addModifiedBlock(position.x, position.y, position.z, block);
	}


}