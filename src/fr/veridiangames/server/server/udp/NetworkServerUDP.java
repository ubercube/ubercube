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

package fr.veridiangames.server.server.udp;

import fr.veridiangames.core.network.PacketManager;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.core.network.NetworkPacket;
import fr.veridiangames.server.server.NetworkServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marc on 05/07/2016.
 */
public class NetworkServerUDP implements Runnable
{
    private NetworkServer server;
    private DatagramSocket socket;

    private List<NetworkPacket> packets;

    public NetworkServerUDP(NetworkServer server, int port)
    {
        try
        {
            this.server = server;
            log("Starting UDP connection");
            this.packets = new ArrayList<>();
            this.socket = new DatagramSocket(port);
            new Thread(this, "udp-thread").start();
        }
        catch (SocketException e)
        {
            log("Server already listening on port: " + port);
            log("Server Failed to connect !");
            log("Terminating udp...");
            System.exit(0);
        }
    }

    public void run()
    {
        while (socket != null)
        {
            try
            {
                byte[] bytes = new byte[Packet.MAX_SIZE];
                DatagramPacket receive = new DatagramPacket(bytes, bytes.length);
                socket.receive(receive);
                DataBuffer data = new DataBuffer(receive.getData());
                int packetID = data.getInt();
                Packet packet = PacketManager.getPacket(packetID);
                if (packet == null)
                    continue;
                packet.read(data);
                packet.process(server, receive.getAddress(), receive.getPort());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void send(byte[] bytes, InetAddress address, int port)
    {
        new Thread("udp-send-thread")
        {
            public void run()
            {
                try
                {
                    socket.send(new DatagramPacket(bytes, bytes.length, address, port));
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
        server.log(msg);
    }

    public void stop()
    {
        log("Closing UDP connection...");
        socket.close();
    }
}
