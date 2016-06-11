/*
 * Copyright (C) 2016 Team Ubercube
 *
 *   This file is part of Ubercube.
 *
 *       Ubercube is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Ubercube is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Ubercube.  If not, see http://www.gnu.org/licenses/.
 */

package fr.veridiangames.core.network.packets;

import java.net.InetAddress;

import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.components.ECName;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;

/**
 * Created by Marccspro on 26 f�vr. 2016.
 */
public class BlockActionPacket extends Packet
{
	int clientID;
	private int action;
	private int x, y, z;
	private int block;
	
	public BlockActionPacket()
	{
		super(BLOCK_ACTION);
	}
	
	public BlockActionPacket(int client, int action, int x, int y, int z, int block)
	{
		super(BLOCK_ACTION);
		data.put(client);
		data.put(action);
		data.put(x);
		data.put(y);
		data.put(z);
		data.put(block);
		
		data.flip();
	}

	public void read(DataBuffer data)
	{
		clientID = data.getInt();
		action = data.getInt();
		x = data.getInt();
		y = data.getInt();
		z = data.getInt();
		block = data.getInt();
	}

	public void process(NetworkableServer server, InetAddress address, int port)
	{
		if (action == 0)
		{
			server.getCore().getGame().getWorld().addModifiedBlock(x, y, z, 0);
			server.sendToAll(new BlockActionPacket(clientID, action, x, y, z, block));
		}
		else if (action == 1)
		{
			server.getCore().getGame().getWorld().addModifiedBlock(x, y, z, block);
			server.sendToAll(new BlockActionPacket(clientID, action, x, y, z, block));
		}
	}

	public void process(NetworkableClient client, InetAddress address, int port)
	{
		if (action == 0)
		{
			client.getCore().getGame().getWorld().removeBlock(x, y, z);
			client.getCore().getGame().getWorld().updateRequest(x, y, z);
			client.getCore().getGame().getWorld().addModifiedBlock(x, y, z, 0);
		}
		else if (action == 1)
		{
			client.getCore().getGame().getWorld().addBlock(x, y, z, block);
			client.getCore().getGame().getWorld().updateRequest(x, y, z);
			client.getCore().getGame().getWorld().addModifiedBlock(x, y, z, block);
		}
	}
}