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

import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;

import java.net.InetAddress;

/**
 * Created by Tybau on 09/08/2017.
 */
public class ApplyDamagePacket extends Packet
{
    private int id;
    private int damage;

    public ApplyDamagePacket()
    {
        super(APPLY_DAMAGE);
    }

    public ApplyDamagePacket(Player p, int damage)
    {
        super(APPLY_DAMAGE);

        data.put(p.getID());
        data.put(damage);

        data.flip();
    }

    public void read(DataBuffer buffer)
    {
        this.id = buffer.getInt();
        this.damage = buffer.getInt();
    }

    public void process(NetworkableServer server, InetAddress address, int port)
    {

    }

    public void process(NetworkableClient client, InetAddress address, int port)
    {
        ClientPlayer p = client.getCore().getGame().getPlayer();
        p.setLife(p.getLife() - this.damage);
    }
}
