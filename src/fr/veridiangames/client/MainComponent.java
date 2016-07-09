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

package fr.veridiangames.client;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.client.main.Main;
import fr.veridiangames.client.rendering.Display;

import java.security.SecureRandom;

/**
 * Created by Marccspro on 28 janv. 2016.
 */
public class MainComponent
{
	public static void main(String[] args)
	{
		Main main = new Main();
		main.setDisplay(new Display("Ubercube", 1280, 720));
		main.setGameCore(new GameCore());

		SecureRandom rand = new SecureRandom();
		rand.setSeed(System.nanoTime());
		int 	clientID = 	rand.nextInt(999999999); //Integer.parseInt(args[0]);
		String 	address = 	args[0].split(":")[0];
		int 	port = 		Integer.parseInt(args[0].split(":")[1]);
		String name = args[1];

		main.openConnection(clientID, name, address, port);
		main.start();
	}
}