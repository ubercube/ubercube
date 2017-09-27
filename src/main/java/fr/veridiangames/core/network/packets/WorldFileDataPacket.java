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
 * Created by Marc on 19/06/2016.
 */
public class WorldFileDataPacket extends Packet
{
	private byte[] worldDatas;

    public WorldFileDataPacket()
    {
        super(WORLD_FILE_DATA);
    }

    public WorldFileDataPacket(byte[] wD)
    {
        super(WORLD_FILE_DATA);
        data.put(wD.length);
        data.put(wD);
        data.flip();
    }

    public WorldFileDataPacket(WorldFileDataPacket packet)
    {
        super(WORLD_FILE_DATA);
        data.put(packet.worldDatas.length);
        data.put(packet.worldDatas);
        data.flip();
    }

    public void read(DataBuffer buffer)
    {
    	worldDatas = new byte[buffer.getInt()];
    	for (int i=0;i<worldDatas.length;i++)
    		worldDatas[i] = buffer.getByte();
    }

    /**
     * Should not be used
     */
    public void process(NetworkableServer server, InetAddress address, int port)
    {
    	
    }

    public void process(NetworkableClient client, InetAddress address, int port)
    {
    	System.arraycopy(worldDatas, 0, WorldFileSizePacket.completeDatas, WorldFileSizePacket.writtenDatas, worldDatas.length);
    	WorldFileSizePacket.writtenDatas += worldDatas.length;
    	if (WorldFileSizePacket.writtenDatas >= WorldFileSizePacket.completeDatas.length)
    		client.getCore().getGame().getWorld().readServerWorld();
    }
}
