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

import fr.veridiangames.core.network.packets.ApplyDamagePacket;
import fr.veridiangames.core.network.packets.BlockActionPacket;
import fr.veridiangames.core.network.packets.BulletHitBlockPacket;
import fr.veridiangames.core.network.packets.BulletHitPlayerPacket;
import fr.veridiangames.core.network.packets.BulletShootPacket;
import fr.veridiangames.core.network.packets.ConnectPacket;
import fr.veridiangames.core.network.packets.DamageForcePacket;
import fr.veridiangames.core.network.packets.DeathPacket;
import fr.veridiangames.core.network.packets.DisconnectPacket;
import fr.veridiangames.core.network.packets.EntityMovementPacket;
import fr.veridiangames.core.network.packets.EntitySyncPacket;
import fr.veridiangames.core.network.packets.GrenadeSpawnPacket;
import fr.veridiangames.core.network.packets.KickPacket;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.core.network.packets.ParticlesRemovePacket;
import fr.veridiangames.core.network.packets.ParticlesSpawnPacket;
import fr.veridiangames.core.network.packets.PingPacket;
import fr.veridiangames.core.network.packets.RespawnPacket;
import fr.veridiangames.core.network.packets.SoundPacket;
import fr.veridiangames.core.network.packets.SyncBlocksPacket;
import fr.veridiangames.core.network.packets.TchatMsgPacket;
import fr.veridiangames.core.network.packets.TimeoutPacket;
import fr.veridiangames.core.network.packets.WeaponChangePacket;
import fr.veridiangames.core.network.packets.WeaponPositionPacket;
import fr.veridiangames.core.network.packets.gamemode.tdm.TDMScorePacket;
import fr.veridiangames.core.network.packets.gamemode.tdm.TDMSpawnPacket;
import fr.veridiangames.core.network.packets.gamemode.tdm.TDMTeamPacket;
import fr.veridiangames.core.utils.Log;

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
		packets.put(Packet.TCHAT_MSG, TchatMsgPacket.class);
		packets.put(Packet.AUDIO, SoundPacket.class);
		packets.put(Packet.GRENADE_SPAWN, GrenadeSpawnPacket.class);
		packets.put(Packet.APPLY_DAMAGE, ApplyDamagePacket.class);

		/* GAME MODES */
		packets.put(Packet.GAMEMODE_TDM_SPAWN, TDMSpawnPacket.class);
		packets.put(Packet.GAMEMODE_TDM_SCORE, TDMScorePacket.class);
		packets.put(Packet.GAMEMODE_TDM_TEAM, TDMTeamPacket.class);
	}

	public static Packet getPacket(int packet)
	{
		try
		{
			if (!packets.containsKey(packet))
			{
				Log.println("Packets does not contain packet ID: " + packet + ", packets size: " + packets.size());
				return null;
			}
			return packets.get(packet).newInstance();
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			Log.exception(e);
			return null;
		}
	}
}
