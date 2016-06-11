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

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Random;

import fr.veridiangames.core.utils.FileUtils;
import fr.veridiangames.core.utils.Log;
import fr.veridiangames.core.utils.SystemUtils;

public class FileManager
{
	//UFS = Ubercube File System
	
	public static void init()
	{
		setWorldFolder();
		setConfigFile();
		setPlayerDataFile();
		setPlayerBanFile();
		setPlayerAdminFile();
		setPlayerAdminFile();
		setPlayerKickedFile();
	}
	
	private static void setWorldFolder()
	{
		if (!FileUtils.fileExist("world"))
			FileUtils.newFile("world");
	}
	
	private static void setConfigFile()
	{
		if (FileUtils.fileExist("config.txt"))
			return;
		
		Random rand = new Random();
		try
		{
			BufferedWriter w = FileUtils.writeFile("config.txt", false);
			w.write("#Ubercube Server Config\n");
			w.write("#Created on the " + SystemUtils.getDate() + "\n");
			w.write("#\n");
			w.write("WorldType:0\n");
			w.write("Seed:" + rand.nextLong() + "\n");
			w.write("ViewDistance:10\n");
			w.write("Fog:1.0\n");
			w.write("Name:My Ubercube Server\n");
			w.write("MaxPlayers:20");
			w.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private static void setPlayerDataFile()
	{
		if (FileUtils.fileExist("players.txt"))
			return;
		
		try
		{
			BufferedWriter w = FileUtils.writeFile("players.txt", false);
			w.write("#Server Player Database\n");
			w.write("#\n");
			w.write("# | Username | User ID | Type | Banned | Kicked |\n");
			w.write("#\n");
			w.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private static void setPlayerBanFile()
	{
		if (FileUtils.fileExist("banned.txt"))
			return;
		
		try
		{
			BufferedWriter w = FileUtils.writeFile("banned.txt", false);
			w.write("#Server Banned Players\n");
			w.write("#\n");
			w.write("# | Username | User ID |\n");
			w.write("#\n");
			w.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private static void setPlayerAdminFile()
	{
		if (FileUtils.fileExist("admins.txt"))
			return;
		
		try
		{
			BufferedWriter w = FileUtils.writeFile("admins.txt", false);
			w.write("#Server Admin Players\n");
			w.write("#\n");
			w.write("# | Username | User ID |\n");
			w.write("#\n");
			w.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private static void setPlayerKickedFile()
	{
		if (FileUtils.fileExist("kicked.txt"))
			return;
		
		try
		{
			BufferedWriter w = FileUtils.writeFile("kicked.txt", false);
			w.write("#Server Kicked Players\n");
			w.write("#\n");
			w.write("# | Username | User ID |\n");
			w.write("#\n");
			w.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
