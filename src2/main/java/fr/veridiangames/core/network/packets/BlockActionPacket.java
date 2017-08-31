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
		this.data.put(client);
		this.data.put(action);
		this.data.put(x);
		this.data.put(y);
		this.data.put(z);
		this.data.put(block);

		this.data.flip();
	}

	@Override
	public void read(DataBuffer data)
	{
		this.clientID = data.getInt();
		this.action = data.getInt();
		this.x = data.getInt();
		this.y = data.getInt();
		this.z = data.getInt();
		this.block = data.getInt();
	}

	@Override
	public void process(NetworkableServer server, InetAddress address, int port)
	{
		if (this.action == 0)
		{
			server.getCore().getGame().getWorld().addModifiedBlock(this.x, this.y, this.z, 0);
			server.tcpSendToAll(new BlockActionPacket(this.clientID, this.action, this.x, this.y, this.z, this.block));
		}
		else if (this.action == 1)
		{
			server.getCore().getGame().getWorld().addModifiedBlock(this.x, this.y, this.z, this.block);
			server.tcpSendToAll(new BlockActionPacket(this.clientID, this.action, this.x, this.y, this.z, this.block));
		}
	}

	@Override
	public void process(NetworkableClient client, InetAddress address, int port)
	{
		if (this.action == 0)
		{
			client.getCore().getGame().getWorld().removeBlock(this.x, this.y, this.z);
			client.getCore().getGame().getWorld().updateRequest(this.x, this.y, this.z);
			client.getCore().getGame().getWorld().addModifiedBlock(this.x, this.y, this.z, 0);
		}
		else if (this.action == 1)
		{
			client.getCore().getGame().getWorld().addBlock(this.x, this.y, this.z, this.block);
			client.getCore().getGame().getWorld().updateRequest(this.x, this.y, this.z);
			client.getCore().getGame().getWorld().addModifiedBlock(this.x, this.y, this.z, this.block);
		}
	}
}