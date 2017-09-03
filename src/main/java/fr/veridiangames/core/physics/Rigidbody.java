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
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.game.world.Chunk;
import fr.veridiangames.core.game.world.World;
import fr.veridiangames.core.maths.Mathf;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.physics.colliders.AABoxCollider;
import fr.veridiangames.core.physics.colliders.Collider;

import static javax.swing.text.html.HTML.Tag.HEAD;

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
	private boolean 	hitGrounded;
	private Entity		parent;
	private boolean 	useGravity;
	private float 		bounceFactor;

	private Vec3 	collidingAxis;
	private boolean collidingX;
	private boolean collidingY;
	private boolean collidingZ;

	private float restitution = 1;
	private boolean ignoreOthers;
	
	private boolean networkView;
	private Vec3 movementAxis;

	private boolean enabled;

	public Rigidbody(Entity e, Vec3 position, Quat rotation, Collider collider, boolean networkView)
	{
		this.parent = e;
		this.position = position.copy();
		this.rotation = rotation;
		this.gravity = new Vec3();
		this.velocity = new Vec3();
		this.collider = collider;
		this.collider.setPosition(position.copy());
		this.airDragFactor = 0.9f;
		this.frictionFactor = 0.3f;
		this.dragFactor = airDragFactor;
		this.networkView = networkView;
		this.mainForce = new Vec3();
		this.collidingAxis = new Vec3(1, 1, 1);
		this.enabled = true;
	}

	public void updateDragFactor(float delta)
	{
		if (grounded)
		{
			dragFactor = frictionFactor;
			if (restitution > 0)
			{
				restitution = velocity.magnitude() * 0.4f;
				applyForce(Vec3.UP, restitution);
				restitution *= bounceFactor;
				if (restitution < 0.01f)
					restitution = 0;
			}
		}
		else
		{
			dragFactor = airDragFactor;
		}
	}

	public void updateVelocity(float delta)
	{
		if (networkView)
			return;

		collider.getPosition().add(velocity.copy().mul(delta));
	}

	public void updateDrag(float delta)
	{
		if (networkView)
			return;

		velocity.mul(dragFactor + 0.09f);
	}
	
	public void applyGravity(float delta)
	{
		if (networkView)
			return;
		if (!useGravity)
			return;

		gravity.add(0, 2.5f * delta, 0);
		Vec3 forceVector = gravity.copy().negate().mul(1.0f / 60.0f / 60.0f);
		mainForce.add(forceVector.mul(delta));
	}
	
	public void applyForces(float delta)
	{
		if (networkView)
			return;

		velocity.add(mainForce);
		mainForce.set(0, 0, 0);
	}

	public void applyForce(Vec3 direction, float force)
	{
		Vec3 forceVector = direction.copy().mul(force);
		mainForce.add(forceVector);
	}

	public void applyMovementForce(Vec3 direction, float force)
	{
		applyForce(direction, force);
	}

	public void resetAxisCollision()
	{
		collidingAxis.set(1, 1, 1);
	}

	public CollisionData getCollisionData(Rigidbody body)
	{
		return collider.getCollisionData(body.getCollider());
	}

	public void handleCollision(CollisionData data, Collider other)
	{
		AABoxCollider a = (AABoxCollider) collider;
		AABoxCollider b = (AABoxCollider) other;
		Vec3 mtd = CollisionHandler.mtdAABBvsAABB(a, b);
		this.collider.getPosition().add(mtd);
	}

	public void handleWorldCollision(World world, float delta)
	{
		if (networkView)
			return;

		collidingX = false;
		collidingY = false;
		collidingZ = false;

		Vec3 axis = new Vec3();
		List<AABoxCollider> blocks = world.getAABoxInRange(position, 4);
		for (int i = 0; i < blocks.size(); i++)
		{
			AABoxCollider b = blocks.get(i);
			CollisionData data = collider.getCollisionData(b);

			if (data.isCollision())
			{
				float collisionMTD = Mathf.nearest(data.getMtdX(), Mathf.nearest(data.getMtdY(), data.getMtdZ(), 0), 0) * delta;
				if (data.isCollisionX() && axis.x == 0 && data.getMtdX() * delta == collisionMTD)
				{
					gravity.x = 0;
					mainForce.x = 0;
					velocity.x = 0;
					float mtd = data.getMtdX() * delta;
					this.collider.getPosition().x += mtd;
					axis.x = 1;
					collidingAxis.x = 0;
					collidingX = true;
				}
				else if (data.isCollisionY() && axis.y == 0 && data.getMtdY() * delta == collisionMTD)
				{
					if (velocity.y < 0 && data.getMtdY() * delta > 0)
					{
						if (!hitGrounded && !grounded && velocity.y < -0.10f)
							hitGrounded = true;
						else
							hitGrounded = false;
						grounded = true;
					}
					else
					{
						hitGrounded = false;
						grounded = false;
					}

					if (data.getMtdY() * delta > 0)
						gravity.y = 0;
					mainForce.y = 0;
					velocity.y = 0;
					float mtd = data.getMtdY() * delta;
					this.collider.getPosition().y += mtd;
					axis.y = 1;
					collidingAxis.y = 0;
					collidingY = true;
				}
				else if (data.isCollisionZ() && axis.z == 0 && data.getMtdZ() * delta == collisionMTD)
				{
					gravity.z = 0;
					mainForce.z = 0;
					velocity.z = 0;
					float mtd = data.getMtdZ() * delta;
					this.collider.getPosition().z += mtd;
					axis.z = 1;
					collidingAxis.z = 0;
					collidingZ = true;
				}
			}
			if (axis.equals(1, 1, 1))
				break;
		}
		if (collider.getPosition().x < 0)
			collider.getPosition().x = 0;
		if (collider.getPosition().z < 0)
			collider.getPosition().z = 0;
		if (collider.getPosition().x > world.getWorldSize() * Chunk.SIZE)
			collider.getPosition().x = world.getWorldSize() * Chunk.SIZE;
		if (collider.getPosition().z > world.getWorldSize()* Chunk.SIZE)
			collider.getPosition().z = world.getWorldSize() * Chunk.SIZE;

		if (!collidingY)
		{
			grounded = false;
			hitGrounded = false;
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

	public boolean isIgnoreOthers()
	{
		return ignoreOthers;
	}

	public void setIgnoreOthers(boolean ignoreOthers)
	{
		this.ignoreOthers = ignoreOthers;
	}

	public boolean isHitGrounded()
	{
		return hitGrounded;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}
}