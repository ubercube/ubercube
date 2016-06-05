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
 *     along with Ubercube.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.veridiangames.client.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.PacketManager;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.client.main.Main;
import fr.veridiangames.client.main.console.Console;

/**
 * Created by Marccspro on 24 f�vr. 2016.
 */
public class NetworkClient implements Runnable, NetworkableClient
{
	private boolean running = false;

	private int 			id;
	private int				port;
	private InetAddress		address;
	private DatagramSocket	socket;
	private Main 			main;
	private Console 		console;
	private boolean 		connected;

	public NetworkClient(int id, String address, int port, Main main)
	{
		this.main = main;
		this.console = main.getConsole();
		connect(id, address, port);
		new Thread(this, "receive-thread").start();
	}

	private boolean connect(int id, String address, int port)
	{
		try
		{
			this.id = id;
			this.address = InetAddress.getByName(address);
			this.port = port;
			this.socket = new DatagramSocket();
			
			log("Connecting to " + address + ":" + port);
			
			return true;
		}
		catch (SocketException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public void run()
	{
		running = true;
		
		while (running)
		{
			try
			{
				byte[] data = new byte[2048];
				DatagramPacket packet = new DatagramPacket(data, data.length);
				socket.receive(packet);
				parsePacket(packet);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void parsePacket(DatagramPacket receive)
	{
		DataBuffer data = new DataBuffer(receive.getData());
		Packet packet = PacketManager.getPacket(data.getInt());
		packet.read(data);
		packet.process(this, receive.getAddress(), receive.getPort());
	}
	
	public void send(DataBuffer data)
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
	
	public void send(Packet packet)
	{
		send(packet.getData());
	}
	
	public void stop()
	{
		socket.close();
		log("Network has terminated !");

		System.exit(0);
	}

	public GameCore getCore()
	{
		return main.getGameCore();
	}
	
	public void log(String msg)
	{
		System.out.println(msg);
		console.print(msg);
	}
	
	public void setConsole(Console c)
	{
		this.console = c;
	}

	public void setConnected(boolean connected)
	{
		this.connected = connected;
	}

	public boolean isConnected()
	{
		return connected;
	}

	public int getPort()
	{
		return port;
	}

	public InetAddress getAddress()
	{
		return address;
	}
}