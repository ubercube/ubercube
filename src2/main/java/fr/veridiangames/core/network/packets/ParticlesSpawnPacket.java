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

import fr.veridiangames.core.game.entities.particles.ParticleSystem;
import fr.veridiangames.core.game.entities.particles.ParticlesManager;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.core.utils.DataBuffer;

/**
 * Created by Tybau on 10/06/2016.
 */
public class ParticlesSpawnPacket extends Packet
{
    private int     id;
    private String  particleName;
    private Vec3    position;
    private Vec3    velocity;
    private int     color;

    public ParticlesSpawnPacket()
    {
        super(PARTICLES_SPAWN);
    }

    public ParticlesSpawnPacket(ParticleSystem system)
    {
        super(PARTICLES_SPAWN);
        this.data.put(system.getID());
        this.data.put(system.getName());

        this.data.put(system.getTransform().getPosition().x);
        this.data.put(system.getTransform().getPosition().y);
        this.data.put(system.getTransform().getPosition().z);

        this.data.put(system.getParticleVelocity().x);
        this.data.put(system.getParticleVelocity().y);
        this.data.put(system.getParticleVelocity().z);

        this.data.put(system.getParticleColor().getARGB());

        this.data.flip();
    }

    public ParticlesSpawnPacket(ParticlesSpawnPacket packet)
    {
        super(PARTICLES_SPAWN);
        this.data.put(packet.id);
        this.data.put(packet.particleName);

        this.data.put(packet.position.x);
        this.data.put(packet.position.y);
        this.data.put(packet.position.z);

        this.data.put(packet.velocity.x);
        this.data.put(packet.velocity.y);
        this.data.put(packet.velocity.z);

        this.data.put(packet.color);

        this.data.flip();
    }

    @Override
	public void read(DataBuffer buffer)
    {
        this.id = buffer.getInt();
        this.particleName = buffer.getString();
        this.position = new Vec3(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
        this.velocity = new Vec3(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
        this.color = buffer.getInt();
    }

    @Override
	public void process(NetworkableServer server, InetAddress address, int port)
    {
        server.udpSendToAll(new ParticlesSpawnPacket(this));
    }

    @Override
	public void process(NetworkableClient client, InetAddress address, int port)
    {
        ParticleSystem ps = ParticlesManager.getParticleSystem(this.particleName);
    	if (ps == null)
    		return;
        client.getCore().getGame().spawn(new ParticleSystem(this.id,
                ps)
                .setPosition(this.position)
                .setParticleVelocity(this.velocity)
                .setParticleColor(new Color4f(this.color)));
    }
}
