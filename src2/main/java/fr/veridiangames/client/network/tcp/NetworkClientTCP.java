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

package fr.veridiangames.client.network.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedQueue;

import fr.veridiangames.client.network.NetworkClient;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.Log;


/**
 * Created by Marc on 05/07/2016.
 */
public class NetworkClientTCP
{
    private NetworkClient client;
    private int port;
    private InetAddress address;
    private Socket socket;

    private DataInputStream  in;
    private DataOutputStream out;

    private TCPReceiverThread receiver;
    private TCPSenderThread sender;

    private ConcurrentLinkedQueue<Packet> sendQueue;

    public NetworkClientTCP(NetworkClient client, String address, int port)
    {
        this.client = client;
        try
        {
            this.address = InetAddress.getByName(address);
            this.port = port;
            this.log("TCP: Connecting");
            this.socket = new Socket(address, port);
			this.socket.setTcpNoDelay(true);
            this.socket.setTrafficClass(0x10);
            this.socket.setKeepAlive(false);
            this.socket.setReuseAddress(false);
            this.socket.setSoTimeout(60000);
            this.socket.setSoLinger(false,0);

            this.in = new DataInputStream(this.socket.getInputStream());
            this.out = new DataOutputStream(this.socket.getOutputStream());

            this.receiver = new TCPReceiverThread(this);
            this.sender = new TCPSenderThread(this);
            this.sendQueue = new ConcurrentLinkedQueue<>();
            this.log("TCP: Connected !");

            this.receiver.start();
            this.sender.start();

        } catch (UnknownHostException e)
        {
            Log.exception(e);
        } catch (IOException e)
        {
            Log.exception(e);
        }
    }

    public void send(Packet packet)
    {
		this.sendQueue.add(packet);
    }

    public void log(String msg)
    {
        this.client.log(msg);
    }

    public void close()
    {
		this.receiver.stopRunning();
		this.sender.stopRunning();
		try {
			this.in.close();
			this.out.close();
			this.socket.close();
		} catch (IOException e) {
			Log.exception(e);
		}
	}

    public Socket getSocket()
    {
        return this.socket;
    }

	public ConcurrentLinkedQueue<Packet> getSendQueue() {
		return this.sendQueue;
	}

	public DataInputStream getIn() {
		return this.in;
	}

	public DataOutputStream getOut() {
		return this.out;
	}

	public InetAddress getAddress() {
		return this.address;
	}

	public int getPort() {
		return this.port;
	}

	public NetworkClient getClient() {
		return this.client;
	}
}
