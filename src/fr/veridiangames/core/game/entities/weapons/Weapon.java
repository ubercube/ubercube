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

package fr.veridiangames.core.game.entities.weapons;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.maths.*;
import fr.veridiangames.core.network.NetworkableClient;

public abstract class Weapon
{
	protected NetworkableClient	net;

	private boolean destroyed;

	protected Vec2 velocity;

	protected int currentPosition;
	protected boolean positionChanged;
	
	protected Transform idlePosition;
	protected Transform zoomPosition;
	protected Transform hidePosition;
	protected Transform runPosition;
	protected Vec3 		runRotation;
	
	protected Transform transform;
	protected Vec3 		rotationFactor;
	protected Player holder;
	protected boolean zoomed;
	
	protected int model;
	
	public Weapon(int model)
	{
		this.model = model;
		this.rotationFactor = new Vec3(0, 0, 0);
		this.transform = new Transform();
		this.positionChanged = false;
		this.velocity = new Vec2();
	}

	public void start()
	{
		currentPosition = 2;
		this.transform.getLocalPosition().add(hidePosition.getLocalPosition());
		this.transform.setLocalRotation(hidePosition.getLocalRotation());
	}

	public void init()
	{
		currentPosition = 0;
	}

	public void onChange()
	{
		currentPosition = 2;
		this.transform.getLocalPosition().add(hidePosition.getLocalPosition());
		this.transform.setLocalRotation(hidePosition.getLocalRotation());
	}
	
	public void update(GameCore core)
	{
		if (currentPosition == 0)
			setTransformSmoothly(idlePosition, 0.4f);
		else if (currentPosition == 1)
			setTransformSmoothly(zoomPosition, 0.4f);
		rotationFactor.mul(0.7f);
		this.transform.setLocalRotation(Quat.euler(rotationFactor));
	}
	
	public void updateWeaponVelocity(Vec3 velocity, float dx, float dy, float factor)
	{
		rotationFactor.add(dy * factor, dx * factor, -dx * factor);
		rotationFactor.add(velocity.z * 0.2f, 0, velocity.x * 0.2f);
	}

	public void updateRunPosition()
	{
		rotationFactor.add(runRotation.copy().mul(0.01f));
	}

	int bobbingTime = 0;
	public void updateBobbing(float velocity, float factor, float speed)
	{
		bobbingTime++;
		float bobbing = Mathf.sin(bobbingTime * speed) * factor * velocity;
		float bobbingX = (Mathf.sin(bobbingTime * speed * 0.5f) * factor * velocity);
		rotationFactor.add(bobbing, bobbingX, 0);
	}
	
	public Transform getTransform()
	{
		return transform;
	}

	public void setTransformSmoothly(Transform transform, float smoothFactor)
	{
		Vec3 positionAddFactor = transform.getLocalPosition().copy().sub(this.transform.getLocalPosition()).mul(smoothFactor);
		this.transform.getLocalPosition().add(positionAddFactor);
	}

	public abstract void onAction();
	public abstract void onActionUp();
	public abstract void onActionDown();

	public void setPosition(int position)
	{
		if (this.currentPosition != position)
			this.positionChanged = true;

		zoomed = false;
		if (position == 1)
			zoomed = true;
		this.currentPosition = position;
	}

	public Transform getIdlePosition()
	{
		return idlePosition;
	}

	public void setIdlePosition(Transform idlePosition)
	{
		this.idlePosition = idlePosition;
	}

	public Transform getZoomPosition()
	{
		return zoomPosition;
	}

	public void setZoomPosition(Transform zoomPosition)
	{
		this.zoomPosition = zoomPosition;
	}

	public Transform getHidePosition()
	{
		return hidePosition;
	}

	public void setHidePosition(Transform hidePosition)
	{
		this.hidePosition = hidePosition;
	}

	public Transform getRunPosition()
	{
		return runPosition;
	}

	public void setRunPosition(Transform runPosition)
	{
		this.runPosition = runPosition;
	}

	public int getModel()
	{
		return model;
	}

	public boolean hasPositionChanged()
	{
		return positionChanged;
	}
	
	public void setPositionChanged(boolean b)
	{
		positionChanged = b;
	}

	public int getCurrentPosition()
	{
		return currentPosition;
	}
	
	public void setVelocity(float x, float y)
	{
		this.velocity.set(x, y);
	}

	public void setNet(NetworkableClient net)
	{
		this.net = net;
	}

	public Player getHolder()
	{
		return holder;
	}

	public void setHolder(Player holder)
	{
		this.holder = holder;
	}

	public boolean isDestroyed()
	{
		return destroyed;
	}

	public void destroy()
	{
		this.destroyed = true;
	}
}