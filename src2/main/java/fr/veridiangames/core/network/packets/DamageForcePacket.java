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
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.game.entities.player.ServerPlayer;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;

/**
 * Created by Marccspro on 26 f�vr. 2016.
 */
public class DamageForcePacket extends Packet
{
	private Vec3 position;
	private float force;

	public DamageForcePacket()

	{
		super(DAMAGE_FORCE);
	}

	public DamageForcePacket(Vec3 pos, float force)
	{
		super(DAMAGE_FORCE);

		this.data.put(pos.x);
		this.data.put(pos.y);
		this.data.put(pos.z);

		this.data.put(force);

		this.data.flip();
	}

	public DamageForcePacket(DamageForcePacket packet)
	{
		super(DAMAGE_FORCE);

		this.data.put(packet.position.x);
		this.data.put(packet.position.y);
		this.data.put(packet.position.z);

		this.data.put(packet.force);

		this.data.flip();
	}

	@Override
	public void read(DataBuffer data)
	{
		this.position = new Vec3(data.getFloat(), data.getFloat(), data.getFloat());
		this.force = data.getFloat();
	}

	@Override
	public void process(NetworkableServer server, InetAddress address, int port)
	{
		server.getCore().getGame().getWorld().applyDamageForce(this.position, this.force, false);
		server.tcpSendToAll(new DamageForcePacket(this));

		for (int id : server.getCore().getGame().getEntityManager().getPlayerEntites())
		{
			Entity e = server.getCore().getGame().getEntityManager().getEntities().get(id);

			if(e instanceof ServerPlayer)
			{
				ServerPlayer p = (ServerPlayer) e;

				float len = this.position.copy().sub(p.getPosition()).magnitude();
				if(len <= 10.0f)
					p.applyDamage((int)((10 - len) * 10), server);
			}
		}
	}

	@Override
	public void process(NetworkableClient client, InetAddress address, int port)
	{
		client.getCore().getGame().getWorld().applyDamageForce(this.position, this.force, true);

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