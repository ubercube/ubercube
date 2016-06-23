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
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.physics.Rigidbody;

/**
 * Created by Marccspro on 31 janv. 2016.
 */
public class ECKeyMovement extends EComponent
{
	private float speed;
	private float jumpForce;
	
	private boolean	up;
	private boolean	down;
	private boolean	left;
	private boolean	right;
	
	private boolean jump;
	private boolean crouche;
	private boolean prone;

	private boolean fly;

	public ECKeyMovement(float speed, float jumpForce)
	{
		super(KEY_MOVEMENT);
		super.addDependencies(RENDER, RIGIDBODY);

		this.speed = speed;
		this.jumpForce = jumpForce;
		this.fly = true;
	}

	public void update(GameCore core)
	{
		Quat rotation = ((ECRender) parent.get(EComponent.RENDER)).getTransform().getRotation();

		Vec3 forwardDirection = rotation.getForward().copy().mul(1, 0, 1).normalize();
		Vec3 leftDirection = rotation.getLeft().copy().mul(1, 0, 1).normalize();

		Rigidbody body = ((ECRigidbody) parent.get(RIGIDBODY)).getBody();

		if (up)
			body.applyForce(forwardDirection, speed);

		if (down)
			body.applyForce(forwardDirection, -speed);

		if (left)
			body.applyForce(leftDirection, speed);

		if (right)
			body.applyForce(leftDirection, -speed);

		body.useGravity(!fly);
		if (fly)
		{
			if (jump)
				body.applyForce(Vec3.UP, speed);
		}
		else
		{
			if (jump)
				if (body.isGrounded())
					body.applyForce(Vec3.UP, jumpForce);
		}

		if (crouche)
			body.applyForce(Vec3.UP, -speed);
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
}
