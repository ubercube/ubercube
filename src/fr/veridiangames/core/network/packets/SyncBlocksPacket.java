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

import fr.veridiangames.core.maths.Vec4i;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;

/**
 * Created by Marccspro on 26 f�vr. 2016.
 */
public class SyncBlocksPacket extends Packet
{
	private List<Vec4i> blocks;
	
	public SyncBlocksPacket()
	{
		super(BLOCK_SYNC);
	}
	
	public SyncBlocksPacket(List<Vec4i> blocks)
	{
		super(BLOCK_SYNC);
		data.put(blocks.size());
		for (Vec4i b : blocks)
		{
			data.put(b.x);
			data.put(b.y);
			data.put(b.z);
			data.put(b.w);

            System.out.println("BLOCK: " + Integer.toHexString(b.w));
		}
		data.flip();
	}
	
	public SyncBlocksPacket(SyncBlocksPacket packet)
	{
		super(BLOCK_SYNC);
		data.put(blocks.size());
		for (Vec4i b : blocks)
		{
			data.put(b.x);
			data.put(b.y);
			data.put(b.z);
			data.put(b.w);
		}
		data.flip();
	}

	public void read(DataBuffer data)
	{
		blocks = new ArrayList<Vec4i>();
		int size = data.getInt();
		for (int i = 0; i < size; i++)
		{
			Vec4i block = new Vec4i(data.getInt(), data.getInt(), data.getInt(), data.getInt());
			System.out.println("BLOCK: " + Integer.toHexString(block.w));
			blocks.add(block);
		}
	}

	public void process(NetworkableServer server, InetAddress address, int port)
	{
		
	}

	public void process(NetworkableClient client, InetAddress address, int port)
	{
		for (Vec4i block : blocks)
		{
			if (block.w == 0)
			{
				client.getCore().getGame().getWorld().removeBlock(block.x, block.y, block.z);
				client.getCore().getGame().getWorld().updateRequest(block.x, block.y, block.z);
				client.getCore().getGame().getWorld().addModifiedBlock(block.x, block.y, block.z, block.w);
			}
			else
			{
				client.getCore().getGame().getWorld().addBlock(block.x, block.y, block.z, block.w);
				client.getCore().getGame().getWorld().updateRequest(block.x, block.y, block.z);
				client.getCore().getGame().getWorld().addModifiedBlock(block.x, block.y, block.z, block.w);
			}
		}
	}
}