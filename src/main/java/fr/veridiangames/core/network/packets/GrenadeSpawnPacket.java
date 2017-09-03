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
import fr.veridiangames.core.game.entities.grenades.Grenade;
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.game.entities.player.ServerPlayer;
import fr.veridiangames.core.game.entities.weapons.Weapon;
import fr.veridiangames.core.game.entities.weapons.explosiveWeapons.WeaponGrenade;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;

import java.net.InetAddress;

/**
 * Created by Tybau on 09/08/2017.
 */
public class GrenadeSpawnPacket extends Packet
{
    int id;
    int holderID;
    Vec3 position;
    Quat direction;
    float force;

    public GrenadeSpawnPacket()
    {
        super(GRENADE_SPAWN);
    }

    public GrenadeSpawnPacket (Grenade grenade)
    {
        super(GRENADE_SPAWN);
        data.put(grenade.getID());

        data.put(grenade.getHolderID());

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

    public GrenadeSpawnPacket (GrenadeSpawnPacket packet)
    {
        super(GRENADE_SPAWN);
        data.put(packet.id);

        data.put(packet.holderID);

        data.put(packet.position.x);
        data.put(packet.position.y);
        data.put(packet.position.z);

        data.put(packet.direction.x);
        data.put(packet.direction.y);
        data.put(packet.direction.z);
        data.put(packet.direction.w);

        data.put(packet.force);

        data.flip();
    }

    public void read(DataBuffer data)
    {
        id = data.getInt();
        holderID = data.getInt();
        position = new Vec3(data.getFloat(), data.getFloat(), data.getFloat());
        direction = new Quat(data.getFloat(), data.getFloat(), data.getFloat(), data.getFloat());
        force = data.getFloat();
    }

    public void process(NetworkableServer server, InetAddress address, int port)
    {
        ServerPlayer player = (ServerPlayer) server.getCore().getGame().getEntityManager().get(holderID);
        if (player == null)
            return;
        if (player.isDead())
            return;
        if (player.getGrenadeCount() > 0 && player.getGrenadeCount() <= player.getMaxGrenades())
        {
            player.setGrenadeCount(player.getGrenadeCount() - 1);
            server.tcpSendToAll(new GrenadeSpawnPacket(this));
        }
    }

    public void process(NetworkableClient client, InetAddress address, int port)
    {
        ClientPlayer player = client.getCore().getGame().getPlayer();
        if (player.getID() == holderID)
        {
            WeaponGrenade g = (WeaponGrenade) player.getWeaponManager().getWeapons().get(Weapon.GRENADE);
            g.setGrenadeCount(g.getGrenadesLeft() - 1);
        }

        GameCore.getInstance().getGame().spawn(new Grenade(id, holderID, position, direction, force).setNetwork(client));
    }
}
