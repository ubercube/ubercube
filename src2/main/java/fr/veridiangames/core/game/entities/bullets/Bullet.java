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

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.components.ECAudioSource;
import fr.veridiangames.core.game.entities.components.ECName;
import fr.veridiangames.core.game.entities.components.ECRender;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.game.entities.particles.ParticleSystem;
import fr.veridiangames.core.game.entities.particles.ParticlesBlood;
import fr.veridiangames.core.game.entities.particles.ParticlesBulletHit;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.game.world.Chunk;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.maths.Vec3i;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.Protocol;
import fr.veridiangames.core.network.packets.BulletHitBlockPacket;
import fr.veridiangames.core.network.packets.BulletHitPlayerPacket;
import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.core.utils.Indexer;

/**
 * Created by Marccspro on 18 mai 2016.
 */
public class Bullet extends Entity
{
	private int holderID;
	private float				force;
	private NetworkableClient	net;
	private Vec3 startPosition = new Vec3();

	public Bullet(int id, int holderID, String name, Vec3 spawnPoint, Quat orientation, float force)
	{
		super(id);
		super.add(new ECName(name));
		super.add(new ECRender(spawnPoint, orientation, new Vec3(0.04f, 0.04f, 0.4f)));
		this.holderID = holderID;
		ECAudioSource audioSource = new ECAudioSource();
//		audioSource.setPosition(spawnPoint);
//		audioSource.setVelocity(new Vec3(0, 0, 0));
//		audioSource.setSound(Sound.AK47_SHOOT);
//		audioSource.setLoop(false);
//		audioSource.play();

		super.add(audioSource);
		super.addTag("Bullet");

		this.startPosition.set(this.getPosition());

		this.force = force;
	}

	@Override
	public void update(GameCore core)
	{
		super.update(core);
		if (this.destroyed)
			return;
		Vec3 position = this.getPosition();
		Vec3 direction = this.getRotation().getForward();

		int block = 0;
		Vec3 blockPosition = new Vec3();
		Entity e = null;

		int step = (int) (this.force * 30.0f);
		for (int i = 0; i < step; i++)
		{
			Vec3 stepedPosition = new Vec3(position).add(direction.copy().mul(this.force / step));
			block = core.getGame().getWorld().getBlockAt(stepedPosition);
			blockPosition = stepedPosition.copy();
			e = core.getGame().getEntityManager().getEntityAt(stepedPosition, "NetPlayer");
			if (e != null)
				if (e.getID() == this.holderID)
					e = null;
			if (block == 0 && e == null)
				position.set(stepedPosition);
		}

		if(this.isBulletOutOfMap(core))
			this.destroy();

		if (block != 0)
		{
			Vec3i impactPosition = new Vec3i(position);
			new Vec3(impactPosition).gtNorm(position);

			if(this.holderID == GameCore.getInstance().getGame().getPlayer().getID())
				this.net.send(new BulletHitBlockPacket(this.holderID, new Vec3i(blockPosition), 0.1f, block), Protocol.TCP);

			ParticleSystem hitParticles = new ParticlesBulletHit(Indexer.getUniqueID(), this.getPosition().copy(), new Color4f(block));
			GameCore.getInstance().getGame().spawn(hitParticles);

			this.destroy();
		}
		if (e != null)
		{
			ParticleSystem blood = new ParticlesBlood(Indexer.getUniqueID(), this.getPosition().copy());
			blood.setParticleVelocity(this.getRotation().getBack().copy().mul(0.02f));
			blood.setNetwork(this.net);
			Player player = (Player) e;
			this.net.send(new BulletHitPlayerPacket(player), Protocol.TCP);
			this.destroy();
		}
	}

	public boolean isBulletOutOfMap(GameCore core)
	{
		int m0 = 0;
		int m1 = core.getGame().getData().getWorldSize() * Chunk.SIZE;
		Vec3 p = this.getPosition();

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
		return this.force;
	}
}
