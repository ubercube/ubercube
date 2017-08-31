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

import fr.veridiangames.core.game.entities.player.NetworkedPlayer;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;

/**
 * Created by Marccspro on 26 f�vr. 2016.
 */
public class EntitySyncPacket extends Packet
{
	private int id;
	private String name;
	private Vec3 position;
	private Quat rotation;

	public EntitySyncPacket()
	{
		super(ENTITY_SYNC);
	}

	public EntitySyncPacket(Player player)
	{
		super(ENTITY_SYNC);
		this.data.put(player.getID());
		this.data.put(player.getName());

		this.data.put(player.getPosition().x);
		this.data.put(player.getPosition().y);
		this.data.put(player.getPosition().z);

		this.data.put(player.getRotation().x);
		this.data.put(player.getRotation().y);
		this.data.put(player.getRotation().z);
		this.data.put(player.getRotation().w);

		this.data.flip();
	}

	public EntitySyncPacket(EntitySyncPacket packet)
	{
		super(ENTITY_SYNC);
		this.data.put(packet.id);
		this.data.put(packet.name);

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
		this.name = data.getString();
		this.position = new Vec3(data.getFloat(), data.getFloat(), data.getFloat());
		this.rotation = new Quat(data.getFloat(), data.getFloat(), data.getFloat(), data.getFloat());
	}

	@Override
	public void process(NetworkableServer server, InetAddress address, int port)
	{
	}

	@Override
	public void process(NetworkableClient client, InetAddress address, int port)
	{
		if (client.getCore().getGame().getPlayer().getID() != this.id)
			client.getCore().getGame().spawn(new NetworkedPlayer(this.id, this.name, this.position, this.rotation, address.getHostName(), port));
	}
}