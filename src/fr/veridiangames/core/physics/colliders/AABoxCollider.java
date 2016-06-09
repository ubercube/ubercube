/*
 *   Copyright (C) 2016 Team Ubercube
 *
 *   This file is part of Ubercube.
 *
 *       Ubercube is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Ubercube is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Ubercube.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.veridiangames.core.physics.colliders;

import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.physics.CollisionData;
import fr.veridiangames.core.physics.CollisionDetector;
import fr.veridiangames.core.physics.CollisionHandler;

/**
 * Created by Marccspro on 22 janv. 2016.
 */
public class AABoxCollider extends Collider
{
	private Vec3 size;

	public AABoxCollider(Vec3 size)
	{
		super();
		this.size = size;
	}
	
	public AABoxCollider()
	{
		this(new Vec3(0.5f, 0.5f, 0.5f));
	}
	
	public CollisionData getCollisionData(Collider collider)
	{
		CollisionData result = new CollisionData();
		result.setCollision(CollisionDetector.detectAABBvsAABB(this,(AABoxCollider) collider));
		if (result.isCollision())
		{
			Vec3 normal = new Vec3(position).gtNorm(collider.getPosition());
			result.setNormal(normal);
			
			Vec3 mtd = CollisionHandler.mtdAABBvsAABB(this, (AABoxCollider) collider);
			result.setMtd(mtd);
		}
		return result;
	}
	

	
	public Vec3 getSize()
	{
		return size;
	}

	public void setSize(Vec3 size)
	{
		this.size = size;
	}
}