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
import fr.veridiangames.core.game.world.Chunk;
import fr.veridiangames.core.game.world.World;
import fr.veridiangames.core.maths.Mathf;
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
	private boolean 	hitGrounded;
	private Entity		parent;
	private boolean 	useGravity;
	private float 		bounceFactor;

	private Vec3 	collidingAxis;
	private boolean collidingY;
	private float restitution = 1;
	private boolean ignoreOthers;

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
		this.airDragFactor = 0.9f;
		this.frictionFactor = 0.3f;
		this.dragFactor = this.airDragFactor;
		this.networkView = networkView;
		this.mainForce = new Vec3();
		this.collidingAxis = new Vec3(1, 1, 1);
	}

	public void updateDragFactor(float delta)
	{
		if (this.grounded)
		{
			this.dragFactor = this.frictionFactor;
			if (this.restitution > 0)
			{
				this.restitution = this.velocity.magnitude() * 0.4f;
				this.applyForce(Vec3.UP, this.restitution);
				this.restitution *= this.bounceFactor;
				if (this.restitution < 0.01f)
					this.restitution = 0;
			}
		} else
			this.dragFactor = this.airDragFactor;
	}

	public void updateVelocity(float delta)
	{
		if (this.networkView)
			return;

		this.collider.getPosition().add(this.velocity.copy().mul(delta));
	}

	public void updateDrag(float delta)
	{
		if (this.networkView)
			return;

		this.velocity.mul(this.dragFactor + 0.09f);
	}

	public void applyGravity(float delta)
	{
		if (this.networkView)
			return;
		if (!this.useGravity)
			return;

		this.gravity.add(0, 2.5f * delta, 0);
		Vec3 forceVector = this.gravity.copy().negate().mul(1.0f / 60.0f / 60.0f);
		this.mainForce.add(forceVector.mul(delta));
	}

	public void applyForces(float delta)
	{
		if (this.networkView)
			return;

		this.velocity.add(this.mainForce);
		this.mainForce.set(0, 0, 0);
	}

	public void applyForce(Vec3 direction, float force)
	{
		Vec3 forceVector = direction.copy().mul(force);
		this.mainForce.add(forceVector);
	}

	public void applyMovementForce(Vec3 direction, float force)
	{
		this.applyForce(direction, force);
	}

	public void resetAxisCollision()
	{
		this.collidingAxis.set(1, 1, 1);
	}

	public CollisionData getCollisionData(Rigidbody body)
	{
		return this.collider.getCollisionData(body.getCollider());
	}

	public void handleCollision(CollisionData data, Collider other)
	{
		AABoxCollider a = (AABoxCollider) this.collider;
		AABoxCollider b = (AABoxCollider) other;
		Vec3 mtd = CollisionHandler.mtdAABBvsAABB(a, b);
		this.collider.getPosition().add(mtd);
	}

	public void handleWorldCollision(World world, float delta)
	{
		if (this.networkView)
			return;

		this.collidingY = false;
		Vec3 axis = new Vec3();
		List<AABoxCollider> blocks = world.getAABoxInRange(this.position, 4);
		for (int i = 0; i < blocks.size(); i++)
		{
			AABoxCollider b = blocks.get(i);
			CollisionData data = this.collider.getCollisionData(b);

			if (data.isCollision())
			{
				float collisionMTD = Mathf.nearest(data.getMtdX(), Mathf.nearest(data.getMtdY(), data.getMtdZ(), 0), 0) * delta;
				if (data.isCollisionX() && axis.x == 0 && data.getMtdX() * delta == collisionMTD)
				{
					this.gravity.x = 0;
					this.mainForce.x = 0;
					this.velocity.x = 0;
					float mtd = data.getMtdX() * delta;
					this.collider.getPosition().x += mtd;
					axis.x = 1;
					this.collidingAxis.x = 0;
				}
				else if (data.isCollisionY() && axis.y == 0 && data.getMtdY() * delta == collisionMTD)
				{
					if (this.velocity.y < 0 && data.getMtdY() * delta > 0)
					{
						if (!this.hitGrounded && !this.grounded && this.velocity.y < -0.10f)
							this.hitGrounded = true;
						else
							this.hitGrounded = false;
						this.grounded = true;
					}
					else
					{
						this.hitGrounded = false;
						this.grounded = false;
					}

					if (data.getMtdY() * delta > 0)
						this.gravity.y = 0;
					this.mainForce.y = 0;
					this.velocity.y = 0;
					float mtd = data.getMtdY() * delta;
					this.collider.getPosition().y += mtd;
					axis.y = 1;
					this.collidingAxis.y = 0;
					this.collidingY = true;
				}
				else if (data.isCollisionZ() && axis.z == 0 && data.getMtdZ() * delta == collisionMTD)
				{
					this.gravity.z = 0;
					this.mainForce.z = 0;
					this.velocity.z = 0;
					float mtd = data.getMtdZ() * delta;
					this.collider.getPosition().z += mtd;
					axis.z = 1;
					this.collidingAxis.z = 0;
				}
			}
			if (axis.equals(1, 1, 1))
				break;
		}
		if (this.collider.getPosition().x < 0)
			this.collider.getPosition().x = 0;
		if (this.collider.getPosition().z < 0)
			this.collider.getPosition().z = 0;
		if (this.collider.getPosition().x > world.getWorldSize() * Chunk.SIZE)
			this.collider.getPosition().x = world.getWorldSize() * Chunk.SIZE;
		if (this.collider.getPosition().z > world.getWorldSize()* Chunk.SIZE)
			this.collider.getPosition().z = world.getWorldSize() * Chunk.SIZE;

		if (!this.collidingY)
		{
			this.grounded = false;
			this.hitGrounded = false;
		}
	}

	public void updatePosition()
	{
		this.position.set(this.collider.getPosition());
		((ECRender) this.parent.get(EComponent.RENDER)).getTransform().setLocalPosition(this.position);
	}

	public void killForces()
	{
		this.mainForce.set(0, 0, 0);
		this.velocity.set(0, 0, 0);
		this.gravity.set(0, 0, 0);
	}

	public Vec3 getPosition()
	{
		return this.position;
	}

	public void setPosition(Vec3 position)
	{
		this.collider.setPosition(position);
	}

	public Quat getRotation()
	{
		return this.rotation;
	}

	public void setRotation(Quat rotation)
	{
		this.rotation = rotation;
	}

	public Vec3 getVelocity()
	{
		return this.velocity;
	}

	public void setVelocity(Vec3 velocity)
	{
		this.velocity = velocity;
	}

	public Collider getCollider()
	{
		return this.collider;
	}

	public void setCollider(Collider collider)
	{
		this.collider = collider;
	}

	public float getDragFactor()
	{
		return this.dragFactor;
	}

	public float getAirDragFactor()
	{
		return this.airDragFactor;
	}

	public void setAirDragFactor(float airDragFactor)
	{
		this.airDragFactor = airDragFactor;
	}

	public float getFrictionFactor()
	{
		return this.frictionFactor;
	}

	public void setFrictionFactor(float frictionFactor)
	{
		this.frictionFactor = frictionFactor;
	}

	public float getBounceFactor()
	{
		return this.bounceFactor;
	}

	public void setBounceFactor(float bounceFactor)
	{
		this.bounceFactor = bounceFactor;
	}

	public boolean isGrounded()
	{
		return this.grounded;
	}

	public void setGrounded(boolean grounded)
	{
		this.grounded = grounded;
	}

	public Entity getParent()
	{
		return this.parent;
	}

	public boolean isNetworkView()
	{
		return this.networkView;
	}

	public void setNetworkView(boolean networkView)
	{
		this.networkView = networkView;
	}

	public boolean isGravity()
	{
		return this.useGravity;
	}

	public void useGravity(boolean useGravity)
	{
		this.useGravity = useGravity;
	}

	public boolean isIgnoreOthers()
	{
		return this.ignoreOthers;
	}

	public void setIgnoreOthers(boolean ignoreOthers)
	{
		this.ignoreOthers = ignoreOthers;
	}

	public boolean isHitGrounded()
	{
		return this.hitGrounded;
	}
}