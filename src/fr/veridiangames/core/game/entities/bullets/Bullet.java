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
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.packets.BulletHitPacket;

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
		Entity e = null;
		
		int step = (int) (force * 30.0f);
		for (int i = 0; i < step; i++)
		{
			Vec3 stepedPosition = new Vec3(position).add(direction.copy().mul(force / step));
			block = core.getGame().getWorld().getBlockAt(stepedPosition);
			e = core.getGame().getEntityManager().getEntityAt(stepedPosition);
			if (block == 0 || e == null)
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
			this.net.send(new BulletHitPacket(new StaticBullet(id, "", position, getRotation())));
			this.destroy();
		}
		if (e != null)
		{
			if (e instanceof Player)
			{
				this.destroy();
			}
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
