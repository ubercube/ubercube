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

package fr.veridiangames.core.physics.colliders;

import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.physics.CollisionData;

/**
 * Created by Marccspro on 22 janv. 2016.
 */
public abstract class Collider
{
	protected Vec3	position;
	protected Quat	rotation;

	public Collider(Vec3 position, Quat rotation)
	{
		this.position = position;
		this.rotation = rotation;
	}
	
	public Collider()
	{
		this(new Vec3(), new Quat());
	}
	
	public abstract CollisionData getCollisionData(Collider collider);

	public Vec3 getPosition()
	{
		return position;
	}

	public void setPosition(Vec3 position)
	{
		this.position = position;
	}

	public Quat getRotation()
	{
		return rotation;
	}

	public void setRotation(Quat rotation)
	{
		this.rotation = rotation;
	}
}