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

package fr.veridiangames.core.game.entities.components;

import fr.veridiangames.core.utils.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Marccspro on 7 f�vr. 2016.
 */
public class ECNetwork extends EComponent
{
	private InetAddress	address;
	private int			port;

	public ECNetwork(String address, int port)
	{
		super(NETWORK);
		
		try
		{
			this.address = InetAddress.getByName(address);
			this.port = port;
		}
		catch (UnknownHostException e)
		{
			Log.exception(e);
		}
	}

	public InetAddress getAddress()
	{
		return address;
	}

	public int getPort()
	{
		return port;
	}
}
