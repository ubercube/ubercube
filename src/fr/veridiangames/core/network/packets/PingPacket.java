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

import fr.veridiangames.core.game.entities.player.ServerPlayer;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;

import java.net.InetAddress;

/**
 * Created by Marc on 19/06/2016.
 */
public class PingPacket extends Packet
{
    private int userID;
    private long pingTime;
    private long ping;

    public PingPacket()
    {
        super(PING);
    }

    public PingPacket(int userID, long pingTime, long ping)
    {
        super(PING);
        data.put(userID);
        data.put(pingTime);
        data.put(ping);
        data.flip();
    }

    public PingPacket(PingPacket packet)
    {
        super(PING);
        data.put(packet.userID);
        data.put(packet.pingTime);
        data.put(packet.ping);
        data.flip();
    }

    public void read(DataBuffer buffer)
    {
        userID = buffer.getInt();
        pingTime = buffer.getLong();
        ping = buffer.getLong();
    }

    public void process(NetworkableServer server, InetAddress address, int port)
    {
        ServerPlayer player = (ServerPlayer) server.getCore().getGame().getEntityManager().getEntities().get(userID);
        player.setPing((long)((pingTime - player.getPingTime()) / 1000.0f));
        player.setPingTime(pingTime);
        player.setTimeOutTests(0);
        player.setPinged(true);
    }

    public void process(NetworkableClient client, InetAddress address, int port)
    {
        client.send(new PingPacket(client.getCore().getGame().getPlayer().getID(), System.currentTimeMillis(), 0));
        //client.log("Ping: " + ping + "ms");
    }
}
