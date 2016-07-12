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

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.components.ECNetwork;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.game.entities.player.ServerPlayer;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.network.packets.PingPacket;
import fr.veridiangames.core.network.packets.TimeoutPacket;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.core.utils.Sleep;
import fr.veridiangames.core.utils.SystemUtils;
import fr.veridiangames.server.FileManager;
import fr.veridiangames.server.server.commands.CmdHelp;
import fr.veridiangames.server.server.commands.CmdKick;
import fr.veridiangames.server.server.commands.CmdStop;
import fr.veridiangames.server.server.commands.Command;
import fr.veridiangames.server.server.tcp.NetworkServerTCP;
import fr.veridiangames.server.server.udp.NetworkServerUDP;


/**
 * Created by Marccspro on 24 f�vr. 2016.
 */
public class NetworkServer implements Runnable, NetworkableServer
{
	private int						port;

	private NetworkServerTCP tcp;
	private NetworkServerUDP udp;

	private GameCore				core;
	private Scanner					scanner;
	private Map<String, Command>	commands;

	public NetworkServer(int port, Scanner scanner)
	{
		this.port = port;
		this.scanner = scanner;
		this.commands = new HashMap<>();
		this.commands.put("help", new CmdHelp());
		this.commands.put("stop", new CmdStop());
		this.commands.put("kick", new CmdKick());
		
		log("Requesting server start on the " + SystemUtils.getDate());
		log("Starting server for " + GameCore.GAME_NAME + " " + GameCore.GAME_VERSION_NAME + " v" + GameCore.GAME_SUB_VERSION);
		log("Setting up server files...");
		FileManager.init();
		log("Requesting connection on port " + port + "...");

		tcp = new NetworkServerTCP(this, port);
		udp = new NetworkServerUDP(this, port);

		new Thread(this, "server-main-thread").start();
	}

	public void run()
	{
		log("Server successfully started on port " + port + " !");
		log("Type \"help\" to list every command.");
		log("Type \"stop\" to stop the server.");
		ping();
		while (true)
		{
			String cmd = scanner.nextLine();
			String[] params = cmd.split(" ");
			String cmdName = params[0];
			if (commands.containsKey(cmdName))
			{
				commands.get(cmdName).process(this, params);
			}
		}
	}

	private void ping()
	{
		new Thread("ping-thread")
		{
			public void run()
			{
				long before = System.nanoTime();
				while (true)
				{
					Sleep.sleep(5000);
					for (int i = 0; i < core.getGame().getEntityManager().getPlayerEntites().size(); i++)
					{
						int key = core.getGame().getEntityManager().getPlayerEntites().get(i);
						ServerPlayer player = (ServerPlayer) core.getGame().getEntityManager().getEntities().get(key);
						player.setPinged(false);
						player.setTimeOutTests(player.getTimeOutTests() + 1);
						if (player.getTimeOutTests() > 5)
						{
							tcpSendToAll(new TimeoutPacket(key));
							core.getGame().remove(key);
							log(player.getName() + " timed out !");
						}
						tcpSend(new PingPacket(player.getID(), 0L, player.getPing()), player.getNetwork().getAddress(), player.getNetwork().getPort());
					}
				}
			}
		}.start();
	}

	public void tcpSend(DataBuffer data, InetAddress address, int port)
	{
		tcp.send(data.getData(), address, port);
	}

	public void tcpSend(Packet packet, InetAddress address, int port)
	{
		if (GameCore.isDisplayNetworkDebug())
			log("[OUT] Sending: " + packet);
		tcpSend(packet.getData(), address, port);
	}

	public void tcpSendToAll(DataBuffer data)
	{
		for (int ii = 0; ii < core.getGame().getEntityManager().getNetworkableEntites().size(); ii++)
		{
			int i = core.getGame().getEntityManager().getNetworkableEntites().get(ii);
			Entity e = core.getGame().getEntityManager().getEntities().get(i);
			ECNetwork net = (ECNetwork) e.get(EComponent.NETWORK);
			tcpSend(data, net.getAddress(), net.getPort());
		}
	}

	public void tcpSendToAll(Packet packet)
	{
		if (GameCore.isDisplayNetworkDebug())
			log("[OUT] Sending to all: " + packet);
		tcpSendToAll(packet.getData());
	}

	public void tcpSendToAny(DataBuffer data, int... ignoreID)
	{
		for (int ii = 0; ii < core.getGame().getEntityManager().getNetworkableEntites().size(); ii++)
		{
			int i = core.getGame().getEntityManager().getNetworkableEntites().get(ii);
			boolean passeIteration = false;
			for (int j = 0; j < ignoreID.length; j++)
			{
				if (i == ignoreID[j])
				{
					passeIteration = true;
					continue;
				}
			}
			if (passeIteration)
				continue;

			Entity e = core.getGame().getEntityManager().getEntities().get(i);
			ECNetwork net = (ECNetwork) e.get(EComponent.NETWORK);
			tcpSend(data, net.getAddress(), net.getPort());
		}
	}

	public void tcpSendToAny(Packet packet, int... ignoreID)
	{
		tcpSendToAny(packet.getData(), ignoreID);
	}

	public void udpSend(DataBuffer data, InetAddress address, int port)
	{
		udp.send(data.getData(), address, port);
	}

	public void udpSend(Packet packet, InetAddress address, int port)
	{
		udpSend(packet.getData(), address, port);
	}

	public void udpSendToAll(DataBuffer data)
	{
		for (int ii = 0; ii < core.getGame().getEntityManager().getNetworkableEntites().size(); ii++)
		{
			int i = core.getGame().getEntityManager().getNetworkableEntites().get(ii);
			Entity e = core.getGame().getEntityManager().getEntities().get(i);
			ECNetwork net = (ECNetwork) e.get(EComponent.NETWORK);
			udpSend(data, net.getAddress(), net.getPort());
		}
	}

	public void udpSendToAll(Packet packet)
	{
		udpSendToAll(packet.getData());
	}

	public void udpSendToAny(DataBuffer data, int... ignoreID)
	{
		for (int ii = 0; ii < core.getGame().getEntityManager().getNetworkableEntites().size(); ii++)
		{
			int i = core.getGame().getEntityManager().getNetworkableEntites().get(ii);
			boolean passeIteration = false;
			for (int j = 0; j < ignoreID.length; j++)
			{
				if (i == ignoreID[j])
				{
					passeIteration = true;
					continue;
				}
			}
			if (passeIteration)
				continue;

			Entity e = core.getGame().getEntityManager().getEntities().get(i);
			ECNetwork net = (ECNetwork) e.get(EComponent.NETWORK);
			udpSend(data, net.getAddress(), net.getPort());
		}
	}

	public void udpSendToAny(Packet packet, int... ignoreID)
	{
		udpSendToAny(packet.getData(), ignoreID);
	}

	public void stop()
	{
		try
		{
			tcp.stop();
			udp.stop();
			log("Server stopped !");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
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

	public NetworkServerTCP getTcp()
	{
		return tcp;
	}

	public NetworkServerUDP getUdp()
	{
		return udp;
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