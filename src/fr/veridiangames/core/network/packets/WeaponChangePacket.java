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

import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;

import java.net.InetAddress;

/**
 * Created by Tybau on 20/06/2016.
 */
public class WeaponChangePacket extends Packet
{
    private int playerID;
    private int weaponID;

    public WeaponChangePacket()
    {
        super(WEAPON_CHANGE);
    }

    public WeaponChangePacket(Player player)
    {
        super(WEAPON_CHANGE);

        data.put(player.getID());
        data.put(player.getWeaponManager().getWeaponID());

        data.flip();
    }

    public WeaponChangePacket(WeaponChangePacket packet)
    {
        super(WEAPON_CHANGE);

        data.put(packet.playerID);
        data.put(packet.weaponID);

        data.flip();
    }

    public void read(DataBuffer buffer)
    {
        playerID = buffer.getInt();
        weaponID = buffer.getInt();
    }

    public void process(NetworkableServer server, InetAddress address, int port)
    {
        server.udpSendToAll(new WeaponChangePacket(this));
    }

    public void process(NetworkableClient client, InetAddress address, int port)
    {
        if(playerID != client.getCore().getGame().getPlayer().getID())
        {
            Player p = (Player)client.getCore().getGame().getEntityManager().getEntities().get(playerID);
            if(p != null)
            {
                p.getWeaponManager().setWeapon(weaponID);
            }
        }
    }
}
