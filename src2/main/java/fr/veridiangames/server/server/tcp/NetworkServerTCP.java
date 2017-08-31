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

package fr.veridiangames.server.server.tcp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.Log;
import fr.veridiangames.server.server.NetworkServer;
import fr.veridiangames.server.server.tcp.client.RemoteClient;

/**
 * Created by Marc on 05/07/2016.
 */
public class NetworkServerTCP implements Runnable
{
    private GameCore core;
    private NetworkServer server;
    private ServerSocket socket;

    private ArrayList<RemoteClient> clients;
    private ArrayList<RemoteClient> clientsToAdd = new ArrayList<>();

    public NetworkServerTCP(NetworkServer server, int port)
    {
        try
        {
            this.core = GameCore.getInstance();
            this.server = server;
            this.clients = new ArrayList<>();
            this.socket = new ServerSocket(port);

			new Thread(this, "tcp-clients").start();
        }
        catch (IOException e)
        {
            this.log("TCP: Server already listening on port: " + port);
            this.log("TCP: Server Failed to connect !");
            this.log("TCP: Terminating tcp...");
            System.exit(0);
        }
    }

    @Override
	public void run()
    {
        this.log("TCP: Connected !");
        while (this.socket != null)
			try
            {
                if (this.socket.isClosed())
                    break;
                Socket acceptedClient = this.socket.accept();
                RemoteClient client = new RemoteClient(acceptedClient, this.server).start();
                this.clients.add(client);
                this.clientsToAdd.add(client);
            }
            catch (IOException e)
            {
                this.socket = null;
            }
    }

    public void send(Packet packet, InetAddress address, int port)
    {
        RemoteClient client = this.getClient(address, port);
        if (client != null)
            client.send(packet);
    }

    public RemoteClient getClient(InetAddress address, int port)
    {
        for (int i = 0; i < this.clients.size(); i++)
			try
            {
                RemoteClient client = this.clients.get(i);
                Socket sock = client.getSocket();
                String sockIp = sock.getInetAddress().getHostAddress();
                String ip = address.getHostAddress();
                if (sockIp.equals(ip) && sock.getPort() == port)
                    return client;
            }
            catch (Exception e)
            {
                this.clients.remove(i);
                return null;
            }
        return null;
    }

    public void log(String msg)
    {
        this.server.log(msg);
    }

    public void stop() throws IOException
    {
        this.log("TCP: Closing connection...");
        for (RemoteClient client : this.clients)
        	client.stop();
        this.socket.close();
    }

    public void disconnectClient(InetAddress address, int port)
    {
        RemoteClient client = this.getClient(address, port);
        if (client == null)
        {
            this.clients.remove(client);
            return;
        }
		try {
			client.stop();
		} catch (IOException e) {
			Log.exception(e);
		}
		this.clientsToAdd.remove(client);
        this.clients.remove(client);
    }

    public ArrayList<RemoteClient> getClients()
    {
        return this.clients;
    }

	public ServerSocket getSocket() {
		return this.socket;
	}

	public ArrayList<RemoteClient> getClientsToAdd() {
		return this.clientsToAdd;
	}

	public NetworkServer getServer() {
		return this.server;
	}
}

