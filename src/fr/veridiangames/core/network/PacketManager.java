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

package fr.veridiangames.core.network;

import java.util.HashMap;
import java.util.Map;

import fr.veridiangames.core.network.packets.*;

/**
 * Created by Marccspro on 26 f�vr. 2016.
 */
public class PacketManager
{
	private static Map<Integer, Class<? extends Packet>> packets;
	
	static
	{
		packets = new HashMap<>();
		packets.put(Packet.CONNECT, ConnectPacket.class);
		packets.put(Packet.ENTITY_SYNC, EntitySyncPacket.class);
		packets.put(Packet.ENTITY_MOVEMENT, EntityMovementPacket.class);
		packets.put(Packet.DISCONNECT, DisconnectPacket.class);
		packets.put(Packet.BLOCK_ACTION, BlockActionPacket.class);
		packets.put(Packet.BLOCK_SYNC, SyncBlocksPacket.class);
		packets.put(Packet.WEAPON_POS, WeaponPositionPacket.class);
		packets.put(Packet.BULLET_HIT_BLOCK, BulletHitBlockPacket.class);
		packets.put(Packet.PARTICLES_SPAWN, ParticlesSpawnPacket.class);
		packets.put(Packet.PARTICLES_REMOVE, ParticlesRemovePacket.class);
		packets.put(Packet.BULLET_HIT_PLAYER, BulletHitPlayerPacket.class);
		packets.put(Packet.DEATH, DeathPacket.class);
		packets.put(Packet.RESPAWN, RespawnPacket.class);
		packets.put(Packet.PING, PingPacket.class);
		packets.put(Packet.TIME_OUT, TimeoutPacket.class);
		packets.put(Packet.WEAPON_CHANGE, WeaponChangePacket.class);
		packets.put(Packet.BULLET_SHOOT, BulletShootPacket.class);
		packets.put(Packet.KICK, KickPacket.class);
		packets.put(Packet.DAMAGE_FORCE, DamageForcePacket.class);
	}
	
	public static Packet getPacket(int packet)
	{
		try
		{
			if (!packets.containsKey(packet))
				return null;
			return (Packet) packets.get(packet).newInstance();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
