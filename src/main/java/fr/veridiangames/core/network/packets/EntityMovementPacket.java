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
import fr.veridiangames.core.game.entities.player.Player;
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
		data.put(player.getID());
		
		data.put(player.getPosition().x);
		data.put(player.getPosition().y);
		data.put(player.getPosition().z);
		
		data.put(player.getRotation().x);
		data.put(player.getRotation().y);
		data.put(player.getRotation().z);
		data.put(player.getRotation().w);
		
		data.flip();
	}
	
	public EntityMovementPacket(EntityMovementPacket packet)
	{
		super(ENTITY_MOVEMENT);
		data.put(packet.id);
		
		data.put(packet.position.x);
		data.put(packet.position.y);
		data.put(packet.position.z);
		
		data.put(packet.rotation.x);
		data.put(packet.rotation.y);
		data.put(packet.rotation.z);
		data.put(packet.rotation.w);
		
		data.flip();
	}

	public void read(DataBuffer data)
	{
		id = data.getInt();
		position = new Vec3(data.getFloat(), data.getFloat(), data.getFloat());
		rotation = new Quat(data.getFloat(), data.getFloat(), data.getFloat(), data.getFloat());
	}

	public void process(NetworkableServer server, InetAddress address, int port)
	{
		Player player = (Player) server.getCore().getGame().getEntityManager().getEntities().get(id);
		if (player == null) 
			return;

		//TODO: Prevent teleporation and speed hack
		player.setPosition(position);
		player.setRotation(rotation);

		if (position.y < 0)
			server.tcpSendToAll(new DeathPacket(id));

		server.udpSendToAll(new EntityMovementPacket(this));
	}

	public void process(NetworkableClient client, InetAddress address, int port)
	{
		if (client.getCore().getGame().getPlayer().getID() == id)
			return;
		
		Player player = (Player) client.getCore().getGame().getEntityManager().getEntities().get(id);
		if (player == null) 
			return;
		player.setPositionSmoothly(position);
		player.setRotation(rotation);
	}
}
