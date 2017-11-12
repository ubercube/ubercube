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

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.network.PacketManager;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.core.network.NetworkPacket;
import fr.veridiangames.core.utils.Log;
import fr.veridiangames.core.utils.Time;
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

    private Thread thread;
    private boolean running;

    public NetworkServerUDP(NetworkServer server, int port)
    {
        try
        {
            this.server = server;
            this.packets = new ArrayList<>();
            this.socket = new DatagramSocket(port);
            this.thread = new Thread(this, "udp-thread");
            this.running = true;
            this.thread.start();
        }
        catch (SocketException e)
        {
            log("UDP: Server already listening on port: " + port);
            log("UDP: Server Failed to connect !");
            log("UDP: Terminating udp...");
            System.exit(0);
        }
    }

    public void run()
    {
        log("UDP: Connected !");
        while (running && socket != null)
        {
            try
            {
                byte[] bytes = new byte[Packet.MAX_SIZE];
                DatagramPacket receive = new DatagramPacket(bytes, bytes.length);
				socket.receive(receive);
                DataBuffer data = new DataBuffer(receive.getData());
                int packetID = data.getInt();
                Packet packet = PacketManager.getPacket(packetID);
				if (GameCore.isDisplayNetworkDebug())
					server.log("UDP: " + Time.getTime() + " [IN]-> received: " + packet);
                if (packet == null)
                    continue;
                packet.read(data);
                packet.process(server, receive.getAddress(), receive.getPort());
            }
            catch (IOException e)
            {
                Log.exception(e);
            }
        }
		socket.close();
    }

    public void send(byte[] bytes, InetAddress address, int port)
    {
		try
		{
			socket.send(new DatagramPacket(bytes, bytes.length, address, port));
		}
		catch (IOException e)
		{
			Log.exception(e);
		}
    }

    public void log(String msg)
    {
        server.log(msg);
    }

    public void stop()
    {
        log("UDP: Closing connection...");
        running = false;
	}
}
