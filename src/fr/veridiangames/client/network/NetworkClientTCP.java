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
import fr.veridiangames.core.utils.Sleep;
import fr.veridiangames.core.network.NetworkPacket;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import static javax.imageio.ImageIO.read;


/**
 * Created by Marc on 05/07/2016.
 */
public class NetworkClientTCP implements Runnable
{
    private NetworkClient client;
    private int id;
    private int port;
    private InetAddress address;
    private Socket socket;

    private DataInputStream in;
    private DataOutputStream out;
    private BufferedInputStream bin;
    private BufferedOutputStream bout;

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
            this.socket.setTrafficClass(0x04);
            this.socket.setKeepAlive(false);
            this.socket.setReuseAddress(false);
            this.socket.setSoTimeout(10000);
            log("Connected to the TCP protocol !");
            new Thread(this, "tcp-thread").start();
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void run()
    {
        try
        {
            bin = new BufferedInputStream(socket.getInputStream());
            bout = new BufferedOutputStream(socket.getOutputStream());
            in = new DataInputStream(bin);
            out = new DataOutputStream(bout);
            while (socket != null)
            {
                try
                {
                    if (bin.available() > 0)
                    {
                        byte[] bytes = DataStream.read(in);
                        DataBuffer data = new DataBuffer(bytes);
                        Packet packet = PacketManager.getPacket(data.getInt());
                        if (packet == null)
                            continue;
                        if (GameCore.isDisplayNetworkDebug())
                            log("[IN] received: " + packet);
                        if (GameCore.isDisplayNetworkDebug())
                            log("[IN]-> received size: " + data.size());
                        packet.read(data);
                        packet.process(client, socket.getInetAddress(), socket.getPort());
                    }
                } catch (IOException e)
                {
                    socket = null;
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void send(byte[] bytes)
    {
        if (out == null)
            return;
        try
        {
            if (bytes.length == 0)
                return;
            if (GameCore.isDisplayNetworkDebug())
                log("[OUT]-> sending size: " + bytes.length);
            DataStream.write(out, bytes);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
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
