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
import fr.veridiangames.core.maths.Mathf;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.maths.Vec4;
import fr.veridiangames.core.physics.Rigidbody;

import javax.jws.soap.SOAPBinding;

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

	public ECKeyMovement(float walkSpeed, float runSpeed, float jumpForce)
	{
		super(KEY_MOVEMENT);
		super.addDependencies(RENDER, RIGIDBODY);

		this.velocity = new Vec3();
		this.walkSpeed = walkSpeed;
		this.runSpeed = runSpeed;
		this.jumpForce = jumpForce;
		this.fly = true;
		this.forwardDirection = new Vec3();
	}


	public void update(GameCore core)
	{
		Quat rotation = ((ECRender) parent.get(EComponent.RENDER)).getTransform().getRotation();

		forwardDirection = rotation.getForward().copy().mul(1, 0, 1).normalize();
		Vec3 leftDirection = rotation.getLeft().copy().mul(1, 0, 1).normalize();

		Rigidbody body = ((ECRigidbody) parent.get(RIGIDBODY)).getBody();

		if (run)
			speed = runSpeed;
		else
			speed = walkSpeed;

		velocity.mul(0.9f);

		if (up)
		{
			body.applyForce(forwardDirection, speed);
			velocity.add(0, 0, speed);
		}

		if (down)
		{
			body.applyForce(forwardDirection, -speed);
			velocity.add(0, 0, -speed);
		}

		if (left)
		{
			body.applyForce(leftDirection, speed);
			velocity.add(speed, 0, 0);
		}

		if (right)
		{
			body.applyForce(leftDirection, -speed);
			velocity.add(-speed, 0, 0);
		}

		body.useGravity(!fly);
		if (fly)
		{
			if (jump)
			{
				body.applyForce(Vec3.UP, speed);

			}
			if (ctrl)
				body.applyForce(Vec3.UP.copy().negate(), speed);
		}
		else
		{
			if (jump)
				if (body.isGrounded() && body.getVelocity().y <= 0)
				{
					core.getGame().spawn(new AudioSource(Sound.JUMP));
					body.applyForce(Vec3.UP, jumpForce);
				}
		}

		velocity.add(0, body.getVelocity().y, 0);
	}

	public Vec3 getForwardDirection()
	{
		return forwardDirection;
	}

	public boolean isUp()
	{
		return up;
	}

	public void setUp(boolean up)
	{
		this.up = up;
	}

	public boolean isDown()
	{
		return down;
	}

	public void setDown(boolean down)
	{
		this.down = down;
	}

	public boolean isLeft()
	{
		return left;
	}

	public void setLeft(boolean left)
	{
		this.left = left;
	}

	public boolean isRight()
	{
		return right;
	}

	public void setRight(boolean right)
	{
		this.right = right;
	}

	public boolean isJump()
	{
		return jump;
	}

	public void setJump(boolean jump)
	{
		this.jump = jump;
	}

	public boolean isCrouche()
	{
		return crouche;
	}

	public void setCrouche(boolean crouche)
	{
		this.crouche = crouche;
	}

	public boolean isProne()
	{
		return prone;
	}

	public void setProne(boolean prone)
	{
		this.prone = prone;
	}

	public float getSpeed()
	{
		return speed;
	}

	public void setSpeed(float speed)
	{
		this.speed = speed;
	}

	public float getJumpForce()
	{
		return jumpForce;
	}

	public void setJumpForce(float jumpForce)
	{
		this.jumpForce = jumpForce;
	}

	public boolean isFly()
	{
		return fly;
	}

	public void setFly(boolean fly)
	{
		this.fly = fly;
	}

	public boolean isRun()
	{
		return run;
	}

	public void setRun(boolean run)
	{
		this.run = run;
	}

	public Vec3 getVelocity(float factor)
	{
		return velocity.copy().mul(factor);
	}

	public boolean isCtrl()
	{
		return ctrl;
	}

	public void setCtrl(boolean ctrl)
	{
		this.ctrl = ctrl;
	}
}
