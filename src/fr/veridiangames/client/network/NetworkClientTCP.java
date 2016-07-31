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

package fr.veridiangames.client.network;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.network.PacketManager;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.core.utils.DataStream;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Marc on 05/07/2016.
 */
public class NetworkClientTCP
{
    private NetworkClient client;
    private int port;
    private InetAddress address;
    private Socket socket;

    private InputStream  in;
    private OutputStream out;

    private Thread receiverThread = new Thread("tcp-receiver") {
        public void run() {
            log("TCP: Starting tcp-receiver");
            while (socket != null)
            {
                try
                {
                    byte[] bytes = DataStream.readPacket(in);
                    DataBuffer data = new DataBuffer(bytes);
                    int id = data.getInt();
                    Packet packet = PacketManager.getPacket(id);

                    if (packet == null)
                    {
                        log("TCP: " + getTime() + " [ERROR]-> Received wrong packet id " + id);
                        System.exit(1);
                    }

                    if (GameCore.isDisplayNetworkDebug() && !packet.getClass().getSimpleName().equals("PingPacket"))
                        log("TCP: " + getTime() + " [IN] -> " + packet.getClass().getSimpleName());

                    packet.read(data);
                    packet.process(client, address, port);
                } catch (IOException e)
                {
                    e.printStackTrace();
                    socket = null;
                }
            }
            log("TCP: Stopping tcp-receiver Thread");
        }
    };

    public NetworkClientTCP(NetworkClient client, String address, int port)
    {
        this.client = client;
        try
        {
            this.address = InetAddress.getByName(address);
            this.port = port;
            this.socket = new Socket(address, port);
            this.socket.setTcpNoDelay(true);
            this.socket.setTrafficClass(0x10);
            this.socket.setKeepAlive(false);
            this.socket.setReuseAddress(false);
            this.socket.setSoTimeout(10000);
            this.in = socket.getInputStream();
            this.out = socket.getOutputStream();
            log("TCP: Connected !");
            receiverThread.start();
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void send(Packet packet)
    {
        new Thread() {
            public void run() {
                try
                {
                    if (packet.getData() == null)
                    {
                        log ("TCP: " + getTime() + " [ERROR]-> Tried to send an empty packet");
                        log ("TCP: " + getTime() + " [ERROR]-> " + packet.getClass().getSimpleName());
                        return;
                    }

                    byte[] bytes = packet.getData().getData();

                    if (bytes.length == 0)
                    {
                        log ("TCP: " + getTime() + " [ERROR]-> Tried to send an empty packet");
                        log ("TCP: " + getTime() + " [ERROR]-> " + packet.getClass().getSimpleName());
                        return;
                    }

                    if (GameCore.isDisplayNetworkDebug() && !packet.getClass().getSimpleName().equals("PingPacket"))
                        log("TCP: " + getTime() + " [OUT]-> " + packet.getClass().getSimpleName());

                    DataStream.writePacket(out, bytes);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void log(String msg)
    {
        client.log(msg);
    }

    public void close()
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

}
