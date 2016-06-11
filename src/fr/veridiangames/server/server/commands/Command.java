/*
 * Copyright (C) 2016 Team Ubercube
 *
 *   This file is part of Ubercube.
 *
 *       Ubercube is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Ubercube is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Ubercube.  If not, see http://www.gnu.org/licenses/.
 */

package fr.veridiangames.server.server.commands;

import fr.veridiangames.server.server.NetworkServer;

public abstract class Command
{
	private String name;
	private String desc;
	
	public Command(String name, String desc)
	{
		this.name = name;
		this.desc = desc;
	}
	
	public abstract void process(NetworkServer server, String[] params);

	public String getName()
	{
		return name;
	}
	
	public String getDesc()
	{
		return desc;
	}
}
