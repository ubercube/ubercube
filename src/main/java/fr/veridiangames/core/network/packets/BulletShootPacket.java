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

import fr.veridiangames.core.game.entities.bullets.Bullet;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.core.utils.Indexer;

/**
 * Created by Marccspro on 26 f�vr. 2016.
 */
public class BulletShootPacket extends Packet
{
	private int clientID;
	private int id;
	private String name;
	private Vec3 position;
	private Quat rotation;
	private float shootForce;

	public BulletShootPacket()
	{
		super(BULLET_SHOOT);
	}

	public BulletShootPacket(int clientID, Bullet bullet)
	{
		super(BULLET_SHOOT);

		this.data.put(clientID);

		this.data.put(bullet.getID());
		this.data.put(bullet.getName());

		this.data.put(bullet.getPosition().x);
		this.data.put(bullet.getPosition().y);
		this.data.put(bullet.getPosition().z);

		this.data.put(bullet.getRotation().x);
		this.data.put(bullet.getRotation().y);
		this.data.put(bullet.getRotation().z);
		this.data.put(bullet.getRotation().w);

		this.data.put(bullet.getForce());

		this.data.flip();
	}

	public BulletShootPacket(BulletShootPacket packet)
	{
		super(BULLET_SHOOT);

		this.data.put(packet.clientID);

		this.data.put(packet.id);
		this.data.put(packet.name);

		this.data.put(packet.position.x);
		this.data.put(packet.position.y);
		this.data.put(packet.position.z);

		this.data.put(packet.rotation.x);
		this.data.put(packet.rotation.y);
		this.data.put(packet.rotation.z);
		this.data.put(packet.rotation.w);

		this.data.put(packet.shootForce);

		this.data.flip();
	}

	@Override
	public void read(DataBuffer data)
	{
		this.clientID = data.getInt();
		this.id = data.getInt();
		this.name = data.getString();
		this.position = new Vec3(data.getFloat(), data.getFloat(), data.getFloat());
		this.rotation = new Quat(data.getFloat(), data.getFloat(), data.getFloat(), data.getFloat());
		this.shootForce = data.getFloat();
	}

	@Override
	public void process(NetworkableServer server, InetAddress address, int port)
	{
		this.id = Indexer.getUniqueID();
		server.udpSendToAll(new BulletShootPacket(this));
	}

	@Override
	public void process(NetworkableClient client, InetAddress address, int port)
	{
		Bullet bullet = new Bullet(this.id, this.clientID, this.name, this.position, this.rotation, this.shootForce);
		bullet.setNetwork(client);

		if (client.getID() != this.clientID)
			client.getCore().getGame().spawn(bullet);
	}
}