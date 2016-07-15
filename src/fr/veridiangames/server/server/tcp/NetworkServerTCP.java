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
import fr.veridiangames.core.network.PacketManager;
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
    private NetworkServer server;
    private ServerSocket socket;
    private List<RemoteClient> clients;


    public List<RemoteClient> clientsToAddSender = new ArrayList<>();
    private Thread senderThread = new Thread("tcp-sender") {
        private List<RemoteClient> senderThreadClients = new ArrayList<>();

        public void run()
        {
            log("TCP: Starting tcp-sender");

            while (socket != null) {
                clientsToAddSender.removeAll(senderThreadClients);
                senderThreadClients.addAll(clientsToAddSender);

                for (RemoteClient client : senderThreadClients)
                {
                    if (client.getSocket() == null || client.getOutputStream() == null)
                    {
                        disconnectClient(client.getSocket().getInetAddress(), client.getSocket().getPort());
                        continue;
                    }

                    ArrayList<Packet> sendingQueue = new ArrayList<>(client.getSendQueue());

                    for (Packet packet : sendingQueue)
                    {
                        try
                        {
                            if (packet == null)
                            {
                                log("TCP: " + getTime() + " [ERROR]-> Tried to send a null packet");
                                continue;
                            }

                            if (packet.getData() == null)
                            {
                                log("TCP: " + getTime() + " [ERROR]-> Tried to send an empty packet");
                                log("TCP: " + getTime() + " [ERROR]-> " + packet);
                                continue;
                            }

                            byte[] bytes = packet.getData().getData();

                            if (bytes.length == 0)
                            {
                                log("TCP: " + getTime() + " [ERROR]-> Tried to send an empty packet");
                                continue;
                            }

                            if (GameCore.isDisplayNetworkDebug())
                                log("TCP: " + getTime() + " [OUT]-> sending: " + packet);

                            DataStream.writePacket(client.getOutputStream(), bytes);
                        } catch (IOException e)
                        {
                            disconnectClient(client.getSocket().getInetAddress(), client.getSocket().getPort());
                            e.printStackTrace();
                        }
                    }

                    client.getSendQueue().removeAll(sendingQueue);
                }
            }
            log("TCP: Stopping tcp-sender Thread");
        }
    };

    public List<RemoteClient> clientsToAddReceiver = new ArrayList<>();
    private Thread receiverThread = new Thread("tcp-receiver") {
        private List<RemoteClient> receiverThreadClients = new ArrayList<>();

        public void run() {
            log("TCP: Starting tcp-receiver");

            while (socket != null)
            {
                clientsToAddReceiver.removeAll(receiverThreadClients);
                receiverThreadClients.addAll(clientsToAddReceiver);

                for (RemoteClient client : receiverThreadClients)
                {
                    try
                    {
                        if (client.getInputStream() == null)
                        {
                            disconnectClient(client.getSocket().getInetAddress(), client.getSocket().getPort());
                            continue;
                        }

                        if (client.getInputStream().available() < Packet.MAX_SIZE)
                            continue;

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
//                        log("TCP: " + getTime() + " processing " + packet);
                        packet.process(server, client.getSocket().getInetAddress(), client.getSocket().getPort());
                    } catch (IOException e)
                    {
                        disconnectClient(client.getSocket().getInetAddress(), client.getSocket().getPort());
                        e.printStackTrace();
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
            this.server = server;
            log("TCP: Starting");
            this.clients = new ArrayList<>();
            this.socket = new ServerSocket(port);
            //this.socket.setReceiveBufferSize(Packet.MAX_SIZE);
            new Thread(this, "tcp-clients").start();
            senderThread.start();
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
                clientsToAddReceiver.add(client);
                clientsToAddSender.add(client);
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
        clients.remove(client);
    }

    private String getTime() {
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

