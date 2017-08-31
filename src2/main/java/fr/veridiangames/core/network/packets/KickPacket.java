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
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;

/**
 * Created by trexr on 03/07/2016.
 */
public class KickPacket extends Packet
{

    private int id;
    private String msg;

    public KickPacket()

    {
        super(KICK);
    }

    public KickPacket(int id, String msg)
    {
        super(KICK);
        this.data.put(id);
        this.data.put(msg);
        this.data.flip();
    }

    public KickPacket(KickPacket packet)
    {
        super(KICK);
        this.data.put(packet.id);
        this.data.put(packet.msg);
        this.data.flip();
    }

    @Override
	public void read(DataBuffer buffer)
    {
		this.id = buffer.getInt();
    	this.msg = buffer.getString();
    }

    @Override
	public void process(NetworkableServer server, InetAddress address, int port)
    {
		if (!server.getCore().getGame().getEntityManager().getEntities().containsKey(this.id))
			return;
		String name = ((ECName) server.getCore().getGame().getEntityManager().get(this.id).get(EComponent.NAME)).getName();
		server.getCore().getGame().getEntityManager().get(this.id).get(EComponent.NETWORK);
		GameCore.getInstance().getGame().remove(this.id);
		server.tcpSendToAll(new KickPacket(this));
		server.log(name + " was kicked... ");
    }

    @Override
	public void process(NetworkableClient client, InetAddress address, int port)
    {
        if (client.getCore().getGame().getPlayer().getID() == this.id)
        {
            ClientPlayer player = client.getCore().getGame().getPlayer();
            player.setKicked(true);
            player.setKickMessage(this.msg);
            client.log("You were kicked: " + this.msg);
        }
        else
        {
        	String name = ((ECName) client.getCore().getGame().getEntityManager().get(this.id).get(EComponent.NAME)).getName();
            client.getCore().getGame().remove(this.id);
            client.log(name + " was kicked...");
            client.console(name + " was kicked...");
        }
    }
}
