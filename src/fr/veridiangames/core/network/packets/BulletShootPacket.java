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

import fr.veridiangames.core.audio.Audio;
import fr.veridiangames.core.game.entities.bullets.Bullet;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.core.utils.Indexer;

import java.net.InetAddress;

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

		data.put(clientID);

		data.put(bullet.getID());
		data.put(bullet.getName());

		data.put(bullet.getPosition().x);
		data.put(bullet.getPosition().y);
		data.put(bullet.getPosition().z);

		data.put(bullet.getRotation().x);
		data.put(bullet.getRotation().y);
		data.put(bullet.getRotation().z);
		data.put(bullet.getRotation().w);

		data.put(bullet.getForce());

		data.flip();
	}

	public BulletShootPacket(BulletShootPacket packet)
	{
		super(BULLET_SHOOT);

		data.put(packet.clientID);

		data.put(packet.id);
		data.put(packet.name);

		data.put(packet.position.x);
		data.put(packet.position.y);
		data.put(packet.position.z);

		data.put(packet.rotation.x);
		data.put(packet.rotation.y);
		data.put(packet.rotation.z);
		data.put(packet.rotation.w);

		data.put(packet.shootForce);

		data.flip();
	}

	public void read(DataBuffer data)
	{
		clientID = data.getInt();
		id = data.getInt();
		name = data.getString();
		position = new Vec3(data.getFloat(), data.getFloat(), data.getFloat());
		rotation = new Quat(data.getFloat(), data.getFloat(), data.getFloat(), data.getFloat());
		shootForce = data.getFloat();
	}

	public void process(NetworkableServer server, InetAddress address, int port)
	{
		id = Indexer.getUniqueID();
		server.sendToAny(new BulletShootPacket(this), clientID);
	}

	public void process(NetworkableClient client, InetAddress address, int port)
	{
		Bullet bullet = new Bullet(id, name, position, rotation, shootForce);
		bullet.setNetwork(client);
		client.getCore().getGame().spawn(bullet);
	}
}