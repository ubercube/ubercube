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
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.components.ECName;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.game.entities.particles.ParticleSystem;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.network.Protocol;
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
	private int shooterId;
	private byte headshot;

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

	public DeathPacket(int playerId, int shooterId, byte headshot)
	{
		super(DEATH);
		data.put(playerId);
		data.put(shooterId);
		data.put(headshot);
		data.flip();
	}

    public void read(DataBuffer buffer)
    {
        this.playerId = buffer.getInt();
        this.shooterId = buffer.getInt();
        headshot = buffer.getByte();
    }

    public void process(NetworkableServer server, InetAddress address, int port)
    {

    }

    public void process(NetworkableClient client, InetAddress address, int port)
    {
        Entity e = client.getCore().getGame().getEntityManager().get(this.playerId);
		String shooterName;
        if(!GameCore.getInstance().getGame().getEntityManager().getEntities().containsKey(shooterId)) {
			shooterName = "Server";
		}else {
			shooterName = ((Player) GameCore.getInstance().getGame().getEntityManager().getEntities().get(shooterId)).getName();
		}


        if (e != null || !(e instanceof Player))
		{
			if (playerId == shooterId)
				client.console(((ECName)e.get(EComponent.NAME)).getName() + " killed himself... ");
			else
				if(headshot != 0)
					client.console(((ECName)e.get(EComponent.NAME)).getName() + " has been killed by " + shooterName + " !");
				else
					client.console(((ECName)e.get(EComponent.NAME)).getName() + " has been headshooted by " + shooterName + " !");

		}
        else
            return;
		Player p = (Player) e;
        if(client.getCore().getGame().getPlayer().getID() != this.playerId)
        {
			if (headshot != 0) {
				client.log(((ECName) e.get(EComponent.NAME)).getName() + " has been killed by " + shooterName + " !");
			} else {
				client.log(((ECName) e.get(EComponent.NAME)).getName() + " has been shooted by " + shooterName + " !");
			}
		}
        else
        {
			if (headshot != 0) {
				client.log("Headshooted by " + shooterName + " !");
			} else {
				client.log("You died by " + shooterName + " !");
			}
            client.getCore().getGame().getPlayer().setDead(true);
            //client.send(new RespawnPacket(GameCore.getInstance().getGame().getPlayer().getID()), Protocol.TCP);
        }
        if (client.getCore().getGame().getEntityManager().getEntities().get(playerId) == null)
            return;

        client.getCore().getGame().spawn(new ParticleSystem(Indexer.getUniqueID(), "Death", ((Player) client.getCore().getGame().getEntityManager().getEntities().get(playerId)).getPosition())
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

        if(playerId != client.getID())
            p.setDead(true);
    }
}
