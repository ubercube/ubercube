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
import fr.veridiangames.core.maths.Mathf;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;

/**
 * Created by Marccspro on 31 janv. 2016.
 */
public class ECMouseLook extends EComponent
{
	private float	dx, dy;
	private float 	rotAmnt;
	private float	speed;
	private float	idleSpeed;
	private float	zoomedSpeed;

	private float yaw, pitch, roll;

	public ECMouseLook(float idleSpeed, float zoomedSpeed)
	{
		super(MOUSE_LOOK);

		this.idleSpeed = idleSpeed;
		this.zoomedSpeed = zoomedSpeed;
		this.speed = this.idleSpeed;
		this.rotAmnt = 0;
		this.yaw = 0;
		this.pitch = 0;
		this.roll = 0;
	}

	public void update(GameCore core)
	{
		Transform transform = ((ECRender) parent.get(EComponent.RENDER)).getTransform();

		roll += dx * speed * 0.5f;
		yaw += dy * speed * 0.5f;

		if (yaw < -89)
			yaw = -89;
		if (yaw > 89)
			yaw = 89;

		transform.setLocalRotation(Quat.deuler(yaw, pitch, roll));
	}

	public void setDX(float dx)
	{
		this.dx = dx;
	}
	
	public void setDY(float dy)
	{
		this.dy = dy;
	}

	public float getDx()
	{
		return dx;
	}

	public float getDy()
	{
		return dy;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getSpeed() { return this.speed; }

	public float getIdleSpeed() {
		return idleSpeed;
	}

	public void setIdleSpeed(float idleSpeed) {
		this.idleSpeed = idleSpeed;
	}

	public float getZoomedSpeed() {
		return zoomedSpeed;
	}

	public void setZoomedSpeed(float zoomedSpeed) {
		this.zoomedSpeed = zoomedSpeed;
	}

	public void useZoomSpeed(boolean trigger, float factor) { this.speed = trigger ? (zoomedSpeed * factor) : idleSpeed;}
}
