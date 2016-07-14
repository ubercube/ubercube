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
import fr.veridiangames.core.network.PacketManager;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.core.utils.DataStream;
import fr.veridiangames.server.server.NetworkServer;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Marc on 07/07/2016.
 */
public class RemoteClient
{
    private Socket socket;

    private InputStream in;
    private OutputStream out;

    private NetworkServer server;

    private List<Packet> sendQueue;

    private Thread senderThread = new Thread("tcp-sender") {
        public void run()
        {
            server.log("TCP: Starting tcp-sender");
            while (socket != null) {
                if (out == null)
                    continue;

                ArrayList<Packet> sendingQueue = new ArrayList<>(sendQueue);

                for (Packet packet : sendingQueue)
                {
                    try
                    {
                        if (packet == null)
                        {
                            server.log ("TCP: " + getTime() + " [ERROR]-> Tried to send a null packet");
                            continue;
                        }

                        if (packet.getData() == null)
                        {
                            server.log ("TCP: " + getTime() + " [ERROR]-> Tried to send an empty packet");
                            server.log ("TCP: " + getTime() + " [ERROR]-> " + packet);
                            continue;
                        }

                        byte[] bytes = packet.getData().getData();

                        if (bytes.length == 0)
                        {
                            server.log ("TCP: " + getTime() + " [ERROR]-> Tried to send an empty packet");
                            continue;
                        }

                        if (GameCore.isDisplayNetworkDebug())
                            server.log("TCP: " + getTime() + " [OUT]-> sending: " + packet);

                        DataStream.writePacket(out, bytes);
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }

                sendQueue.removeAll(sendingQueue);
            }
            server.log("TCP: Stopping tcp-sender Thread");
        }
    };

    private Thread receiverThread = new Thread("tcp-receiver") {
        public void run() {
            server.log("TCP: Starting tcp-receiver");

            while (socket != null)
            {
                try
                {
                    byte[] bytes = DataStream.readPacket(in);
                    DataBuffer data = new DataBuffer(bytes);
                    Packet packet = PacketManager.getPacket(data.getInt());

                    if (packet == null)
                    {
                        server.log("TCP: " + getTime() + " [ERROR]-> Received empty packet");
                        continue;
                    }

                    if (GameCore.isDisplayNetworkDebug())
                        server.log("TCP: " + getTime() + " [IN]-> received: " + packet);

                    packet.read(data);
                    server.log("TCP: " + getTime() + " processing " + packet);
                    packet.process(server, socket.getInetAddress(), socket.getPort());
                } catch (IOException e)
                {
                    e.printStackTrace();
                    socket = null;
                }
            }
            server.log("TCP: Stopping tcp-receiver Thread");
        }
    };

    public RemoteClient(Socket socket, NetworkServer server)
    {
        try
        {
            this.socket = socket;
            this.socket.setTcpNoDelay(true);
            this.socket.setTrafficClass(0x10);
            this.socket.setKeepAlive(true);
            this.socket.setReuseAddress(false);
            this.socket.setSoTimeout(10000);
            this.socket.setReceiveBufferSize(Packet.MAX_SIZE);
            this.socket.setSendBufferSize(Packet.MAX_SIZE);
            this.server = server;
            this.sendQueue = new ArrayList<>();
            this.in = socket.getInputStream();
            this.out = socket.getOutputStream();
        } catch (SocketException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void send(Packet packet)
    {
        sendQueue.add(packet);
    }

    public void stop()
    {
        try
        {
            socket.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");//dd/MM/yyyy
        Calendar calendar = Calendar.getInstance();
        String time = dateFormat.format(calendar.getTime());
        return time;
    }

    public Socket getSocket()
    {
        return socket;
    }

    public void start()
    {
        senderThread.start();
        receiverThread.start();
    }
}
