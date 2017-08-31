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

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.components.ECName;
import fr.veridiangames.core.game.entities.components.ECNetwork;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;

/**
 * Created by Marccspro on 26 f�vr. 2016.
 */
public class DisconnectPacket extends Packet
{
	private int id;
	private String reason;

	public DisconnectPacket() {
		super(DISCONNECT);
	}

	public DisconnectPacket(int id, String reason)
	{
		super(DISCONNECT);
		this.data.put(id);
		this.data.put(reason);
		this.data.flip();
	}

	public DisconnectPacket(DisconnectPacket packet)
	{
		super(DISCONNECT);
		this.data.put(packet.id);
		this.data.put(packet.reason);
		this.data.flip();
	}

	@Override
	public void read(DataBuffer data)
	{
		this.id = data.getInt();
		this.reason = data.getString();
	}

	@Override
	public void process(NetworkableServer server, InetAddress address, int port)
	{
		if (!server.getCore().getGame().getEntityManager().getEntities().containsKey(this.id))
			return;
		String name = ((ECName) server.getCore().getGame().getEntityManager().get(this.id).get(EComponent.NAME)).getName();
		ECNetwork net = ((ECNetwork) server.getCore().getGame().getEntityManager().get(this.id).get(EComponent.NETWORK));
		GameCore.getInstance().getGame().remove(this.id);
		server.tcpSendToAll(new DisconnectPacket(this));
		server.getTcp().disconnectClient(net.getAddress(), net.getPort());
		server.log(name + " disconnected... " + this.reason);
	}

	@Override
	public void process(NetworkableClient client, InetAddress address, int port)
	{
		if (!client.getCore().getGame().getEntityManager().getEntities().containsKey(this.id))
			return;
		String name = ((ECName) client.getCore().getGame().getEntityManager().get(this.id).get(EComponent.NAME)).getName();
		client.getCore().getGame().remove(this.id);
		client.log(name + " disconnected... " + this.reason);
		client.console(name + " disconnected... " + this.reason);
	}
}