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
 *     along with Ubercube.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.veridiangames.core.network.packets;

import java.net.InetAddress;

import fr.veridiangames.core.game.entities.components.ECWeapon;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;

/**
 * Created by Marccspro on 26 f�vr. 2016.
 */
public class WeaponPositionPacket extends Packet
{
	private int clientID;
	private int weaponPosition;
	
	public WeaponPositionPacket()
	{
		super(WEAPON_POS);
	}
	
	public WeaponPositionPacket(int client, int position)
	{
		super(WEAPON_POS);
		data.put(client);
		data.put(position);
		data.flip();
	}
	
	public WeaponPositionPacket(WeaponPositionPacket packet)
	{
		super(WEAPON_POS);
		data.put(clientID);
		data.put(weaponPosition);
		data.flip();
	}

	public void read(DataBuffer data)
	{
		clientID = data.getInt();
		weaponPosition = data.getInt();
	}

	public void process(NetworkableServer server, InetAddress address, int port)
	{
		server.sendToAny(new WeaponPositionPacket(clientID, weaponPosition), clientID);
	}

	public void process(NetworkableClient client, InetAddress address, int port)
	{
		ECWeapon weapon = (ECWeapon) client.getCore().getGame().getEntityManager().get(clientID).get(EComponent.WEAPON);
		weapon.getWeapon().setPosition(weaponPosition);
	}
}