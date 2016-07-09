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

import fr.veridiangames.core.game.entities.particles.ParticleSystem;
import fr.veridiangames.core.game.entities.particles.ParticlesManager;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.core.utils.DataBuffer;

import java.net.InetAddress;

/**
 * Created by Tybau on 10/06/2016.
 */
public class ParticlesSpawnPacket extends Packet
{
    private int     id;
    private String  particleName;
    private int     playerId;
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
        data.put(system.getID());
        data.put(system.getName());

        data.put(system.getTransform().getPosition().x);
        data.put(system.getTransform().getPosition().y);
        data.put(system.getTransform().getPosition().z);

        data.put(system.getParticleVelocity().x);
        data.put(system.getParticleVelocity().y);
        data.put(system.getParticleVelocity().z);

        data.put(system.getParticleColor().getARGB());

        data.flip();
    }

    public ParticlesSpawnPacket(ParticlesSpawnPacket packet)
    {
        super(PARTICLES_SPAWN);
        data.put(packet.id);
        data.put(packet.particleName);

        data.put(packet.position.x);
        data.put(packet.position.y);
        data.put(packet.position.z);

        data.put(packet.velocity.x);
        data.put(packet.velocity.y);
        data.put(packet.velocity.z);

        data.put(packet.color);

        data.flip();
    }

    public void read(DataBuffer buffer)
    {
        id = buffer.getInt();
        particleName = buffer.getString();
        position = new Vec3(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
        velocity = new Vec3(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
        color = buffer.getInt();
    }

    public void process(NetworkableServer server, InetAddress address, int port)
    {
        server.tcpSendToAll(new ParticlesSpawnPacket(this));
    }

    public void process(NetworkableClient client, InetAddress address, int port)
    {
        client.getCore().getGame().spawn(new ParticleSystem(id,
                ParticlesManager.getParticleSystem(particleName))
                .setPosition(position)
                .setParticleVelocity(velocity)
                .setParticleColor(new Color4f(color)));
    }
}
