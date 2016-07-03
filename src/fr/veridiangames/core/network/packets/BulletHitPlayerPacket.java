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
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.game.entities.player.ServerPlayer;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;

import static sun.audio.AudioPlayer.player;

/**
 * Created by Marccspro on 26 f�vr. 2016.
 */
public class BulletHitPlayerPacket extends Packet
{
    private int playerId;
    private int life;
    private boolean hitable;

    public BulletHitPlayerPacket()

    {
        super(BULLET_HIT_PLAYER);
    }

    public BulletHitPlayerPacket(Player player)
    {
        super(BULLET_HIT_PLAYER);
        data.put(player.getID());
        data.put(0);
        data.put(player.isHitable() ? 1 : 0);

        data.flip();
    }

    public BulletHitPlayerPacket(BulletHitPlayerPacket packet, boolean hitable, int life)
    {
        super(BULLET_HIT_PLAYER);
        data.put(packet.playerId);
        data.put(life);
        data.put(hitable ? 1 : 0);

        data.flip();
    }

    public void read(DataBuffer data)
    {
        playerId = data.getInt();
        life = data.getInt();
        hitable = data.getInt() == 0 ? false : true;
    }

    public void process(NetworkableServer server, InetAddress address, int port)
    {
        ServerPlayer p = (ServerPlayer) server.getCore().getGame().getEntityManager().getEntities().get(playerId);
        p.setHitable(hitable);
        System.out.println(hitable);
        if (hitable)
        {
            p.setLife(p.getLife() - 20);    // TODO : Modify damage
            if(p.getLife() <= 0)
            {
                server.sendToAll(new DeathPacket(playerId));
                ((ServerPlayer) server.getCore().getGame().getEntityManager().getEntities().get(playerId)).setDead(true);
            }
            else
                server.send(new BulletHitPlayerPacket(this, p.isHitable(), p.getLife()), p.getNetwork().getAddress(), p.getNetwork().getPort());
        }
    }

    public void process(NetworkableClient client, InetAddress address, int port)
    {
        client.getCore().getGame().getPlayer().setLife(life);
    }
}