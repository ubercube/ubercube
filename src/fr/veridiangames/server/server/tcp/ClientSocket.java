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

import fr.veridiangames.core.network.PacketManager;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.core.utils.DataStream;
import fr.veridiangames.server.server.NetworkServer;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Marc on 07/07/2016.
 */
public class ClientSocket implements Runnable
{
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private NetworkServer server;

    public ClientSocket(Socket socket, NetworkServer server)
    {
        try
        {
            this.socket = socket;
            this.socket.setTcpNoDelay(true);
            this.socket.setTrafficClass(0x04);
            this.socket.setKeepAlive(true);
            this.socket.setReuseAddress(false);
            this.socket.setSoTimeout(10000);
            this.server = server;
        } catch (SocketException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void run()
    {
        try
        {
            in = socket.getInputStream();
            out = socket.getOutputStream();

            while (socket != null)
            {
                try
                {
                    System.out.println("Receiving something...");
                    byte[] bytes = DataStream.read(in);
                    DataBuffer data = new DataBuffer(bytes);

                    int packetID = data.getInt();
                    Packet packet = PacketManager.getPacket(packetID);
                    if (packet == null)
                        continue;
                    System.out.println("[IN]-> received: " + packet);
                    packet.read(data);
                    packet.process(server, socket.getInetAddress(), socket.getPort());
                }
                catch (IOException e)
                {
                    socket = null;
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void send(byte[] bytes)
    {
        try
        {
            if (bytes.length == 0)
                return;

            System.out.println("[OUT] -> Sending size: " + bytes.length);
            DataStream.write(out, bytes);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
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

    public Socket getSocket()
    {
        return socket;
    }

    public void start()
    {
        new Thread(this).start();
    }
}
