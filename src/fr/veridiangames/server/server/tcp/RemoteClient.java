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

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.network.PacketManager;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.core.utils.DataStream;
import fr.veridiangames.core.utils.Sleep;
import fr.veridiangames.server.server.NetworkServer;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marc on 07/07/2016.
 */
public class RemoteClient implements Runnable
{
    private Socket socket;

    private BufferedInputStream bin;
    private BufferedOutputStream bout;
    private DataInputStream in;
    private DataOutputStream out;

    private NetworkServer server;

    private List<Packet> packets;

    public RemoteClient(Socket socket, NetworkServer server)
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
            this.packets = new ArrayList<>();
        } catch (SocketException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void run()
    {
        processPackets();
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
                        if (GameCore.isDisplayNetworkDebug())
                            System.out.println("Receiving something...");
                        byte[] bytes = DataStream.read(in);
                        DataBuffer data = new DataBuffer(bytes);
                        int packetID = data.getInt();
                        Packet packet = PacketManager.getPacket(packetID);
                        if (packet == null)
                            continue;
                        if (GameCore.isDisplayNetworkDebug())
                            System.out.println("[IN]-> received: " + packet);
                        packet.read(data);
                        packets.add(packet);
                    }
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

    private void processPackets()
    {
        new Thread("tcp-process-thread")
        {
            public void run()
            {
                while (true)
                {
                    Sleep.sleep(100);
                    for (int i = 0; i < packets.size(); i++)
                    {
                        packets.get(i).process(server, socket.getInetAddress(), socket.getPort());
                        packets.remove(i);
                    }
                }
            }
        }.start();
    }

    public void send(byte[] bytes)
    {
        new Thread("tcp-send-thread")
        {
            public void run()
            {
                try
                {
                    if (bytes.length == 0)
                        return;
                    if (GameCore.isDisplayNetworkDebug())
                        System.out.println("[OUT] -> Sending size: " + bytes.length);
                    DataStream.write(out, bytes);
                }
                catch (IOException e)
                {
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

    public void start()
    {
        new Thread(this).start();
    }
}
