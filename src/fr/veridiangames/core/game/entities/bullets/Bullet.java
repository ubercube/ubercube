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

package fr.veridiangames.core.game.entities.bullets;

import fr.veridiangames.client.Ubercube;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.components.ECAudioSource;
import fr.veridiangames.core.game.entities.components.ECName;
import fr.veridiangames.core.game.entities.components.ECRender;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.game.entities.particles.*;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.game.world.Chunk;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.maths.Vec3i;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.packets.BulletHitBlockPacket;
import fr.veridiangames.core.network.packets.BulletHitPlayerPacket;
import fr.veridiangames.core.network.packets.DamageForcePacket;
import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.core.utils.Indexer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Marccspro on 18 mai 2016.
 */
public class Bullet extends Entity
{
	public enum BulletType
	{
		NORMAL(0), EXPlOSIVE(1), INCENDIARY(2);

		private static Map map = new HashMap<>();
		private final int i;

		static {
			for (BulletType bulletType : BulletType.values()) {
				map.put(bulletType.getValue(), bulletType);
			}
		}

		BulletType(int i)
		{
			this.i = i;
		}

		public static BulletType valueOf(int bulletType) {
			return (BulletType) map.get(bulletType);
		}

		public int getValue()
		{
			return i;
		}
	}

	private int 				holderID;
	private float				force;
	private BulletType 			bulletType;
	private NetworkableClient	net;
	private Vec3 				startPosition;
	private ParticleSystem 		smokeParticles;

	public Bullet(int id, int holderID, String name, Vec3 spawnPoint, Quat orientation, float force, BulletType bulletType)
	{
		super(id);
		super.add(new ECName(name));
		super.add(new ECRender(spawnPoint, orientation, new Vec3(0.04f, 0.04f, 0.4f)));
		super.addTag("Bullet");

		this.holderID = holderID;
		this.force = force;
		this.bulletType = bulletType;
		this.startPosition = new Vec3(this.getPosition());

		ECAudioSource audioSource = new ECAudioSource();

		if (bulletType == BulletType.EXPlOSIVE)
		{
			smokeParticles = new ParticlesSmoke(Indexer.getUniqueID(), getPosition().copy(), getRotation().getForward());
			Ubercube.getInstance().getGameCore().getGame().spawn(smokeParticles);
		}

//		audioSource.setPosition(spawnPoint);
//		audioSource.setVelocity(new Vec3(0, 0, 0));
//		audioSource.setSound(Sound.AK47_SHOOT);
//		audioSource.setLoop(false);
//		audioSource.play();

		super.add(audioSource);
	}

	public void update(GameCore core)
	{
		super.update(core);
		if (destroyed)
			return;
		Vec3 position = getPosition();
		Vec3 direction = getRotation().getForward();

		int block = 0;
		Vec3 blockPosition = new Vec3();
		Entity e = null;
		
		int step = (int) (force * 30.0f);
		for (int i = 0; i < step; i++)
		{
			Vec3 steppedPosition = new Vec3(position).add(direction.copy().mul(force / step));
			block = core.getGame().getWorld().getBlockAt(steppedPosition);
			blockPosition = steppedPosition.copy();
			e = core.getGame().getEntityManager().getEntityAt(steppedPosition, "NetPlayer");
			if (e != null)
				if (e.getID() == holderID)
					e = null;
			if (block == 0 && e == null)
			{
				position.set(steppedPosition);
			}
		}

		if (smokeParticles != null)
			smokeParticles.setPosition(position.copy());

		if(isBulletOutOfMap(core))
			this.destroy();

		if (block != 0)
		{
			Vec3i impactPosition = new Vec3i(position);
			Vec3 normal = new Vec3(impactPosition).gtNorm(position);

			if (bulletType == BulletType.EXPlOSIVE)
			{
				getCore().getGame().spawn(new ParticlesExplosion(Indexer.getUniqueID(), getPosition().copy()));

				if (holderID == core.getGame().getPlayer().getID())
					net.tcpSend(new DamageForcePacket(getPosition().copy(), 2 + (float) Math.random() * 1.5f));

				core.getGame().getWorld().explosion(getPosition(), 1f);
			} else if (holderID == core.getGame().getPlayer().getID()) {
				this.net.tcpSend(new BulletHitBlockPacket(holderID, new Vec3i(blockPosition), 0.1f, block));
			}

			ParticleSystem hitParticles = new ParticlesBulletHit(Indexer.getUniqueID(), getPosition().copy(), new Color4f(block));
			core.getGame().spawn(hitParticles);

			this.destroy();
		}
		if (e != null)
		{
			ParticleSystem blood = new ParticlesBlood(Indexer.getUniqueID(), getPosition().copy());
			blood.setParticleVelocity(getRotation().getBack().copy().mul(0.02f));
			core.getGame().spawn(blood);
			Player player = (Player) e;

			if (holderID == core.getGame().getPlayer().getID())
				this.net.tcpSend(new BulletHitPlayerPacket(player));

			this.destroy();
		}
	}

	public boolean isBulletOutOfMap(GameCore core)
	{
		int m0 = 0;
		int m1 = core.getGame().getData().getWorldSize() * Chunk.SIZE;
		Vec3 p = getPosition();

		if (p.x < m0 ||p.z < m0 || p.x > m1 || p.z > m1)
			return true;
		if (p.y < 0 || p.y > 100)
			return true;

		return false;
	}

	public Vec3 getPosition()
	{
		return ((ECRender) this.get(EComponent.RENDER)).getTransform().getPosition();
	}

	public Quat getRotation()
	{
		return ((ECRender) this.get(EComponent.RENDER)).getTransform().getRotation();
	}
	
	public void setNetwork(NetworkableClient net)
	{
		this.net = net;
	}

	public String getName()
	{
		return ((ECName) this.get(EComponent.NAME)).getName();
	}

	public ECAudioSource getAudioSource()
	{
		return (ECAudioSource) this.get(EComponent.AUDIO_SOURCE);
	}
	
	public float getForce()
	{
		return force;
	}

	public BulletType getBulletType() {
		return bulletType;
	}

	public void destroy() {
		super.destroy();

		if (smokeParticles != null)
			smokeParticles.setActivate(false);
	}
}
