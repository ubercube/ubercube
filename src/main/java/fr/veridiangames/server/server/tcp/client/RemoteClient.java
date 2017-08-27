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

package fr.veridiangames.server.server.tcp.client;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.network.PacketManager;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.core.utils.DataStream;
import fr.veridiangames.server.server.NetworkServer;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedQueue;

import static fr.veridiangames.core.utils.Time.getTime;

/**
 * Created by Marc on 07/07/2016.
 */
public class RemoteClient
{
    private Socket          				socket;
    private InputStream     				in;
    private OutputStream    				out;
    private NetworkServer   				server;
    private ConcurrentLinkedQueue<Packet> 	sendQueue;
    private int             				id;
    private RemoteClientReceiver			receiver;
    private RemoteClientSender				sender;

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
			this.server = server;
			this.receiver = new RemoteClientReceiver(this);
			this.sender = new RemoteClientSender(this);
			this.sendQueue = new ConcurrentLinkedQueue<>();
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
		sendQueue.add(packet);
	}

	public RemoteClient start()
	{
		this.receiver.start();
		this.sender.start();
		return this;
	}

    public void stop()
    {
        try
        {
			socket.close();
        }
        catch (IOException e)
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

    public InputStream getInputStream()
    {
        return in;
    }

    public int getId() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

	public NetworkServer getServer() {
		return server;
	}

	public ConcurrentLinkedQueue<Packet> getSendingQueue()
	{
		return sendQueue;
	}

	public InputStream getIn() {
		return in;
	}

	public OutputStream getOut() {
		return out;
	}
}
