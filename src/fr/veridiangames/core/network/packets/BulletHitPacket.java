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

import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.bullets.StaticBullet;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;

/**
 * Created by Marccspro on 26 f�vr. 2016.
 */
public class BulletHitPacket extends Packet
{
	private int id;
	private Vec3 position;
	private Quat rotation;
	private int parentID;
	
	public BulletHitPacket()
	
	{
		super(BULLET_HIT);
	}
	
	public BulletHitPacket(StaticBullet bullet)
	{
		super(BULLET_HIT);
		data.put(bullet.getID());
		
		data.put(bullet.getPosition().x);
		data.put(bullet.getPosition().y);
		data.put(bullet.getPosition().z);
		
		data.put(bullet.getRotation().x);
		data.put(bullet.getRotation().y);
		data.put(bullet.getRotation().z);
		data.put(bullet.getRotation().w);
		
		if (bullet.getParent() == null)
			data.put(0);
		else
			data.put(bullet.getParent().getID());
		
		data.flip();
	}
	
	public BulletHitPacket(BulletHitPacket packet)
	{
		super(BULLET_HIT);
		data.put(packet.id);
		
		data.put(packet.position.x);
		data.put(packet.position.y);
		data.put(packet.position.z);
		
		data.put(packet.rotation.x);
		data.put(packet.rotation.y);
		data.put(packet.rotation.z);
		data.put(packet.rotation.w);
		
		data.put(packet.parentID);

		data.flip();
	}

	public void read(DataBuffer data)
	{
		id = data.getInt();
		position = new Vec3(data.getFloat(), data.getFloat(), data.getFloat());
		rotation = new Quat(data.getFloat(), data.getFloat(), data.getFloat(), data.getFloat());
		parentID = data.getInt();
	}

	public void process(NetworkableServer server, InetAddress address, int port)
	{
		server.sendToAll(new BulletHitPacket(this));
	}

	public void process(NetworkableClient client, InetAddress address, int port)
	{
		client.getCore().getGame().spawn(new StaticBullet(id, "", position, rotation));
	}
}