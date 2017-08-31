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

import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.game.entities.player.ServerPlayer;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;

/**
 * Created by Marccspro on 26 f�vr. 2016.
 */
public class EntityMovementPacket extends Packet
{
	private int id;
	private Vec3 position;
	private Quat rotation;

	public EntityMovementPacket()
	{
		super(ENTITY_MOVEMENT);
	}

	public EntityMovementPacket(Player player)
	{
		super(ENTITY_MOVEMENT);
		this.data.put(player.getID());

		this.data.put(player.getPosition().x);
		this.data.put(player.getPosition().y);
		this.data.put(player.getPosition().z);

		this.data.put(player.getRotation().x);
		this.data.put(player.getRotation().y);
		this.data.put(player.getRotation().z);
		this.data.put(player.getRotation().w);

		this.data.flip();
	}

	public EntityMovementPacket(EntityMovementPacket packet)
	{
		super(ENTITY_MOVEMENT);
		this.data.put(packet.id);

		this.data.put(packet.position.x);
		this.data.put(packet.position.y);
		this.data.put(packet.position.z);

		this.data.put(packet.rotation.x);
		this.data.put(packet.rotation.y);
		this.data.put(packet.rotation.z);
		this.data.put(packet.rotation.w);

		this.data.flip();
	}

	@Override
	public void read(DataBuffer data)
	{
		this.id = data.getInt();
		this.position = new Vec3(data.getFloat(), data.getFloat(), data.getFloat());
		this.rotation = new Quat(data.getFloat(), data.getFloat(), data.getFloat(), data.getFloat());
	}

	@Override
	public void process(NetworkableServer server, InetAddress address, int port)
	{
		ServerPlayer player = (ServerPlayer) server.getCore().getGame().getEntityManager().getEntities().get(this.id);
		if (player == null)
			return;

		Vec3 vel = player.getPosition().copy().sub(this.position);
		player.setOnlineVelocity(vel);

		//TODO: Prevent teleporation and speed hack (with vel)

		/* Falling system */
//		if(vel.y > 0.0 && !player.isFalling())
//		{
//			player.setFalling(true);
//			player.setHigh(position.y);
//		}
//
//		if(player.isFalling() && vel.y == 0.0)
//		{
//			float fallHight = player.getHigh() - position.y;
//			player.setFalling(false);
//			player.setHigh(0.0f);
//
//			if(fallHight > 5)
//			{
//				int damage = ((int)fallHight - 5) * 5;
//				player.applyDamage(damage, server);
//			}
//		}

		player.setPosition(this.position);
		player.setRotation(this.rotation);

		if (this.position.y < 0 && !player.isDead())
			player.kill(server);

		server.udpSendToAll(new EntityMovementPacket(this));
	}

	@Override
	public void process(NetworkableClient client, InetAddress address, int port)
	{
		if (client.getCore().getGame().getPlayer().getID() == this.id)
			return;

		Player player = (Player) client.getCore().getGame().getEntityManager().getEntities().get(this.id);
		if (player == null)
			return;
		player.setPositionSmoothly(this.position);
		player.setRotation(this.rotation);
	}
}
