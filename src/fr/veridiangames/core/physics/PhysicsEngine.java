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
import fr.veridiangames.core.maths.Vec3;

/**
 * Created by Marccspro on 22 janv. 2016.
 */
public class PhysicsEngine
{
	private List<Rigidbody> bodies;

	public PhysicsEngine()
	{
		bodies = new ArrayList<>();
	}

	public void update(GameCore core)
	{
		for (int i = 0; i < bodies.size(); i++)
		{
			Rigidbody a = bodies.get(i);
			a.applyGravity();
			a.applyForces();
			a.updateVelocity();
			for (int j = 0; j < bodies.size(); j++)
			{
				Rigidbody b = bodies.get(j);
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
		bodies.add(body);
	}

	public void removeBody(Rigidbody body)
	{
		bodies.remove(body);
	}

	public void explosion(Vec3 pos, float force)
	{
		if (pos.toString().contains("NaN"))
			return;

		for (Rigidbody b : bodies)
		{
			float dist = (b.getPosition().x - pos.x) * (b.getPosition().x - pos.x)
					   + (b.getPosition().y - pos.y) * (b.getPosition().y - pos.y)
					   + (b.getPosition().z - pos.z) * (b.getPosition().z - pos.z);

			Vec3 dir = new Vec3(b.getPosition().x - pos.x, b.getPosition().y - pos.y, b.getPosition().z - pos.z).normalize();

			System.out.println("exp " + pos.toString() + "\nrb " + b.getPosition().toString() + " \nforce: " + force/(dist+force));

			b.applyForce(dir, force/(dist+force));
		}
	}
}
