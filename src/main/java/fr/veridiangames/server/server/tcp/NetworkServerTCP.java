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

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.Log;
import fr.veridiangames.server.server.NetworkServer;
import fr.veridiangames.server.server.tcp.client.RemoteClient;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

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
            log("TCP: Server already listening on port: " + port);
            log("TCP: Server Failed to connect !");
            log("TCP: Terminating tcp...");
            System.exit(0);
        }
    }

    public void run()
    {
        log("TCP: Connected !");
        while (socket != null)
        {
            try
            {
                if (socket.isClosed())
                    break;
                Socket acceptedClient = this.socket.accept();
                RemoteClient client = new RemoteClient(acceptedClient, server).start();
                clients.add(client);
                clientsToAdd.add(client);
            }
            catch (IOException e)
            {
                socket = null;
            }
        }
    }

    public void send(Packet packet, InetAddress address, int port)
    {
        RemoteClient client = getClient(address, port);
        if (client != null)
            client.send(packet);
    }

    public RemoteClient getClient(InetAddress address, int port)
    {
        for (int i = 0; i < clients.size(); i++)
        {
            try
            {
                RemoteClient client = clients.get(i);
                Socket sock = client.getSocket();
                String sockIp = sock.getInetAddress().getHostAddress();
                String ip = address.getHostAddress();
                if (sockIp.equals(ip) && sock.getPort() == port)
                    return client;
            }
            catch (Exception e)
            {
                clients.remove(i);
                return null;
            }
        }
        return null;
    }

    public void log(String msg)
    {
        server.log(msg);
    }

    public void stop()
    {
        log("TCP: Closing connection...");
        for (RemoteClient client : clients)
        	client.stop();
		try {
			socket.close();
		} catch (IOException e) {
			Log.exception(e);
		}
	}

    public void disconnectClient(InetAddress address, int port)
    {
        RemoteClient client = getClient(address, port);
        if (client == null)
        {
            clients.remove(client);
            return;
        }
		client.stop();
		clientsToAdd.remove(client);
        clients.remove(client);
    }

    public ArrayList<RemoteClient> getClients()
    {
        return clients;
    }

	public ServerSocket getSocket() {
		return socket;
	}

	public ArrayList<RemoteClient> getClientsToAdd() {
		return clientsToAdd;
	}

	public NetworkServer getServer() {
		return server;
	}
}

