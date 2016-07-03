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
import java.util.ArrayList;
import java.util.List;

import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.player.NetworkedPlayer;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.game.entities.player.ServerPlayer;
import fr.veridiangames.core.maths.Mathf;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.maths.Vec4i;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;

/**
 * Created by Marccspro on 26 f�vr. 2016.
 */
public class ConnectPacket extends Packet
{
	private int id;
	private String name;
	private Vec3 position;
	private Quat rotation;
	
	public ConnectPacket()
	
	{
		super(CONNECT);
	}
	
	public ConnectPacket(Player player)
	{
		super(CONNECT);
		data.put(player.getID());
		data.put(player.getName());
		
		data.put(player.getPosition().x);
		data.put(player.getPosition().y);
		data.put(player.getPosition().z);
		
		data.put(player.getRotation().x);
		data.put(player.getRotation().y);
		data.put(player.getRotation().z);
		data.put(player.getRotation().w);
		
		data.flip();
	}
	
	public ConnectPacket(ConnectPacket packet)
	{
		super(CONNECT);
		data.put(packet.id);
		data.put(packet.name);
		
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
		name = data.getString();
		position = new Vec3(data.getFloat(), data.getFloat(), data.getFloat());
		rotation = new Quat(data.getFloat(), data.getFloat(), data.getFloat(), data.getFloat());
	}

	public void process(NetworkableServer server, InetAddress address, int port)
	{
		server.getCore().getGame().spawn(new ServerPlayer(id, name, position, rotation, address.getHostName(), port));
		server.log(name + " just connected !");
		server.sendToAll(new ConnectPacket(this));
		for (int i = 0; i < server.getCore().getGame().getWorld().getModifiedBlocks().size(); i++)
		{
			System.out.println("COLOR: " + Integer.toHexString(server.getCore().getGame().getWorld().getModifiedBlocks().get(i).w));
		}

		/* SENDING MULTIPLE PACKETS TO AVOID READ OVERFLOW OF 2048 */
		int modifiedBlocksSize = server.getCore().getGame().getWorld().getModifiedBlocks().size();
		int packetCount = (int) Mathf.ceil((float) (modifiedBlocksSize * 16) / Packet.MAX_SIZE);
		List<Vec4i> currentData = server.getCore().getGame().getWorld().getModifiedBlocks();
		for (int i = 0; i < packetCount; i++)
		{
			List<Vec4i> dataToSend = new ArrayList<>();
			float count = (float)modifiedBlocksSize / packetCount;
			int icount = (int) count;

			for (int j = 0; j < count; j++)
			{
				int index = i * icount + j;
				dataToSend.add(currentData.get(index));
			}
			server.send(new SyncBlocksPacket(dataToSend), address, port);
		}

		for (int i = 0; i < server.getCore().getGame().getEntityManager().getNetworkableEntites().size(); i++)
		{
			int id = server.getCore().getGame().getEntityManager().getNetworkableEntites().get(i);
			if (id == this.id) 
				continue;
			Entity e = server.getCore().getGame().getEntityManager().getEntities().get(id);
			if (e instanceof Player)
				server.send(new EntitySyncPacket((Player) e), address, port);
		}
	}

	public void process(NetworkableClient client, InetAddress address, int port)
	{
		if (client.getCore().getGame().getPlayer().getID() != id)
		{
			client.getCore().getGame().spawn(new NetworkedPlayer(id, name, position, rotation, address.getHostName(), port));
			client.log(name + " just connected !");
		}
		else
		{
			client.log("You just connected as " + name);
			client.setConnected(true);
		}
	}
}