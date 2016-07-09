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
import fr.veridiangames.server.server.NetworkServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Marc on 07/07/2016.
 */
public class ClientSocket implements Runnable
{
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private NetworkServer server;

    public ClientSocket(Socket socket, NetworkServer server)
    {
        try
        {
            this.socket = socket;
            this.socket.setTcpNoDelay(true);
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
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            while (socket != null)
            {
                try
                {
                    int len = in.readInt();
                    byte[] bytes = new byte[len];
                    in.readFully(bytes);
                    DataBuffer data = new DataBuffer(bytes);
                    int packetID = data.getInt();
                    Packet packet = PacketManager.getPacket(packetID);
                    if (packet == null)
                        continue;
                    System.out.println("receiving: " + packet);
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
            out.writeInt(bytes.length);
            out.write(bytes, 0, bytes.length);
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
