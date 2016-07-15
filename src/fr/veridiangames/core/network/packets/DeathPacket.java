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
import fr.veridiangames.core.game.entities.particles.ParticleSystem;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.core.utils.Indexer;

import java.net.InetAddress;

/**
 * Created by Tybau on 13/06/2016.
 */
public class DeathPacket extends Packet
{
    private int playerId;

    public DeathPacket()
    {
        super(DEATH);
    }

    public DeathPacket(int playerId)
    {
        super(DEATH);
        data.put(playerId);
        data.flip();
    }

    public void read(DataBuffer buffer)
    {
        this.playerId = buffer.getInt();
    }

    public void process(NetworkableServer server, InetAddress address, int port)
    {

    }

    public void process(NetworkableClient client, InetAddress address, int port)
    {
        client.console(this.playerId + " just died !");
        if(GameCore.getInstance().getGame().getPlayer().getID() != this.playerId)
            client.log(this.playerId + " just died !");
        else
        {
            client.log("You died !");
            GameCore.getInstance().getGame().getPlayer().setDead(true);
            GameCore.getInstance().getGame().getPlayer().setLife(Player.MAX_LIFE);
            client.tcpSend(new RespawnPacket(GameCore.getInstance().getGame().getPlayer().getID()));
        }

        if (GameCore.getInstance().getGame().getEntityManager().getEntities().get(playerId) == null)
            return;

            GameCore.getInstance().getGame().spawn(new ParticleSystem(Indexer.getUniqueID(), "Death", ((Player) GameCore.getInstance().getGame().getEntityManager().getEntities().get(playerId)).getPosition())
            .setParticleVelocity(new Vec3(0, 0.2f, 0))
            .setParticleVelocityRandomness(0.05f)
            .setParticleColor(new Color4f(0.7f, 0f, 0f))
            .setImpulsion(true)
            .setScaleInterval(0.05f, 0.2f)
            .setParticleCount(120)
            .setGravity(new Vec3(0, -0.01f, 0))
            .useCollision(true)
            .setParticleLifeTime(240)
            .setParticleLifeTimeRandomness(120));
    }
}
