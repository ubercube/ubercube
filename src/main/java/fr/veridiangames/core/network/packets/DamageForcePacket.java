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

import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.game.entities.player.ServerPlayer;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;

import java.net.InetAddress;

/**
 * Created by Marccspro on 26 f�vr. 2016.
 */
public class DamageForcePacket extends Packet
{
	private int authorID;
	private Vec3 position;
	private float force;

	public DamageForcePacket()

	{
		super(DAMAGE_FORCE);
	}

	public DamageForcePacket(int authorID, Vec3 pos, float force)
	{
		super(DAMAGE_FORCE);

		data.put(authorID);

		data.put(pos.x);
		data.put(pos.y);
		data.put(pos.z);

		data.put(force);

		data.flip();
	}

	public DamageForcePacket(DamageForcePacket packet)
	{
		super(DAMAGE_FORCE);

		data.put(packet.authorID);

		data.put(packet.position.x);
		data.put(packet.position.y);
		data.put(packet.position.z);
		
		data.put(packet.force);

		data.flip();
	}

	public void read(DataBuffer data)
	{
		authorID = data.getInt();
		position = new Vec3(data.getFloat(), data.getFloat(), data.getFloat());
		force = data.getFloat();
	}

	public void process(NetworkableServer server, InetAddress address, int port)
	{
		server.getCore().getGame().getWorld().applyDamageForce(position, force, false);
		server.tcpSendToAll(new DamageForcePacket(this));

		for (int id : server.getCore().getGame().getEntityManager().getPlayerEntites())
		{
			Entity e = server.getCore().getGame().getEntityManager().getEntities().get(id);

			if(e instanceof ServerPlayer)
			{
				ServerPlayer p = (ServerPlayer) e;

				float len = this.position.copy().sub(p.getPosition()).magnitude();
				if(len <= 10.0f)
					p.applyDamage((int)((10 - len) * 10), server, authorID);
			}
		}
	}

	public void process(NetworkableClient client, InetAddress address, int port)
	{
		client.getCore().getGame().getWorld().applyDamageForce(position, force, true);

		ClientPlayer p = client.getCore().getGame().getPlayer();

		float len = this.position.copy().sub(p.getPosition()).magnitude();

		if(len <= 10.0f)
		{
			Vec3 vel = p.getPosition().copy().sub(this.position);
			vel = vel.copy().normalize().mul(10).sub(vel).div(10);
			vel.y = 0.1f;

			p.getRigidBody().getBody().applyForce(vel, 1);
		}

//		client.getCore().getGame().getWorld().updateRequest((int) position.x, (int) position.y, (int) position.z, (int) force);
	}
}