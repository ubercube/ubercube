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

package fr.veridiangames.core.physics;

import fr.veridiangames.core.maths.Mathf;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.physics.colliders.AABoxCollider;

public class CollisionHandler
{
	public static Vec3 mtdAABBvsAABB(AABoxCollider a, AABoxCollider b)
	{
		Vec3 mtd = new Vec3();
		Vec3 absNormal = new Vec3();
		
		float xSide = a.getPosition().x - b.getPosition().x;
		float xDist = Mathf.abs(xSide) - (a.getSize().x + b.getSize().x);
		float ySide = a.getPosition().y - b.getPosition().y;
		float yDist = Mathf.abs(ySide) - (a.getSize().y + b.getSize().y);
		float zSide = a.getPosition().z - b.getPosition().z;
		float zDist = Mathf.abs(zSide) - (a.getSize().z + b.getSize().z);
		
		xSide = Mathf.norm(xSide);
		ySide = Mathf.norm(ySide);
		zSide = Mathf.norm(zSide);
		float dist = Mathf.nearest(xDist, Mathf.nearest(yDist, zDist, 0), 0);

		float side = 0;
		if (dist == xDist)
		{
			side = xSide;
			absNormal.x = 1;
		}
		else if (dist == yDist)
		{
			side = ySide;
			absNormal.y = 1;
		}
		else if (dist == zDist)
		{
			side = zSide;
			absNormal.z = 1;
		}
		else
			System.out.println("ERROR 42: Houston on a un probl�me !");
		
		mtd = absNormal.copy().mul(dist).mul(-side);
		return mtd;
	}
}
