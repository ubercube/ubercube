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

import fr.veridiangames.server.server.NetworkPacket;
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
    private List<ClientSocket> clients;

    public NetworkServerTCP(NetworkServer server, int port)
    {
        try
        {
            this.server = server;
            log("Starting TCP connection");
            this.clients = new ArrayList<>();
            this.socket = new ServerSocket(port);
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
        try
        {
            while (true)
            {
                Socket newClient = this.socket.accept();
                ClientSocket client = new ClientSocket(newClient, server);
                clients.add(client);
                client.start();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void send(byte[] bytes, InetAddress address, int port)
    {
        ClientSocket client = getClient(address, port);
        if (client != null)
            client.send(bytes);
    }

    public ClientSocket getClient(InetAddress address, int port)
    {
        for (ClientSocket client : clients)
        {
            Socket sock = client.getSocket();
            String sockIp = sock.getInetAddress().getHostName();
            String ip = address.getHostName();
            if (sockIp.equals(ip) && sock.getPort() == port)
                return client;
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
}

