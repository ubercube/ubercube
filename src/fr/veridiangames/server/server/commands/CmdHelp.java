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

import java.util.Map;
import java.util.Map.Entry;

import fr.veridiangames.server.server.NetworkServer;

public class CmdHelp extends Command
{
	public CmdHelp()
	{
		super("help", "Lists all of the commands.");
	}

	public void process(NetworkServer server, String[] params)
	{
		Map<String, Command> cmds = server.getCommands();
		
		server.log("Server commands:");
		for (Entry<String, Command> c : cmds.entrySet())
		{
			String name = c.getKey();
			String desc = c.getValue().getDesc();
			server.log(name + ": " + desc);
		}
	}
}
