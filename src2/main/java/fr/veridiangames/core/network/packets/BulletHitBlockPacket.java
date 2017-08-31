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

import fr.veridiangames.core.GameCore;
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
	private int honerID;
	private Vec3i position;
	private float damage;
	private int block;

	public BulletHitBlockPacket()
	{
		super(BULLET_HIT_BLOCK);
	}

	public BulletHitBlockPacket(int honerID, Vec3i pos, float damage, int block)
	{
		super(BULLET_HIT_BLOCK);

		this.data.put(honerID);

		this.data.put(pos.x);
		this.data.put(pos.y);
		this.data.put(pos.z);

		this.data.put(damage);
		this.data.put(block);

		this.data.flip();
	}

	public BulletHitBlockPacket(BulletHitBlockPacket packet)
	{
		super(BULLET_HIT_BLOCK);

		this.data.put(packet.honerID);

		this.data.put(packet.position.x);
		this.data.put(packet.position.y);
		this.data.put(packet.position.z);

		this.data.put(packet.damage);
		this.data.put(packet.block);

		this.data.flip();
	}

	@Override
	public void read(DataBuffer data)
	{
		this.honerID = data.getInt();
		this.position = new Vec3i(data.getInt(), data.getInt(), data.getInt());
		this.damage = data.getFloat();
		this.block = data.getInt();
	}

	@Override
	public void process(NetworkableServer server, InetAddress address, int port)
	{
		if(this.position.y <= 0) return;

		this.block = GameCore.getInstance().getGame().getWorld().applyBlockDamage(this.position.x, this.position.y, this.position.z, this.damage);
		if(Color4f.getColorFromARGB(this.block).getAlpha() <= 0)
			this.block = 0;
		server.getCore().getGame().getWorld().addModifiedBlock(this.position.x, this.position.y, this.position.z, this.block);
		server.tcpSendToAll(new BulletHitBlockPacket(this));
	}

	@Override
	public void process(NetworkableClient client, InetAddress address, int port)
	{
		client.getCore().getGame().getWorld().addBlock(this.position.x, this.position.y, this.position.z, this.block);
		client.getCore().getGame().getWorld().updateRequest(this.position.x, this.position.y, this.position.z);
		client.getCore().getGame().getWorld().addModifiedBlock(this.position.x, this.position.y, this.position.z, this.block);
	}
}