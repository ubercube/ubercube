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

package fr.veridiangames.server;

import java.util.Scanner;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.server.server.NetworkServer;

/**
 * Created by Marccspro on 31 janv. 2016.
 */
public class ServerMain
{
	private Scanner 		scanner;
	private NetworkServer 	server;
	private GameCore 		core;
	
	public ServerMain()
	{
		this.scanner = new Scanner(System.in);
		this.core = new GameCore();
		int port = 25565;
		this.server = new NetworkServer(port, scanner);
		this.server.setGameCore(core);
	}

	public static void main(String[] args)
	{
		new ServerMain();
	}
}