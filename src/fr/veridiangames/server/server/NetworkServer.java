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

package fr.veridiangames.server.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.components.ECNetwork;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.network.PacketManager;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.core.utils.SystemUtils;
import fr.veridiangames.server.FileManager;
import fr.veridiangames.server.server.commands.CmdHelp;
import fr.veridiangames.server.server.commands.CmdKick;
import fr.veridiangames.server.server.commands.CmdStop;
import fr.veridiangames.server.server.commands.Command;

/**
 * Created by Marccspro on 24 f�vr. 2016.
 */
public class NetworkServer implements Runnable, NetworkableServer
{
	private int						port;
	private DatagramSocket			socket;
	private GameCore				core;
	private Scanner					scanner;
	private Map<String, Command>	commands;

	public NetworkServer(int port, Scanner scanner)
	{
		this.port = port;
		this.scanner = scanner;
		this.commands = new HashMap<String, Command>();
		this.commands.put("help", new CmdHelp());
		this.commands.put("stop", new CmdStop());
		this.commands.put("kick", new CmdKick());
		
		
		log("Requesting server start on the " + SystemUtils.getDate());
		log("Setting up server files...");
		FileManager.init();
		log("Requesting connection on port " + port + "...");
		openConnection();
	}

	private void openConnection()
	{
		try
		{
			this.socket = new DatagramSocket(port);
			new Thread(this, "server-thread").start();
		}
		catch (SocketException e)
		{
			log("Server already listening on port: " + port);
			log("Server Failed to connect !");
			log("Terminating...");
			System.exit(0);
		}
	}

	public void run()
	{
		log("Server successfully started on port " + port + " !");
		log("Type \"help\" to list every command.");
		log("Type \"stop\" to stop the server.");
		receive();
		while (true)
		{
			System.out.print("> ");
			String cmd = scanner.nextLine();
			String[] params = cmd.split(" ");
			String cmdName = params[0];
			if (commands.containsKey(cmdName))
			{
				commands.get(cmdName).process(this, params);
			}
		}
	}

	private void receive()
	{
		new Thread("receive-thread")
		{
			public void run()
			{
				while (true)
				{
					try
					{
						byte[] data = new byte[2048];
						DatagramPacket receive = new DatagramPacket(data, data.length);
						socket.receive(receive);
						parsePacket(receive);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	public void parsePacket(DatagramPacket receive)
	{
		DataBuffer data = new DataBuffer(receive.getData());
		Packet packet = PacketManager.getPacket(data.getInt());
		packet.read(data);
		packet.process(this, receive.getAddress(), receive.getPort());
	}

	public void send(DataBuffer data, InetAddress address, int port)
	{
		new Thread("send-thread")
		{
			public void run()
			{
				try
				{
					DatagramPacket packet = new DatagramPacket(data.getData(), data.getData().length, address, port);
					socket.send(packet);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void send(Packet packet, InetAddress address, int port)
	{
		send(packet.getData(), address, port);
	}

	public void sendToAll(DataBuffer data)
	{
		for (int i : core.getGame().getEntityManager().getNetworkableEntites())
		{
			Entity e = core.getGame().getEntityManager().getEntities().get(i);
			ECNetwork net = (ECNetwork) e.get(EComponent.NETWORK);
			send(data, net.getAddress(), net.getPort());
		}
	}

	public void sendToAll(Packet packet)
	{
		sendToAll(packet.getData());
	}

	public void sendToAny(DataBuffer data, int... ignoreID)
	{
		for (int i : core.getGame().getEntityManager().getNetworkableEntites())
		{
			for (int j = 0; j < ignoreID.length; j++)
				if (i == ignoreID[j])
					continue;

			Entity e = core.getGame().getEntityManager().getEntities().get(i);
			ECNetwork net = (ECNetwork) e.get(EComponent.NETWORK);
			send(data, net.getAddress(), net.getPort());
		}
	}

	public void sendToAny(Packet packet, int... ignoreID)
	{
		sendToAny(packet.getData(), ignoreID);
	}

	public void stop()
	{
		socket.close();
		log("Server stopped !");
	}

	public int getPort()
	{
		return port;
	}

	public void setGameCore(GameCore core)
	{
		this.core = core;
	}

	public GameCore getCore()
	{
		return core;
	}

	public void log(String msg)
	{
		System.out.println(msg);
	}

	public Map<String, Command> getCommands()
	{
		return commands;
	}
}