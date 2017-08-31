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

package fr.veridiangames.core.network.packets;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.components.ECName;
import fr.veridiangames.core.game.entities.components.ECNetwork;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.game.entities.player.NetworkedPlayer;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.game.entities.player.ServerPlayer;
import fr.veridiangames.core.maths.Mathf;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.maths.Vec4i;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.NetworkableServer;
import fr.veridiangames.core.utils.DataBuffer;
import fr.veridiangames.core.game.modes.GameMode;

/**
 * Created by Marccspro on 26 f�vr. 2016.
 */
public class ConnectPacket extends Packet
{
	private int id;
	private String name;
	private Vec3 position;
	private Quat rotation;
	private String version;
	private long seed;

	public ConnectPacket()
	{
		super(CONNECT);
	}

	public ConnectPacket(Player player)
	{
		super(CONNECT);
		this.data.put(player.getID());
		this.data.put(player.getName());

		this.data.put(player.getPosition().x);
		this.data.put(player.getPosition().y);
		this.data.put(player.getPosition().z);

		this.data.put(player.getRotation().x);
		this.data.put(player.getRotation().y);
		this.data.put(player.getRotation().z);
		this.data.put(player.getRotation().w);

		this.data.put(GameCore.GAME_SUB_VERSION);

		this.data.put((long) 0);

		this.data.flip();
	}

	public ConnectPacket(ConnectPacket packet)
	{
		super(CONNECT);
		this.data.put(packet.id);
		this.data.put(packet.name);

		this.data.put(packet.position.x);
		this.data.put(packet.position.y);
		this.data.put(packet.position.z);

		this.data.put(packet.rotation.x);
		this.data.put(packet.rotation.y);
		this.data.put(packet.rotation.z);
		this.data.put(packet.rotation.w);

		this.data.put(packet.version);

		this.data.put(packet.seed);

		this.data.flip();
	}

	@Override
	public void read(DataBuffer data)
	{
		this.id = data.getInt();
		this.name = data.getString();
		this.position = new Vec3(data.getFloat(), data.getFloat(), data.getFloat());
		this.rotation = new Quat(data.getFloat(), data.getFloat(), data.getFloat(), data.getFloat());
		this.version = data.getString();
		this.seed = data.getLong();
	}

	@Override
	public void process(NetworkableServer server, InetAddress address, int port)
	{
		this.seed = server.getCore().getGame().getData().getWorldGen().getSeed();
		server.getCore().getGame().spawn(new ServerPlayer(this.id, this.name, this.position, this.rotation, address.getHostName(), port));

		server.getTcp().getClient(address, port).setID(this.id);

		server.log(this.name + " just connected !");
		server.tcpSendToAll(new ConnectPacket(this));

		/* SENDING MULTIPLE PACKETS TO AVOID READ OVERFLOW OF 512 */
		int modifiedBlocksSize = server.getCore().getGame().getWorld().getModifiedBlocks().size();
		int packetCount = (int) ((float) (modifiedBlocksSize * 16) / (Packet.MAX_SIZE - 50)) + 2;
		List<Vec4i> currentData = GameCore.getInstance().getGame().getWorld().getModifiedBlocks();
		int count = (int) ((float) modifiedBlocksSize / (float) packetCount);

//		System.out.println("Modified block size: " + modifiedBlocksSize);
//		System.out.println("Modified block size(Bytes): " + modifiedBlocksSize * 4 * 4);
//		System.out.println("Packet max size: " + Packet.MAX_SIZE);
//		System.out.println("Num packets: " + packetCount);

		boolean finished = false;
		for (int i = 0; i < packetCount + 16; i++)
		{
			List<Vec4i> dataToSend = new ArrayList<>();

			for (int j = 0; j < count; j++)
			{
				int index = i * count + j;
				if (index < currentData.size())
				{
					dataToSend.add(currentData.get(index));
				}
				else
				{
					finished = true;
					break;
				}
			}
			server.tcpSend(new SyncBlocksPacket(dataToSend), address, port);
			if (finished)
				break;
		}

		for (int i = 0; i < server.getCore().getGame().getEntityManager().getNetworkableEntites().size(); i++)
		{
			int id = server.getCore().getGame().getEntityManager().getNetworkableEntites().get(i);
			if (id == this.id)
				continue;
			Entity e = server.getCore().getGame().getEntityManager().getEntities().get(id);
			if (e instanceof Player)
				server.tcpSend(new EntitySyncPacket((Player) e), address, port);
		}

		/* Game Mode managment */
		GameMode mode = server.getCore().getGame().getGameMode();
		mode.onPlayerConnect((Player) server.getCore().getGame().getEntityManager().get(this.id), server);

		// GAME MODE
		GameCore.getInstance().getGame().getGameMode().onPlayerSpawn((Player) GameCore.getInstance().getGame().getEntityManager().get(this.id), server);
		this.position = GameCore.getInstance().getGame().getGameMode().getPlayerSpawn((Player) GameCore.getInstance().getGame().getEntityManager().get(this.id));

		server.tcpSend(new RespawnPacket((Player) GameCore.getInstance().getGame().getEntityManager().get(this.id), this.position), address, port);

		if (!this.version.equals(GameCore.GAME_SUB_VERSION))
		{
			server.log(this.name + " tried to connect with an invalid version: v" + this.version + "  Current: v" + GameCore.GAME_SUB_VERSION);
			server.tcpSendToAll(new KickPacket(this.id, "Invalid game version, please download the latest one: ubercube.github.io"));
			GameCore.getInstance().getGame().remove(this.id);
//			server.getTcp().disconnectClient(address, port);
			return;
		}
	}

	@Override
	public void process(NetworkableClient client, InetAddress address, int port)
	{
		if (client.getCore().getGame().getPlayer().getID() != this.id)
		{
			client.getCore().getGame().spawn(new NetworkedPlayer(this.id, this.name, this.position, this.rotation, address.getHostName(), port));
			client.log(this.name + " just connected !");
		}
		else
		{
			client.log("You just connected as " + this.name);
			client.getCore().getGame().createWorld(this.seed);
			client.setConnected(true);
		}
		client.console(this.name + " just connected !");
	}
}