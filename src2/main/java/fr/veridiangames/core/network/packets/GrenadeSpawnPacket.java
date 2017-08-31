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
        this.data.put(grenade.getID());

        this.data.put(grenade.getHolderID());

        this.data.put(grenade.getPosition().x);
        this.data.put(grenade.getPosition().y);
        this.data.put(grenade.getPosition().z);

        this.data.put(grenade.getRotation().x);
        this.data.put(grenade.getRotation().y);
        this.data.put(grenade.getRotation().z);
        this.data.put(grenade.getRotation().w);

        this.data.put(grenade.getForce());

        this.data.flip();
    }

    public GrenadeSpawnPacket (GrenadeSpawnPacket packet)
    {
        super(GRENADE_SPAWN);
        this.data.put(packet.id);

        this.data.put(packet.holderID);

        this.data.put(packet.position.x);
        this.data.put(packet.position.y);
        this.data.put(packet.position.z);

        this.data.put(packet.direction.x);
        this.data.put(packet.direction.y);
        this.data.put(packet.direction.z);
        this.data.put(packet.direction.w);

        this.data.put(packet.force);

        this.data.flip();
    }

    @Override
	public void read(DataBuffer data)
    {
        this.id = data.getInt();
        this.holderID = data.getInt();
        this.position = new Vec3(data.getFloat(), data.getFloat(), data.getFloat());
        this.direction = new Quat(data.getFloat(), data.getFloat(), data.getFloat(), data.getFloat());
        this.force = data.getFloat();
    }

    @Override
	public void process(NetworkableServer server, InetAddress address, int port)
    {
        ServerPlayer player = (ServerPlayer) server.getCore().getGame().getEntityManager().get(this.holderID);
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

    @Override
	public void process(NetworkableClient client, InetAddress address, int port)
    {
        ClientPlayer player = client.getCore().getGame().getPlayer();
        if (player.getID() == this.holderID)
        {
            WeaponGrenade g = (WeaponGrenade) player.getWeaponManager().getWeapons().get(Weapon.GRENADE);
            g.setGrenadeCount(g.getGrenadesLeft() - 1);
        }

        GameCore.getInstance().getGame().spawn(new Grenade(this.id, this.holderID, this.position, this.direction, this.force).setNetwork(client));
    }
}
