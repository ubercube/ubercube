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

package fr.veridiangames.server;

import static java.lang.Math.abs;

import java.util.Random;
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

	public ServerMain(int port)
	{
		this.scanner = new Scanner(System.in);
		this.core = new GameCore();
		this.core.getGame().createWorld(abs(new Random().nextInt()));

		this.server = new NetworkServer(port, this.scanner, this.core);
	}

	public static void main(String[] args)
	{
		if (args.length != 1)
			System.out.println("Usage: ./ubercube_server port");
		else
			new ServerMain(Integer.parseInt(args[0]));
	}
}