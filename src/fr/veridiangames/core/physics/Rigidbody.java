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

import java.util.List;

import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.components.ECRender;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.game.world.World;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.physics.colliders.AABoxCollider;
import fr.veridiangames.core.physics.colliders.Collider;

/**
 * Created by Marccspro on 22 janv. 2016.
 */
public class Rigidbody
{
	private Vec3 	mainForce;
	
	private Vec3	position;
	private Quat	rotation;
	private Vec3	velocity;
	private Vec3 	gravity;
	private float	dragFactor;
	private float 	airDragFactor;
	private float 	frictionFactor;

	private Collider	collider;
	private boolean 	grounded;
	private Entity		parent;
	private boolean 	useGravity;
	private float 		bounceFactor;

	private List<String> ignores;
	
	private boolean networkView;

	public Rigidbody(Entity e, Vec3 position, Quat rotation, Collider collider, boolean networkView)
	{
		this.parent = e;
		this.position = position.copy();
		this.rotation = rotation;
		this.gravity = new Vec3();
		this.velocity = new Vec3();
		this.collider = collider;
		this.collider.setPosition(position.copy());
		this.dragFactor = 0f;
		this.airDragFactor = 0.9f;
		this.frictionFactor = 0.3f;
		this.networkView = networkView;
		this.mainForce = new Vec3();
	}

	public void updateVelocity()
	{
		if (networkView)
			return;
		
		if (grounded)
		{
			dragFactor = frictionFactor;
//			if (bounceFactor > 0)
//			{
//				applyForce(Vec3.UP, bounceFactor);
//				bounceFactor *= 0.8f;
//				if (bounceFactor < 0.1f)
//					bounceFactor = 0;
//			}
			System.out.println("Grounded");
		}
		else
		{
			dragFactor = airDragFactor;
		}
		
		collider.getPosition().add(velocity);
		velocity.mul(dragFactor);
	}
	
	public void applyGravity()
	{
		if (networkView)
			return;
		if (!useGravity)
			return;
		gravity.add(0, 3f, 0);
		applyForce(gravity.copy().negate(), 1.0f / 60.0f / 60.0f);
	}
	
	public void applyForces()
	{
		if (networkView)
			return;
		
		velocity.add(mainForce);
		mainForce.set(0, 0, 0);
		grounded = false;
	}

	public void applyForce(Vec3 direction, float force)
	{
		Vec3 forceVector = direction.copy().mul(force);
		mainForce.add(forceVector);
	}

	public CollisionData getCollisionData(Rigidbody body)
	{
		return collider.getCollisionData(body.getCollider());
	}

	public void handleCollision(CollisionData data, Collider other)
	{
		AABoxCollider a = (AABoxCollider) collider;
		AABoxCollider b = (AABoxCollider) other;
		Vec3 mtd = CollisionHandler.mtdAABBvsAABB(a, b).copy();
		this.collider.getPosition().add(mtd);
	}
	
	public void handleWorldCollision(World world)
	{
		if (networkView)
			return;

		grounded = false;
		List<AABoxCollider> blocks = world.getAABoxInRange(position, 3);
		for (int i = 0; i < blocks.size(); i++)
		{
			AABoxCollider b = blocks.get(blocks.size() - i - 1);
			CollisionData data = collider.getCollisionData(b);
			if (data.isCollision())
			{
				Vec3 mtd = data.getMtd();

				if (data.getNormal().y == 1 && velocity.y <= 0 && mainForce.y <= 0)
				{
					gravity.y = 0;
					mainForce.y = 0;
					velocity.y = 0;
					grounded = true;
				}
				if (data.getNormal().x != 0)
				{
					gravity.x = 0;
					mainForce.x = 0;
					velocity.x = 0;
				}
				if (data.getNormal().z != 0)
				{
					gravity.z = 0;
					mainForce.z = 0;
					velocity.z = 0;
				}
				this.collider.getPosition().add(mtd);
			}
		}
	}
	
	public void updatePosition()
	{		
		this.position.set(collider.getPosition());
		((ECRender) this.parent.get(EComponent.RENDER)).getTransform().setLocalPosition(position);
	}

	public void killForces()
	{
		mainForce.set(0, 0, 0);
		velocity.set(0, 0, 0);
		gravity.set(0, 0, 0);
	}

	public Vec3 getPosition()
	{
		return position;
	}

	public void setPosition(Vec3 position)
	{
		this.collider.setPosition(position);
	}

	public Quat getRotation()
	{
		return rotation;
	}

	public void setRotation(Quat rotation)
	{
		this.rotation = rotation;
	}

	public Vec3 getVelocity()
	{
		return velocity;
	}

	public void setVelocity(Vec3 velocity)
	{
		this.velocity = velocity;
	}

	public Collider getCollider()
	{
		return collider;
	}

	public void setCollider(Collider collider)
	{
		this.collider = collider;
	}

	public float getDragFactor()
	{
		return dragFactor;
	}

	public float getAirDragFactor()
	{
		return airDragFactor;
	}

	public void setAirDragFactor(float airDragFactor)
	{
		this.airDragFactor = airDragFactor;
	}

	public float getFrictionFactor()
	{
		return frictionFactor;
	}

	public void setFrictionFactor(float frictionFactor)
	{
		this.frictionFactor = frictionFactor;
	}

	public float getBounceFactor()
	{
		return bounceFactor;
	}

	public void setBounceFactor(float bounceFactor)
	{
		this.bounceFactor = bounceFactor;
	}

	public boolean isGrounded()
	{
		return grounded;
	}

	public void setGrounded(boolean grounded)
	{
		this.grounded = grounded;
	}

	public Entity getParent()
	{
		return parent;
	}

	public boolean isNetworkView()
	{
		return networkView;
	}

	public void setNetworkView(boolean networkView)
	{
		this.networkView = networkView;
	}

	public boolean isGravity()
	{
		return useGravity;
	}

	public void useGravity(boolean useGravity)
	{
		this.useGravity = useGravity;
	}
}