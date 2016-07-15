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

import fr.veridiangames.client.Ubercube;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.Game;
import fr.veridiangames.core.game.entities.EntityManager;
import fr.veridiangames.core.game.entities.components.ECNetwork;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.network.PacketManager;
import fr.veridiangames.core.network.packets.DisconnectPacket;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.core.utils.DataStream;
import fr.veridiangames.server.server.NetworkServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Marc on 05/07/2016.
 */
public class NetworkServerTCP implements Runnable
{
    private GameCore core;
    private NetworkServer server;
    private ServerSocket socket;
    private List<RemoteClient> clients;

    private List<RemoteClient> clientsToAdd = new ArrayList<>();
    private Thread receiverThread = new Thread("tcp-receiver") {
    private List<RemoteClient> receiverThreadClients = new ArrayList<>();

    public void run()
    {
        while (socket != null)
        {
            clientsToAdd.removeAll(receiverThreadClients);
            receiverThreadClients.addAll(clientsToAdd);

            ArrayList<RemoteClient> toRemove = new ArrayList<>();
            for (RemoteClient client : receiverThreadClients) {
                if (!clients.contains(client))
                    toRemove.add(client);
            }

            receiverThreadClients.removeAll(toRemove);

            for (RemoteClient client : receiverThreadClients)
            {
                try
                {
                    if (client.getInputStream() == null)
                    {
                        disconnectClient(client.getSocket().getInetAddress(), client.getSocket().getPort());
                        server.tcpSendToAll(new DisconnectPacket(client.getId(), "Client disconnected by the server"));
                        continue;
                    }

                    if (client.getInputStream().available() < Packet.MAX_SIZE) {
                        continue;
                    }

                    byte[] bytes = DataStream.readPacket(client.getInputStream());
                    DataBuffer data = new DataBuffer(bytes);
                    Packet packet = PacketManager.getPacket(data.getInt());

                    if (packet == null)
                    {
                        log("TCP: " + getTime() + " [ERROR]-> Received empty packet");
                        continue;
                    }

                    if (GameCore.isDisplayNetworkDebug())
                        log("TCP: " + getTime() + " [IN]-> received: " + packet);

                    packet.read(data);
                    packet.process(server, client.getSocket().getInetAddress(), client.getSocket().getPort());
                } catch (IOException e)
                {
                    disconnectClient(client.getSocket().getInetAddress(), client.getSocket().getPort());
                    server.tcpSendToAll(new DisconnectPacket(client.getId(), "Client disconnected by the server"));
                }
            }
        }
        log("TCP: Stopping tcp-receiver Thread");
    }
    };

    public NetworkServerTCP(NetworkServer server, int port)
    {
        try
        {
            this.core = GameCore.getInstance();
            this.server = server;
            this.clients = new ArrayList<>();
            this.socket = new ServerSocket(port);
            //this.socket.setReceiveBufferSize(Packet.MAX_SIZE);
            new Thread(this, "tcp-clients").start();
            receiverThread.start();
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
        while (socket != null)
        {
            try
            {
                Socket acceptedClient = this.socket.accept();
                RemoteClient client = new RemoteClient(acceptedClient, server);
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

    public void stop() throws IOException
    {
        log("TCP: Closing connection...");
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
        clientsToAdd.remove(client);
        clients.remove(client);
    }

    public String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");//dd/MM/yyyy
        Calendar calendar = Calendar.getInstance();
        String time = dateFormat.format(calendar.getTime());
        return time;
    }

    public List<RemoteClient> getClients()
    {
        return clients;
    }
}

