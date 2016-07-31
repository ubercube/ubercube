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

import fr.veridiangames.core.game.entities.bullets.Bullet;
import fr.veridiangames.core.game.entities.grenades.Grenade;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.core.utils.Indexer;

import java.net.InetAddress;

/**
 * Created by Aiko on 26/07/2016.
 */
public class GrenadeShootPacket extends Packet {
    private int clientID;
    private int id;
    private String name;
    private Vec3 position;
    private Quat rotation;
    private float shootForce;
    private Bullet.BulletType bulletType;

    public GrenadeShootPacket()
    {
        super(GRENADE_SHOOT);
    }

    public GrenadeShootPacket(int clientID, Grenade grenade)
    {
        super(GRENADE_SHOOT);

        data.put(clientID);

        data.put(grenade.getID());
        data.put(grenade.getName());

        data.put(grenade.getPosition().x);
        data.put(grenade.getPosition().y);
        data.put(grenade.getPosition().z);

        data.put(grenade.getRotation().x);
        data.put(grenade.getRotation().y);
        data.put(grenade.getRotation().z);
        data.put(grenade.getRotation().w);

        data.put(grenade.getForce());

        data.flip();
    }

    public GrenadeShootPacket(GrenadeShootPacket packet)
    {
        super(GRENADE_SHOOT);

        data.put(packet.clientID);

        data.put(packet.id);
        data.put(packet.name);

        data.put(packet.position.x);
        data.put(packet.position.y);
        data.put(packet.position.z);

        data.put(packet.rotation.x);
        data.put(packet.rotation.y);
        data.put(packet.rotation.z);
        data.put(packet.rotation.w);

        data.put(packet.shootForce);

        data.flip();
    }

    public void read(DataBuffer data)
    {
        clientID = data.getInt();
        id = data.getInt();
        name = data.getString();
        position = new Vec3(data.getFloat(), data.getFloat(), data.getFloat());
        rotation = new Quat(data.getFloat(), data.getFloat(), data.getFloat(), data.getFloat());
        shootForce = data.getFloat();
    }

    public void process(NetworkableServer server, InetAddress address, int port)
    {
        server.tcpSendToAll(new GrenadeShootPacket(this));
    }

    public void process(NetworkableClient client, InetAddress address, int port)
    {
        Grenade grenade = new Grenade(id, clientID, position, rotation, shootForce);
        grenade.setNetwork(client);
        client.getCore().getGame().spawn(grenade);
    }
}
