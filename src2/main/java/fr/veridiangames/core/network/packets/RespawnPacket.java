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

        this.data.put(player.getID());

        this.data.put(player.getName());

        Vec3 spawn = GameCore.getInstance().getGame().getGameMode().getPlayerSpawn((Player) GameCore.getInstance().getGame().getEntityManager().get(this.playerId));
        this.data.put(spawn.x);
        this.data.put(spawn.y);
        this.data.put(spawn.z);

        this.data.put(player.getRotation().x);
        this.data.put(player.getRotation().y);
        this.data.put(player.getRotation().z);
        this.data.put(player.getRotation().w);

        this.data.flip();
    }

    public RespawnPacket(RespawnPacket packet)
    {
        super(RESPAWN);

        this.data.put(packet.playerId);

        this.data.put(packet.name);

        this.data.put(packet.position.x);
        this.data.put(packet.position.y);
        this.data.put(packet.position.z);

        this.data.put(packet.rotation.x);
        this.data.put(packet.rotation.y);
        this.data.put(packet.rotation.z);
        this.data.put(packet.rotation.w);

        this.data.flip();
    }


    public RespawnPacket(Player p, Vec3 v)
    {
        super(RESPAWN);

        this.data.put(p.getID());

        this.data.put(p.getName());

        this.data.put(v.x);
        this.data.put(v.y);
        this.data.put(v.z);

        this.data.put(p.getRotation().x);
        this.data.put(p.getRotation().y);
        this.data.put(p.getRotation().z);
        this.data.put(p.getRotation().w);

        this.data.flip();
    }

    @Override
	public void read(DataBuffer buffer)
    {
        this.playerId = buffer.getInt();
        this.name = buffer.getString();
        this.position = new Vec3(buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
        this.rotation = new Quat(buffer.getFloat(), buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
    }

    @Override
	public void process(NetworkableServer server, InetAddress address, int port)
    {
        ServerPlayer p = (ServerPlayer) server.getCore().getGame().getEntityManager().getEntities().get(this.playerId);
        p.setLife(Player.MAX_LIFE);
        p.setDead(false);
        p.setGrenadeCount(p.getMaxGrenades());
        p.setTimeSinceSpawn(0);

        // GAME MODE
        GameCore.getInstance().getGame().getGameMode().onPlayerSpawn((Player) GameCore.getInstance().getGame().getEntityManager().get(this.playerId), server);
        this.position = GameCore.getInstance().getGame().getGameMode().getPlayerSpawn((Player) GameCore.getInstance().getGame().getEntityManager().get(this.playerId));

        /*int x = server.getCore().getGame().getData().getWorldSize() * 8;
        int y = server.getCore().getGame().getData().getWorldSize() * 8;
        int height = (int) server.getCore().getGame().getData().getWorldGen().getNoise(x, y) + 15;
        this.position = new Vec3(x, height, y);      // TODO : Modify position*/

        server.tcpSendToAll(new RespawnPacket(this));
    }

    @Override
	public void process(NetworkableClient client, InetAddress address, int port)
    {
        if(this.playerId == client.getID())
        {
            ClientPlayer p = GameCore.getInstance().getGame().getPlayer();
            p.getRigidBody().getBody().killForces();
            p.setPosition(this.position);
            p.setLife(Player.MAX_LIFE);
            p.setDead(false);
            ((WeaponGrenade) p.getWeaponManager().getWeapons().get(Weapon.GRENADE)).resetGrenades();
        } else if(!client.getCore().getGame().getEntityManager().getPlayerEntites().contains(this.playerId))
			client.getCore().getGame().spawn(new NetworkedPlayer(this.playerId, this.name, this.position, this.rotation, address.getHostName(), port));
    }
}
