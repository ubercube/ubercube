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

import fr.veridiangames.client.Ubercube;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.game.entities.player.NetworkedPlayer;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.game.entities.player.ServerPlayer;
import fr.veridiangames.core.game.entities.weapons.Weapon;
import fr.veridiangames.core.game.entities.weapons.explosiveWeapons.WeaponGrenade;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.core.utils.Log;

import java.net.InetAddress;
import java.util.Random;

/**
 * Created by Tybau on 13/06/2016.
 */
public class RespawnPacket extends Packet
{
    private int playerId;
    private String name;
    private Vec3 position;
    private Quat rotation;

    public RespawnPacket()
    {
        super(RESPAWN);
    }

    public RespawnPacket(Player player)
    {
        super(RESPAWN);

        data.put(player.getID());

        data.put(player.getName());

        Vec3 spawn = GameCore.getInstance().getGame().getGameMode().getPlayerSpawn(playerId);
        data.put(spawn.x);
        data.put(spawn.y);
        data.put(spawn.z);

        data.put(player.getRotation().x);
        data.put(player.getRotation().y);
        data.put(player.getRotation().z);
        data.put(player.getRotation().w);

        data.flip();
    }

    public RespawnPacket(RespawnPacket packet)
    {
        super(RESPAWN);

        data.put(packet.playerId);

        data.put(packet.name);

        data.put(packet.position.x);
        data.put(packet.position.y);
        data.put(packet.position.z);

        data.put(packet.rotation.x);
        data.put(packet.rotation.y);
        data.put(packet.rotation.z);
        data.put(packet.rotation.w);

        data.flip();
    }


    public RespawnPacket(Player p, Vec3 v)
    {
        super(RESPAWN);

        data.put(p.getID());

        data.put(p.getName());

        data.put(v.x);
        data.put(v.y);
        data.put(v.z);

        data.put(p.getRotation().x);
        data.put(p.getRotation().y);
        data.put(p.getRotation().z);
        data.put(p.getRotation().w);

        data.flip();
    }

    public void read(DataBuffer buffer)
    {
        this.playerId = buffer.getInt();
        this.name = buffer.getString();
        this.position = new Vec3(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
        this.rotation = new Quat(buffer.getFloat(), buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
    }

    public void process(NetworkableServer server, InetAddress address, int port)
    {
        ServerPlayer p = (ServerPlayer) server.getCore().getGame().getEntityManager().getEntities().get(playerId);
        p.setLife(Player.MAX_LIFE);
        p.setDead(false);
        p.setGrenadeCount(p.getMaxGrenades());
        p.setTimeSinceSpawn(0);

        // GAME MODE
        GameCore.getInstance().getGame().getGameMode().onPlayerSpawn(playerId, server);
		Vec3 spawPos = GameCore.getInstance().getGame().getGameMode().getPlayerSpawn(playerId);
		this.position = new Vec3(spawPos.x, server.getCore().getGame().getWorld().getHeightAt((int)spawPos.x, (int)spawPos.z), spawPos.z);

        /*int x = server.getCore().getGame().getData().getWorldSize() * 8;
        int y = server.getCore().getGame().getData().getWorldSize() * 8;
        int height = (int) server.getCore().getGame().getData().getWorldGen().getNoise(x, y) + 15;
        this.position = new Vec3(x, height, y);      // TODO : Modify position*/

        server.tcpSendToAll(new RespawnPacket(this));
    }

    public void process(NetworkableClient client, InetAddress address, int port)
    {
        if(playerId == client.getID())
        {
            ClientPlayer p = GameCore.getInstance().getGame().getPlayer();
            p.getRigidBody().getBody().killForces();
            p.setPosition(this.position);
            p.setLife(Player.MAX_LIFE);
            p.setDead(false);
            ((WeaponGrenade) p.getWeaponManager().getWeapons().get(Weapon.GRENADE)).resetGrenades();
        }
        else
        {
            if(!client.getCore().getGame().getEntityManager().getPlayerEntites().contains(playerId))
            {
                client.getCore().getGame().spawn(new NetworkedPlayer(playerId, name, position, rotation, address.getHostName(), port));
            }
        }
    }
}
