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

import fr.veridiangames.core.network.PacketManager;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.server.server.NetworkPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Marc on 05/07/2016.
 */
public class NetworkClientTCP implements Runnable
{
    private NetworkClient   client;
    private int 			id;
    private int				port;
    private InetAddress     address;
    private Socket          socket;

    private DataInputStream in;
    private DataOutputStream out;

    public NetworkClientTCP(NetworkClient client, int id, String address, int port)
    {

        this.client = client;
        this.id = id;
        try
        {
            this.address = InetAddress.getByName(address);
            this.port = port;
            this.socket = new Socket(address, port);
            this.socket.setTcpNoDelay(true);
            log("Connected to the TCP protocol !");
            new Thread(this, "tcp-thread").start();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void run()
    {
        try
        {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            while (true)
            {
                int len = in.readInt();
                byte[] bytes = new byte[len];
                in.readFully(bytes);
                DataBuffer data = new DataBuffer(bytes);
                Packet packet = PacketManager.getPacket(data.getInt());
                packet.read(data);
                packet.process(client, socket.getInetAddress(), socket.getPort());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void send(byte[] bytes)
    {
        new Thread("tcpSend-thread")
        {
            public void run()
            {
                try
                {
                    out.writeInt(bytes.length);
                    out.write(bytes, 0, bytes.length);
                }
                catch (IOException e)
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

    public Socket getSocket()
    {
        return socket;
    }
}
