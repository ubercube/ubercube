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
import java.text.SimpleDateFormat;
import java.util.*;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.components.ECNetwork;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.game.entities.player.ServerPlayer;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.network.PacketManager;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.network.packets.PingPacket;
import fr.veridiangames.core.network.packets.TimeoutPacket;
import fr.veridiangames.core.utils.*;
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

	private NetworkServerTCP 		tcp;
	private NetworkServerUDP 		udp;

	private GameCore				core;
	private Scanner					scanner;
	private Map<String, Command>	commands;

	public NetworkServer(int port, Scanner scanner, GameCore core)
	{
		this.port = port;
		this.core = core;
		this.scanner = scanner;
		this.commands = new HashMap<>();
		this.commands.put("help", new CmdHelp());
		this.commands.put("stop", new CmdStop());
		this.commands.put("kick", new CmdKick());
		
		log("Requesting server start on the " + SystemUtils.getDate());
		log("Starting server for " + GameCore.GAME_NAME + " " + GameCore.GAME_VERSION_NAME + " v" + GameCore.GAME_SUB_VERSION);
		long seed = core.getGame().getData().getWorldGen().getSeed();
		log("Generating world with seed: " + seed + " (" + ((seed % 2 == 0) ? "SNOWY" : "GRASSY") + ")");
		//log("Setting up server files...");
		//FileManager.init();
		log("Requesting connection on port " + port + "...");

		tcp = new NetworkServerTCP(this, port);
		udp = new NetworkServerUDP(this, port);

		new Thread(this, "server-main-thread").start();
		ping();
	}

	public void run()
	{
		log("Server successfully started on port " + port + " !");
		log("Type \"help\" to list every command.");
		log("Type \"stop\" to stop the server.");
		while (true)
		{
			Sleep.sleep(500);
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
		while (true)
		{
			Sleep.sleep(2000);
			for (int i = 0; i < core.getGame().getEntityManager().getPlayerEntites().size(); i++)
			{
				int key = core.getGame().getEntityManager().getPlayerEntites().get(i);
				ServerPlayer player = (ServerPlayer) core.getGame().getEntityManager().getEntities().get(key);
				player.setPinged(false);
				player.setTimeOutTests(player.getTimeOutTests() + 1);
				if (player.getTimeOutTests() > 5)
				{
					tcpSendToAll(new TimeoutPacket(key));
					tcp.disconnectClient(player.getNetwork().getAddress(), player.getNetwork().getPort());
					core.getGame().remove(key);
					log(player.getName() + " timed out !");
				}
				tcpSendToAll(new PingPacket(player.getID(), System.currentTimeMillis(), player.getPing()));
			}
		}
	}

	public void tcpSend(Packet packet, InetAddress address, int port)
	{
		tcp.send(packet, address, port);
	}

	public void tcpSendToAll(Packet packet)
	{
		for (int ii = 0; ii < core.getGame().getEntityManager().getNetworkableEntites().size(); ii++)
		{
			int i = core.getGame().getEntityManager().getNetworkableEntites().get(ii);
			Entity e = core.getGame().getEntityManager().getEntities().get(i);
			ECNetwork net = (ECNetwork) e.get(EComponent.NETWORK);
			tcpSend(packet, net.getAddress(), net.getPort());
		}
	}

	public void tcpSendToAny(Packet packet, int... ignores)
	{
		for (int ii = 0; ii < core.getGame().getEntityManager().getNetworkableEntites().size(); ii++)
		{
			int i = core.getGame().getEntityManager().getNetworkableEntites().get(ii);
			boolean ignore = false;
			for (int j = 0; j < ignores.length; j++)
			{
				if (i == ignores[j])
				{
					ignore = true;
					break;
				}
			}
			if (ignore)
				continue;
			Entity e = core.getGame().getEntityManager().getEntities().get(i);
			ECNetwork net = (ECNetwork) e.get(EComponent.NETWORK);
			tcpSend(packet, net.getAddress(), net.getPort());
		}
	}

	public void udpSend(Packet packet, InetAddress address, int port)
	{
		udp.send(packet.getData().getData(), address, port);
	}

	public void udpSendToAll(Packet packet)
	{
		for (int ii = 0; ii < core.getGame().getEntityManager().getNetworkableEntites().size(); ii++)
		{
			int i = core.getGame().getEntityManager().getNetworkableEntites().get(ii);
			Entity e = core.getGame().getEntityManager().getEntities().get(i);
			ECNetwork net = (ECNetwork) e.get(EComponent.NETWORK);
			udpSend(packet, net.getAddress(), net.getPort());
		}
	}

	public void udpSendToAny(Packet packet, int... ignores)
	{
		for (int ii = 0; ii < core.getGame().getEntityManager().getNetworkableEntites().size(); ii++)
		{
			int i = core.getGame().getEntityManager().getNetworkableEntites().get(ii);
			boolean ignore = false;
			for (int j = 0; j < ignores.length; j++)
			{
				if (i == ignores[j])
				{
					ignore = true;
					break;
				}
			}
			if (ignore)
				continue;
			Entity e = core.getGame().getEntityManager().getEntities().get(i);
			ECNetwork net = (ECNetwork) e.get(EComponent.NETWORK);
			udpSend(packet, net.getAddress(), net.getPort());
		}
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
			Log.exception(e);
		}
	}

	public int getPort()
	{
		return port;
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
		if (msg.toLowerCase().contains("error"))
			System.err.println("[" + Time.getTime() + "] " + msg);
		else
			System.out.println("[" + Time.getTime() + "] " + msg);
	}

	public Map<String, Command> getCommands()
	{
		return commands;
	}
}