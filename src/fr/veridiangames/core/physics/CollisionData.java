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
 *     along with Ubercube.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.veridiangames.core.physics;

import fr.veridiangames.core.maths.Vec3;

/**
 * Created by Marccspro on 22 janv. 2016.
 */
public class CollisionData
{
	private Vec3	normal;
	private Vec3 	mtd;
	private boolean	collision;

	public CollisionData(Vec3 normal, boolean collision)
	{
		this.normal = normal;
		this.collision = collision;
	}
	
	public CollisionData() {
		this(new Vec3(), false);
	}
	
	public Vec3 getNormal()
	{
		return normal;
	}

	public void setNormal(Vec3 normal)
	{
		this.normal = normal;
	}

	public boolean isCollision()
	{
		return collision;
	}

	public void setCollision(boolean collision)
	{
		this.collision = collision;
	}

	public Vec3 getMtd()
	{
		return mtd;
	}

	public void setMtd(Vec3 mtd)
	{
		this.mtd = mtd;
	}
}