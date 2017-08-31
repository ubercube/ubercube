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

import fr.veridiangames.core.audio.Sound;
import fr.veridiangames.core.game.entities.audio.AudioSource;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.game.entities.player.ServerPlayer;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;

/**
 * Created by Marccspro on 26 f�vr. 2016.
 */
public class BulletHitPlayerPacket extends Packet
{
    private int playerId;
    private int life;
    private boolean hitable;

    public BulletHitPlayerPacket()

    {
        super(BULLET_HIT_PLAYER);
    }

    public BulletHitPlayerPacket(Player player)
    {
        super(BULLET_HIT_PLAYER);
        this.data.put(player.getID());
        this.data.put(0);
        this.data.put(player.isHitable() ? 1 : 0);

        this.data.flip();
    }

    public BulletHitPlayerPacket(BulletHitPlayerPacket packet, boolean hitable, int life)
    {
        super(BULLET_HIT_PLAYER);
        this.data.put(packet.playerId);
        this.data.put(life);
        this.data.put(hitable ? 1 : 0);

        this.data.flip();
    }

    @Override
	public void read(DataBuffer data)
    {
        this.playerId = data.getInt();
        this.life = data.getInt();
        this.hitable = data.getInt() == 0 ? false : true;
    }

    @Override
	public void process(NetworkableServer server, InetAddress address, int port)
    {
        ServerPlayer p = (ServerPlayer) server.getCore().getGame().getEntityManager().getEntities().get(this.playerId);
        if (p == null)
            return;
        boolean dead = p.applyDamage(20, server);
        if(!dead)
            server.tcpSendToAll(new BulletHitPlayerPacket(this, p.isHitable(), p.getLife()));
    }

    @Override
	public void process(NetworkableClient client, InetAddress address, int port)
    {
        if (client.getCore().getGame().getPlayer().getID() == this.playerId)
			client.playSound(new AudioSource(Sound.PLAYER_HIT));
		else if (client.getCore().getGame().getEntityManager().getEntities().containsKey(this.playerId))
			client.playSound(new AudioSource(Sound.PLAYER_HIT, ((Player) client.getCore().getGame().getEntityManager().get(this.playerId)).getPosition().copy()));
    }
}