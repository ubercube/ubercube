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

package fr.veridiangames.core.game.entities.components;

import java.util.ArrayList;
import java.util.List;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.maths.Vec3i;
import fr.veridiangames.core.profiler.Profiler;

/**
 * Created by Marccspro on 14 f�vr. 2016.
 */
public class ECRaycast extends EComponent
{
	private List<String> ignore;

	private float		dist;
	private float		precision;
	private Vec3		position;
	private Vec3		direction;
	private RaycastHit	hit;
	private Vec3 		hitPoint;
	private Profiler	profiler;

	public ECRaycast(float dist, float precision, String... ignoreTags)
	{
		super(RAYCAST);
		this.dist = dist;
		this.precision = precision;
		this.position = new Vec3();
		this.direction = new Vec3();
		this.ignore = new ArrayList<String>();
		for (String s : ignoreTags)
			this.ignore.add(s);
		this.profiler = new Profiler("raycast", true);
	}

	@Override
	public void update(GameCore core)
	{
		this.profiler.start();
		this.hit = null;
		this.hitPoint = null;
		for (float i = 0; i < this.dist; i += this.precision)
		{
			Vec3 point = new Vec3(this.position.copy().add(this.direction.copy().mul(i)));
			int block = core.getGame().getWorld().getBlockAt(point);
			Entity e = core.getGame().getEntityManager().getEntityAt(point);

			if (block != 0)
			{
				this.hit = new RaycastHit(null, block, point.getInts());
				this.hitPoint = point;
				return;
			}

			if (e != null)
			{
				boolean ig = false;
				for (String s : this.ignore)
					if (e.getTags().contains(s))
						ig = true;
				if (ig)
					continue;

				this.hit = new RaycastHit(e, 0, null);
				this.hitPoint = point;
				return;
			}
		}
		this.profiler.end();
	}

	public Vec3 getPosition()
	{
		return this.position;
	}

	public void setPosition(Vec3 position)
	{
		this.position = position;
	}

	public Vec3 getDirection()
	{
		return this.direction;
	}

	public void setDirection(Vec3 direction)
	{
		this.direction = direction;
	}

	public RaycastHit getHit()
	{
		return this.hit;
	}

	public Vec3 getExactHitPoint()
	{
		return this.hitPoint;
	}

	public class RaycastHit
	{
		private int		block;
		private Vec3i	blockPosition;
		private Entity	e;

		public RaycastHit(Entity e, int block, Vec3i blockPosition)
		{
			this.e = e;
			this.block = block;
			this.blockPosition = blockPosition;
		}

		public int getBlock()
		{
			return this.block;
		}

		public Vec3i getBlockPosition()
		{
			return this.blockPosition;
		}

		public Entity getEntity()
		{
			return this.e;
		}
	}
}