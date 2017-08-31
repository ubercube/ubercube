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

package fr.veridiangames.core.game.entities.components;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.audio.Sound;
import fr.veridiangames.core.game.entities.audio.AudioSource;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.physics.Rigidbody;
import fr.veridiangames.core.profiler.Profiler;

/**
 * Created by Marccspro on 31 janv. 2016.
 */
public class ECKeyMovement extends EComponent
{
	private float speed;
	private float walkSpeed;
	private float runSpeed;
	private float jumpForce;

	private boolean	up;
	private boolean	down;
	private boolean	left;
	private boolean	right;
	private boolean	ctrl;

	private boolean jump;
	private boolean crouche;
	private boolean prone;

	private boolean run;

	private boolean fly;

	private Vec3 velocity;
	private Vec3 forwardDirection;

	private Profiler profiler;

	public ECKeyMovement(float walkSpeed, float runSpeed, float jumpForce)
	{
		super(KEY_MOVEMENT);
		super.addDependencies(RENDER, RIGIDBODY);

		this.velocity = new Vec3();
		this.walkSpeed = walkSpeed;
		this.runSpeed = runSpeed;
		this.jumpForce = jumpForce;
		this.fly = false;
		this.forwardDirection = new Vec3();
		this.profiler = new Profiler("player_movement", true);
	}

	@Override
	public void update(GameCore core)
	{
		this.profiler.start();
		Quat rotation = ((ECRender) this.parent.get(EComponent.RENDER)).getTransform().getRotation();

		this.forwardDirection = rotation.getForward().copy().mul(1, 0, 1).normalize();
		Vec3 leftDirection = rotation.getLeft().copy().mul(1, 0, 1).normalize();

		Rigidbody body = ((ECRigidbody) this.parent.get(RIGIDBODY)).getBody();

		if (this.run)
			this.speed = this.runSpeed;
		else
			this.speed = this.walkSpeed;

		this.velocity.mul(0.9f);

		if (this.up)
		{
			body.applyForce(this.forwardDirection, this.speed);
			this.velocity.add(0, 0, this.speed);
		}

		if (this.down)
		{
			body.applyForce(this.forwardDirection, -this.speed);
			this.velocity.add(0, 0, -this.speed);
		}

		if (this.left)
		{
			body.applyForce(leftDirection, this.speed);
			this.velocity.add(this.speed, 0, 0);
		}

		if (this.right)
		{
			body.applyForce(leftDirection, -this.speed);
			this.velocity.add(-this.speed, 0, 0);
		}

		body.useGravity(!this.fly);
		if (this.fly)
		{
			if (this.jump)
				body.applyForce(Vec3.UP, this.speed);
			if (this.ctrl)
				body.applyForce(Vec3.UP.copy().negate(), this.speed);
		} else if (this.jump)
			if (body.isGrounded() && body.getVelocity().y <= 0)
			{
				core.getGame().spawn(new AudioSource(Sound.JUMP));
				body.applyForce(Vec3.UP, this.jumpForce);
			}

		this.velocity.add(0, body.getVelocity().y, 0);
		this.profiler.end();
	}

	public Vec3 getForwardDirection()
	{
		return this.forwardDirection;
	}

	public boolean isUp()
	{
		return this.up;
	}

	public void setUp(boolean up)
	{
		this.up = up;
	}

	public boolean isDown()
	{
		return this.down;
	}

	public void setDown(boolean down)
	{
		this.down = down;
	}

	public boolean isLeft()
	{
		return this.left;
	}

	public void setLeft(boolean left)
	{
		this.left = left;
	}

	public boolean isRight()
	{
		return this.right;
	}

	public void setRight(boolean right)
	{
		this.right = right;
	}

	public boolean isJump()
	{
		return this.jump;
	}

	public void setJump(boolean jump)
	{
		this.jump = jump;
	}

	public boolean isCrouche()
	{
		return this.crouche;
	}

	public void setCrouche(boolean crouche)
	{
		this.crouche = crouche;
	}

	public boolean isProne()
	{
		return this.prone;
	}

	public void setProne(boolean prone)
	{
		this.prone = prone;
	}

	public float getSpeed()
	{
		return this.speed;
	}

	public void setSpeed(float speed)
	{
		this.speed = speed;
	}

	public float getJumpForce()
	{
		return this.jumpForce;
	}

	public void setJumpForce(float jumpForce)
	{
		this.jumpForce = jumpForce;
	}

	public boolean isFly()
	{
		return this.fly;
	}

	public void setFly(boolean fly)
	{
		this.fly = fly;
	}

	public boolean isRun()
	{
		return this.run;
	}

	public void setRun(boolean run)
	{
		this.run = run;
	}

	public Vec3 getVelocity(float factor)
	{
		return this.velocity.copy().mul(factor);
	}

	public boolean isCtrl()
	{
		return this.ctrl;
	}

	public void setCtrl(boolean ctrl)
	{
		this.ctrl = ctrl;
	}
}
