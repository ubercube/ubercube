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

package fr.veridiangames.core.game.entities.player;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.components.ECModel;
import fr.veridiangames.core.game.entities.components.ECName;
import fr.veridiangames.core.game.entities.components.ECNetwork;
import fr.veridiangames.core.game.entities.components.FuckedECPosition;
import fr.veridiangames.core.game.entities.components.ECRender;
import fr.veridiangames.core.game.entities.components.ECRigidbody;
import fr.veridiangames.core.game.entities.components.FuckedECRotation;
import fr.veridiangames.core.game.entities.components.ECWeapon;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;

/**
 * Created by Marccspro on 26 f�vr. 2016.
 */
public class Player extends Entity
{
	private Vec3 newPosition;
	private boolean dead;
	
	public Player(int id, String name, Vec3 position, Quat rotation, String address, int port)
	{
		super(id);
		super.add(new ECName(name));
		super.add(new ECRender(position, rotation, new Vec3(0.4f, 2.5f * 0.5f, 0.4f)));
		super.add(new ECModel(0));
		super.add(new ECNetwork(address, port));
		super.add(new ECWeapon(0));

		this.dead = false;
	}
	
	public void update(GameCore core)
	{
		super.update(core);
		if (newPosition != null)
		{
			Vec3 smoothPosition = getPosition().copy().add(newPosition.copy().sub(getPosition()).mul(0.2f));
			setPosition(smoothPosition);
		}
	}
	
	public String getName()
	{
		return ((ECName) this.get(EComponent.NAME)).getName();
	}
	
	public Vec3 getPosition()
	{
		if (this.contains(EComponent.RIGIDBODY))
		{
			return ((ECRigidbody) this.get(EComponent.RIGIDBODY)).getBody().getPosition();
		}
		else
		{
			return ((ECRender) this.get(EComponent.RENDER)).getTransform().getPosition();
		}
	}
	
	public Quat getRotation()
	{
		return ((ECRender) this.get(EComponent.RENDER)).getEyeTransform().getRotation();
	}
	
	public void setPositionSmoothly(Vec3 position)
	{
		newPosition = position;
	}
	
	public void setPosition(Vec3 position)
	{
		if (this.contains(EComponent.RIGIDBODY))
		{
			((ECRigidbody) this.get(EComponent.RIGIDBODY)).getBody().setPosition(position);
		}
		else
		{
			((ECRender) this.get(EComponent.RENDER)).getTransform().setLocalPosition(position);
		}
	}
	
	public void setRotation(Quat rotation)
	{
		((ECRender) this.get(EComponent.RENDER)).getTransform().setLocalRotation(rotation);
	}
	
	public Vec3 getViewDirection()
	{
		return getRotation().getForward();
	}
	
	public ECNetwork getNetwork()
	{
		return ((ECNetwork) this.get(EComponent.NETWORK));
	}
	
	public ECWeapon getWeaponManager()
	{
		return ((ECWeapon) super.get(EComponent.WEAPON));
	}

	public boolean isDead()
	{
		return dead;
	}

	public void setDead(boolean dead)
	{
		this.dead = dead;
	}
}
