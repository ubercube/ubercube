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

import java.io.IOException;
import java.net.*;

/**
 * Created by Marc on 05/07/2016.
 */
public class NetworkClientUDP implements Runnable
{
    private NetworkClient   client;
    private int 			id;
    private int				port;
    private InetAddress		address;
    private DatagramSocket	socket;

    public NetworkClientUDP(NetworkClient client, int id, String address, int port)
    {
        try
        {
            this.client = client;
            this.id = id;
            this.address = InetAddress.getByName(address);
            this.port = port;
            this.socket = new DatagramSocket(client.getTcp().getSocket().getLocalPort());
            log("Connected to the UDP protocol !");
            new Thread(this, "udp-thread").start();
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
    }

    public void run()
    {
        while (true)
        {
            try
            {
                byte[] bytes = new byte[2048];
                DatagramPacket receive = new DatagramPacket(bytes, bytes.length);
                socket.receive(receive);
                DataBuffer data = new DataBuffer(receive.getData());
                int packetID = data.getInt();
                Packet packet = PacketManager.getPacket(packetID);
                if (packet == null)
                    continue;
                packet.read(data);
                packet.process(client, receive.getAddress(), receive.getPort());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void send(byte[] bytes)
    {
        new Thread("udp-send-thread")
        {
            public void run()
            {
                try
                {
                    DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
                    socket.send(packet);
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
        socket.close();
    }
}
