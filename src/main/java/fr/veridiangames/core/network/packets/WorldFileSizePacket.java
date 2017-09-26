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
public class WorldFileSizePacket extends Packet
{
	// This packet is supposed to be received only one time per game instance
	public static byte[] completeDatas;
	static int writtenDatas = 0;
    private int size;

    public WorldFileSizePacket()
    {
        super(WORLD_FILE_SIZE);
    }

    public WorldFileSizePacket(int size)
    {
        super(WORLD_FILE_SIZE);
        data.put(size);
        data.flip();
    }

    public WorldFileSizePacket(WorldFileSizePacket packet)
    {
        super(WORLD_FILE_SIZE);
        data.put(packet.size);
        data.flip();
    }

    public void read(DataBuffer buffer)
    {
        size = buffer.getInt();
    }

    /**
     * Should not be used
     */
    public void process(NetworkableServer server, InetAddress address, int port)
    {
    	
    }

    public void process(NetworkableClient client, InetAddress address, int port)
    {
    	completeDatas = new byte[size];
    }
}
