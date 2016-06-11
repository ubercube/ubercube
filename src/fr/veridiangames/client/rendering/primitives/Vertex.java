/*
 * Copyright (C) 2016 Team Ubercube
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
 *       along with Ubercube.  If not, see http://www.gnu.org/licenses/.
 */

package fr.veridiangames.client.rendering.primitives;

import fr.veridiangames.core.maths.Vec3;

public class Vertex
{
	private Vec3 position;
	private Vec3 normal;
	private Vec3 color;
	
	public Vertex(Vec3 pos)
	{
		this(pos, new Vec3(), new Vec3());
	}
	
	public Vertex(Vec3 position, Vec3 normal, Vec3 color)
	{
		this.position = position;
		this.normal = normal;
		this.color = color;
	}

	public Vec3 getPosition()
	{
		return position;
	}

	public void setPosition(Vec3 position)
	{
		this.position = position;
	}

	public Vec3 getNormal()
	{
		return normal;
	}

	public void setNormal(Vec3 normal)
	{
		this.normal = normal;
	}

	public Vec3 getColor()
	{
		return color;
	}

	public void setColor(Vec3 color)
	{
		this.color = color;
	}
}
