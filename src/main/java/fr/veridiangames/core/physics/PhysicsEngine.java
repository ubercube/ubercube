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

import java.util.ArrayList;
import java.util.List;

import fr.veridiangames.core.GameCore;

/**
 * Created by Marccspro on 22 janv. 2016.
 */
public class PhysicsEngine
{
	private List<Rigidbody> bodys;

	public PhysicsEngine()
	{
		bodys = new ArrayList<>();
	}

	public void update(GameCore core)
	{
		System.out.println("Physics: " + bodys.size());
		for (int i = 0; i < bodys.size(); i++)
		{
			Rigidbody a = bodys.get(i);
			a.applyGravity();
			a.applyForces();
			a.updateVelocity();
			for (int j = 0; j < bodys.size(); j++)
			{
				Rigidbody b = bodys.get(j);
				if (a == b)
					continue;

				if (a.isIgnoreOthers() || b.isIgnoreOthers())
					continue;

				CollisionData data = a.getCollisionData(b);
				if (data.isCollision())
				{
					a.handleCollision(data, b.getCollider());
				}
			}
			a.handleWorldCollision(core.getGame().getWorld());
			a.updatePosition();
			a.updateDragFactor();
		}
	}

	public void addBody(Rigidbody body)
	{
		bodys.add(body);
	}

	public void removeBody(Rigidbody body)
	{
		bodys.remove(body);
	}
}
