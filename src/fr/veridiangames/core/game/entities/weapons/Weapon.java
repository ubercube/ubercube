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

import java.util.ArrayList;
import java.util.List;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.game.entities.weapons.fire_weapons.WeaponAK47;
import fr.veridiangames.core.game.entities.weapons.fire_weapons.WeaponAWP;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec2;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.packets.WeaponPositionPacket;

public abstract class Weapon
{
	protected NetworkableClient	net;
	
	public static final int AK47 		= 0;
	public static final int AWP 		= 1;
	public static final int SHOVEL 		= 2;

	protected Vec2 velocity;

	protected int currentPosition;
	protected boolean positionChanged;
	
	protected Transform idlePosition;
	protected Transform zoomPosition;
	protected Transform hidePosition;
	protected Transform runPosition;
	
	protected Transform transform;
	protected Player holder;
	
	protected int model;
	
	public Weapon(int model)
	{
		this.model = model;
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
	}
	
	public void updateWeaponVelocity(float vx, float vy)
	{
		
	}
	
	public Transform getTransform()
	{
		return transform;
	}

	public void setTransformSmoothly(Transform transform, float smoothFactor)
	{
		Vec3 positionAddFactor = transform.getLocalPosition().copy().sub(this.transform.getLocalPosition()).mul(smoothFactor);
		this.transform.getLocalPosition().add(positionAddFactor);
		this.transform.setLocalRotation(transform.getLocalRotation());
	}

	public void shoot()
	{
	}
	
	public void setPosition(int position)
	{
		if (this.currentPosition != position)
			this.positionChanged = true;

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
}