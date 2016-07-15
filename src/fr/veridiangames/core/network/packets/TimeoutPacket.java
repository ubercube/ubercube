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

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.components.ECName;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;

import java.net.InetAddress;

/**
 * Created by Marccspro on 26 f�vr. 2016.
 */
public class TimeoutPacket extends Packet
{
	private int id;

	public TimeoutPacket()
	{
		super(TIME_OUT);
	}

	public TimeoutPacket(int id)
	{
		super(TIME_OUT);
		data.put(id);
		data.flip();
	}

	public TimeoutPacket(TimeoutPacket packet)
	{
		super(TIME_OUT);
		data.put(packet.id);
		data.flip();
	}

	public void read(DataBuffer data)
	{
		id = data.getInt();
	}

	public void process(NetworkableServer server, InetAddress address, int port)
	{
	}

	public void process(NetworkableClient client, InetAddress address, int port)
	{
		if (!GameCore.getInstance().getGame().getEntityManager().getEntities().containsKey(id))
			return;

		String name = ((ECName) GameCore.getInstance().getGame().getEntityManager().get(id).get(EComponent.NAME)).getName();
		GameCore.getInstance().getGame().remove(id);
		client.log(name + " timed out...");
		client.console(name + " timed out...");
	}
}