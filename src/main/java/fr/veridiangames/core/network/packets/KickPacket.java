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
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.utils.DataBuffer;

import fr.veridiangames.core.game.entities.components.ECName;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;

import java.net.InetAddress;

/**
 * Created by trexr on 03/07/2016.
 */
public class KickPacket extends Packet
{

    private int id;

    public KickPacket()

    {
        super(KICK);
    }

    public KickPacket(int id)
    {
        super(KICK);
        data.put(id);
        data.flip();
    }

    public KickPacket(KickPacket packet)
    {
        super(KICK);
        data.put(packet.id);
        data.flip();
    }

    public void read(DataBuffer buffer)
    {
        this.id = buffer.getInt();
    }

    public void process(NetworkableServer server, InetAddress address, int port)
    {
    }

    public void process(NetworkableClient client, InetAddress address, int port)
    {
        if (client.getCore().getGame().getPlayer().getID() == id)
        {
            ClientPlayer player = client.getCore().getGame().getPlayer();
            player.setKicked(true);
            client.log("You were kicked !");
        }
        else
        {
            String name = ((ECName) client.getCore().getGame().getEntityManager().get(id).get(EComponent.NAME)).getName();
            client.getCore().getGame().remove(id);
            client.log(name + " was kicked...");
            client.console(name + " was kicked...");
        }
    }
}
