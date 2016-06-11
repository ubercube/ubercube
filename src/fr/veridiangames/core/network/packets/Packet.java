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

import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;

/**
 * Created by Marccspro on 25 f�vr. 2016.
 */
public abstract class Packet
{
	public static final int	DISCONNECT			= 0x00;
	public static final int	CONNECT				= 0x01;
	public static final int	ENTITY_SYNC			= 0x02;
	public static final int	ENTITY_MOVEMENT		= 0x03;
	public static final int	BLOCK_ACTION		= 0x04;
	public static final int	BLOCK_SYNC			= 0x05;
	public static final int	WEAPON_POS			= 0x06;
	public static final int	BULLET_HIT			= 0x07;
	public static final int PARTICLES_SPAWN 	= 0x08;
	public static final int PARTICLES_REMOVE 	= 0x09;

	protected DataBuffer data;

	public Packet(int packetID)
	{
		data = new DataBuffer();
		data.put(packetID);
	}

	public abstract void read(DataBuffer buffer);
	public abstract void process(NetworkableServer server, InetAddress address, int port);
	public abstract void process(NetworkableClient client, InetAddress address, int port);

	public DataBuffer getData()
	{
		return data;
	}
}