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

package fr.veridiangames.core.physics;

import fr.veridiangames.core.maths.Vec3;

/**
 * Created by Marccspro on 22 janv. 2016.
 */
public class CollisionData
{
	private Vec3	normal;
//	private Vec3 	mtd;
//	private boolean	collision;

	private float mtdX;
	private float mtdY;
	private float mtdZ;

	private boolean collision;
	private boolean	collisionX;
	private boolean	collisionY;
	private boolean	collisionZ;

	public CollisionData(boolean collision, boolean collisionX, boolean collisionY, boolean collisionZ)
	{
		this.collision = collision;
		this.collisionX = collisionX;
		this.collisionY = collisionY;
		this.collisionZ = collisionZ;
	}

//	public CollisionData(Vec3 normal, boolean collision)
//	{
//		this.normal = normal;
//		this.collision = collision;
//	}
//
//	public CollisionData() {
//		this(new Vec3(), false);
//	}

	public Vec3 getNormal()
	{
		return this.normal;
	}

	public void setNormal(Vec3 normal)
	{
		this.normal = normal;
	}

	public boolean isCollision()
	{
		return this.collision;
	}
//	public void setCollision(boolean collision)
//	{
//		this.collision = collision;
//	}
//	public Vec3 getMtd()
//	{
//		return mtd;
//	}
//	public void setMtd(Vec3 mtd)
//	{
//		this.mtd = mtd;
//	}

	public float getMtdX()
	{
		return this.mtdX;
	}

	public float getMtdY()
	{
		return this.mtdY;
	}

	public float getMtdZ()
	{
		return this.mtdZ;
	}

	public boolean isCollisionX()
	{
		return this.collisionX;
	}

	public boolean isCollisionY()
	{
		return this.collisionY;
	}

	public boolean isCollisionZ()
	{
		return this.collisionZ;
	}

	public void setMtdX(float mtdX)
	{
		this.mtdX = mtdX;
	}

	public void setMtdY(float mtdY)
	{
		this.mtdY = mtdY;
	}

	public void setMtdZ(float mtdZ)
	{
		this.mtdZ = mtdZ;
	}

	public void setCollisionX(boolean collisionX)
	{
		this.collisionX = collisionX;
	}

	public void setCollisionY(boolean collisionY)
	{
		this.collisionY = collisionY;
	}

	public void setCollisionZ(boolean collisionZ)
	{
		this.collisionZ = collisionZ;
	}
}