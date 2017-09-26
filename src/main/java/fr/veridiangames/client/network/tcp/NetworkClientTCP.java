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

import fr.veridiangames.client.network.NetworkClient;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.utils.Log;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ConcurrentLinkedQueue;


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
            log("TCP: Connecting");
            this.socket = new Socket(address, port);
			this.socket.setTcpNoDelay(true);
            this.socket.setTrafficClass(0x10);
            this.socket.setKeepAlive(false);
            this.socket.setReuseAddress(false);
            this.socket.setSoTimeout(0);
            this.socket.setSoLinger(false,0);

            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            this.receiver = new TCPReceiverThread(this);
            this.sender = new TCPSenderThread(this);
            this.sendQueue = new ConcurrentLinkedQueue<>();
            log("TCP: Connected !");

            receiver.start();
            sender.start();

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
		sendQueue.add(packet);
    }

    public void log(String msg)
    {
        client.log(msg);
    }

    public void close()
    {
		receiver.stopRunning();
		sender.stopRunning();
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			Log.exception(e);
		}
	}

    private String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");//dd/MM/yyyy
        Calendar calendar = Calendar.getInstance();
        String time = dateFormat.format(calendar.getTime());
        return time;
    }

    public Socket getSocket()
    {
        return socket;
    }

	public ConcurrentLinkedQueue<Packet> getSendQueue() {
		return sendQueue;
	}

	public DataInputStream getIn() {
		return in;
	}

	public DataOutputStream getOut() {
		return out;
	}

	public InetAddress getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public NetworkClient getClient() {
		return client;
	}
}
