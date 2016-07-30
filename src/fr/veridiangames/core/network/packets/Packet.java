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
 * Created by Marccspro on 25 f�vr. 2016.
 */
public abstract class Packet
{
	public static final int MAX_SIZE = 512;

	public static final int	DISCONNECT			= 0;
	public static final int	CONNECT				= 1;
	public static final int	ENTITY_SYNC			= 2;
	public static final int	ENTITY_MOVEMENT		= 3;
	public static final int	BLOCK_ACTION		= 4;
	public static final int	BLOCK_SYNC			= 5;
	public static final int	WEAPON_POS			= 6;
	public static final int BULLET_HIT_BLOCK	= 7;
	public static final int PARTICLES_SPAWN 	= 8;
	public static final int PARTICLES_REMOVE 	= 9;
	public static final int BULLET_HIT_PLAYER 	= 10;
	public static final int DEATH				= 11;
	public static final int RESPAWN				= 12;
	public static final int PING 				= 13;
	public static final int TIME_OUT			= 14;
	public static final int WEAPON_CHANGE		= 15;
	public static final int BULLET_SHOOT		= 16;
	public static final int KICK				= 17;
	public static final int DAMAGE_FORCE		= 18;
	public static final int TCHAT_MSG			= 19;
	public static final int AUDIO				= 20;
	public static final int GRENADE_SHOOT       = 21;

	protected DataBuffer data;

	public Packet(int packetID)
	{
		data = new DataBuffer(MAX_SIZE);
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