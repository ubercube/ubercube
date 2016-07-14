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

import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.server.server.NetworkServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marc on 05/07/2016.
 */
public class NetworkServerTCP implements Runnable
{
    private NetworkServer server;
    private ServerSocket socket;
    private List<RemoteClient> clients;

    public NetworkServerTCP(NetworkServer server, int port)
    {
        try
        {
            this.server = server;
            log("Starting TCP connection");
            this.clients = new ArrayList<>();
            this.socket = new ServerSocket(port);
            this.socket.setReceiveBufferSize(Packet.MAX_SIZE);
            new Thread(this, "tcp-thread").start();
        }
        catch (IOException e)
        {
            log("Server already listening on port: " + port);
            log("Server Failed to connect !");
            log("Terminating tcp...");
            System.exit(0);
        }
    }

    public void run()
    {
        while (socket != null)
        {
            try
            {
                Socket acceptedClient = this.socket.accept();
                RemoteClient client = new RemoteClient(acceptedClient, server);
                clients.add(client);
                client.start();

            }
            catch (IOException e)
            {
                socket = null;
            }
        }

    }

    public void send(byte[] bytes, InetAddress address, int port)
    {
        RemoteClient client = getClient(address, port);
        if (client != null)
            client.send(bytes);
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

    public void stop() throws IOException
    {
        log("Closing TCP connection...");
        socket.close();
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
        clients.remove(client);
    }

    public List<RemoteClient> getClients()
    {
        return clients;
    }
}

