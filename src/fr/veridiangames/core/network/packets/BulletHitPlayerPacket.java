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
import java.util.List;
import java.util.Map;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.bullets.StaticBullet;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.game.entities.player.ServerPlayer;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;

/**
 * Created by Marccspro on 26 f�vr. 2016.
 */
public class BulletHitPlayerPacket extends Packet
{
    private int playerId;
    private int life;

    public BulletHitPlayerPacket()

    {
        super(BULLET_HIT_PLAYER);
    }

    public BulletHitPlayerPacket(Player player)
    {
        super(BULLET_HIT_PLAYER);
        data.put(player.getID());
        data.put(0);

        data.flip();
    }

    public BulletHitPlayerPacket(BulletHitPlayerPacket packet, int life)
    {
        super(BULLET_HIT_PLAYER);
        data.put(packet.playerId);
        data.put(life);

        data.flip();
    }

    public void read(DataBuffer data)
    {
        playerId = data.getInt();
        life = data.getInt();
    }

    public void process(NetworkableServer server, InetAddress address, int port)
    {
        ServerPlayer p = (ServerPlayer) server.getCore().getGame().getEntityManager().getEntities().get(playerId);
        p.setLife(p.getLife() - 20);    // TODO : Modify damage

        if(p.getLife() <= 0)
        {
            server.sendToAll(new DeathPacket(playerId));
            ((ServerPlayer) server.getCore().getGame().getEntityManager().getEntities().get(playerId)).setDead(true);
        }
        else
            server.send(new BulletHitPlayerPacket(this, p.getLife()), p.getNetwork().getAddress(), p.getNetwork().getPort());
    }

    public void process(NetworkableClient client, InetAddress address, int port)
    {
        System.out.println("Life : " + life);
        client.getCore().getGame().getPlayer().setDisplayLife(life);
    }
}