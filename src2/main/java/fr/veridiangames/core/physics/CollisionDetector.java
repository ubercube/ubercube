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

import fr.veridiangames.core.physics.colliders.AABoxCollider;

/**
 * Created by Marccspro on 22 janv. 2016.
 */
public class CollisionDetector
{
	public static boolean detectAABBvsAABB(AABoxCollider a, AABoxCollider b)
	{
		float ax0 = a.getPosition().x - a.getSize().x;
		float ax1 = a.getPosition().x + a.getSize().x;
		float ay0 = a.getPosition().y - a.getSize().y;
		float ay1 = a.getPosition().y + a.getSize().y;
		float az0 = a.getPosition().z - a.getSize().z;
		float az1 = a.getPosition().z + a.getSize().z;

		float bx0 = b.getPosition().x - b.getSize().x;
		float bx1 = b.getPosition().x + b.getSize().x;
		float by0 = b.getPosition().y - b.getSize().y;
		float by1 = b.getPosition().y + b.getSize().y;
		float bz0 = b.getPosition().z - b.getSize().z;
		float bz1 = b.getPosition().z + b.getSize().z;

		if (ax0 < bx1 && ax1 >= bx0 &&
			ay0 < by1 && ay1 >= by0 &&
			az0 < bz1 && az1 >= bz0)
			return true;

		return false;
	}

	public static boolean detectAxisX(AABoxCollider a, AABoxCollider b)
	{
		float ax0 = a.getPosition().x - a.getSize().x;
		float ax1 = a.getPosition().x + a.getSize().x;

		float bx0 = b.getPosition().x - b.getSize().x;
		float bx1 = b.getPosition().x + b.getSize().x;

		if (ax0 < bx1 && ax1 >= bx0)
			return true;
		return false;
	}

	public static boolean detectAxisY(AABoxCollider a, AABoxCollider b)
	{
		float ay0 = a.getPosition().y - a.getSize().y;
		float ay1 = a.getPosition().y + a.getSize().y;

		float by0 = b.getPosition().y - b.getSize().y;
		float by1 = b.getPosition().y + b.getSize().y;

		if (ay0 < by1 && ay1 >= by0)
			return true;
		return false;
	}

	public static boolean detectAxisZ(AABoxCollider a, AABoxCollider b)
	{
		float az0 = a.getPosition().z - a.getSize().z;
		float az1 = a.getPosition().z + a.getSize().z;

		float bz0 = b.getPosition().z - b.getSize().z;
		float bz1 = b.getPosition().z + b.getSize().z;

		if (az0 < bz1 && az1 >= bz0)
			return true;
		return false;
	}
}