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

package fr.veridiangames.core.physics.colliders;

import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.maths.Vec3i;
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

		boolean collision = CollisionDetector.detectAABBvsAABB(this,(AABoxCollider) collider);
		boolean xCollision = CollisionDetector.detectAxisX(this,(AABoxCollider) collider);
		boolean yCollision = CollisionDetector.detectAxisY(this,(AABoxCollider) collider);
		boolean zCollision = CollisionDetector.detectAxisZ(this,(AABoxCollider) collider);

		CollisionData data = new CollisionData(collision, xCollision, yCollision, zCollision);
		Vec3 normal = new Vec3();
		if (collision)
		{
			if (xCollision)
			{
				float mtd = CollisionHandler.mtdAxisX(this,(AABoxCollider) collider);
				data.setMtdX(mtd);
				if (mtd != 0)
					normal.x = mtd > 0 ? 1 : -1;
			}
			if (yCollision)
			{
				float mtd = CollisionHandler.mtdAxisY(this,(AABoxCollider) collider);
				data.setMtdY(mtd);
				if (mtd != 0)
					normal.y = mtd > 0 ? 1 : -1;
			}
			if (zCollision)
			{
				float mtd = CollisionHandler.mtdAxisZ(this,(AABoxCollider) collider);
				data.setMtdZ(mtd);
				if (mtd != 0)
					normal.z = mtd > 0 ? 1 : -1;
			}
			data.setNormal(normal);
		}

//		data.setCollision(CollisionDetector.detectAABBvsAABB(this,(AABoxCollider) collider));
//		if (data.isCollision())
//		{
//			Vec3 normal = new Vec3(position).gtNorm(collider.getPosition());
//			data.setNormal(normal);
//
//			Vec3 mtd = CollisionHandler.mtdAABBvsAABB(this, (AABoxCollider) collider);
//			data.setMtd(mtd);
//
//		}

		return data;
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