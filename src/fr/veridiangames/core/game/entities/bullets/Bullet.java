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
import fr.veridiangames.core.game.entities.components.ECName;
import fr.veridiangames.core.game.entities.components.ECRender;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.game.entities.particles.ParticleSystem;
import fr.veridiangames.core.game.entities.particles.ParticlesBlood;
import fr.veridiangames.core.game.entities.particles.ParticlesBulletHit;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.maths.Vec3i;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.packets.BulletHitBlockPacket;
import fr.veridiangames.core.network.packets.BulletHitPlayerPacket;
import fr.veridiangames.core.utils.Indexer;

/**
 * Created by Marccspro on 18 mai 2016.
 */
public class Bullet extends Entity
{
	private float				force;
	private NetworkableClient	net;
	private Vec3 startPosition = new Vec3();

	public Bullet(int id, String name, Vec3 spawnPoint, Quat orientation, float force)
	{
		super(id);
		super.add(new ECName(name));
		super.add(new ECRender(spawnPoint, orientation, new Vec3(0.04f, 0.04f, 0.4f)));
		super.addTag("Bullet");

		startPosition.set(this.getPosition());

		this.force = force;
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
			Vec3 stepedPosition = new Vec3(position).add(direction.copy().mul(force / step));
			block = core.getGame().getWorld().getBlockAt(stepedPosition);
			blockPosition = stepedPosition.copy();
			e = core.getGame().getEntityManager().getEntityAt(stepedPosition, "NetPlayer");
			if (block == 0 && e == null)
			{
				position.set(stepedPosition);
			}
		}

		Vec3 v = new Vec3(getPosition());
		v.sub(startPosition);
		float distance = Math.abs(v.sqrt());
		if(distance > 1000){
			this.destroy();
		}

		if (block != 0)
		{
			Vec3i impactPosition = new Vec3i(position);
			Vec3 normal = new Vec3(impactPosition).gtNorm(position);

			this.net.send(new BulletHitBlockPacket(new Vec3i(blockPosition), 0.1f, block));

			ParticleSystem hitParticles = new ParticlesBulletHit(Indexer.getUniqueID(), getPosition().copy());
			hitParticles.setNetwork(net);

			this.destroy();
		}
		if (e != null)
		{
			ParticleSystem blood = new ParticlesBlood(Indexer.getUniqueID(), getPosition().copy());
			blood.setParticleVelocity(getRotation().getBack().copy().mul(0.02f));
			blood.setNetwork(net);
			this.net.send(new BulletHitPlayerPacket((Player) e));
			this.destroy();
		}
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
	
	public float getForce()
	{
		return force;
	}
}
