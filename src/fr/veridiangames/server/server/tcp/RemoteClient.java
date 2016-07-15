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
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataStream;
import fr.veridiangames.server.server.NetworkServer;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
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
            //this.socket.setReceiveBufferSize(Packet.MAX_SIZE);
            //this.socket.setSendBufferSize(Packet.MAX_SIZE);
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
       new Thread() {

           public void run()
           {
               if (socket == null || out == null)
               {
                   server.getTcp().disconnectClient(socket.getInetAddress(), socket.getPort());
               }

               try
               {
                   if (packet == null)
                   {
                       server.getTcp().log ("TCP: " + server.getTcp().getTime() + " [ERROR]-> Tried to send a null packet");
                       return;
                   }

                   if (packet.getData() == null)
                   {
                       server.getTcp().log("TCP: " + server.getTcp().getTime() + " [ERROR]-> Tried to send an empty packet");
                       server.getTcp().log("TCP: " + server.getTcp().getTime() + " [ERROR]-> " + packet);
                       return;
                   }

                   byte[] bytes = packet.getData().getData();

                   if (bytes.length == 0)
                   {
                       server.getTcp().log("TCP: " + server.getTcp().getTime() + " [ERROR]-> Tried to send an empty packet");
                       return;
                   }

                   if (GameCore.isDisplayNetworkDebug())
                       server.getTcp().log("TCP: " + server.getTcp().getTime() + " [OUT]-> sending: " + packet);

                   DataStream.writePacket(out, bytes);
               } catch (IOException e)
               {
                   server.getTcp().disconnectClient(socket.getInetAddress(), socket.getPort());
                   e.printStackTrace();
               }
           }
       }.start();
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

    public OutputStream getOutputStream()
    {
        return out;
    }

    public List<Packet> getSendQueue()
    {
        return sendQueue;
    }

    public InputStream getInputStream()
    {
        return in;
    }
}
