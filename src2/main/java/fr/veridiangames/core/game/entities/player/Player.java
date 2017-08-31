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
import fr.veridiangames.core.game.entities.components.ECAudioSource;
import fr.veridiangames.core.game.entities.components.ECName;
import fr.veridiangames.core.game.entities.components.ECNetwork;
import fr.veridiangames.core.game.entities.components.ECRender;
import fr.veridiangames.core.game.entities.components.ECRigidbody;
import fr.veridiangames.core.game.entities.components.ECWeapon;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.game.modes.Team;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;

/**
 * Created by Marccspro on 26 f�vr. 2016.
 */
public class Player extends Entity
{
	public static final int MAX_LIFE = 100;

	private Vec3 newPosition;
	private boolean dead;
	private boolean hitable;
	private int ping;

	public Player(int id, String name, Vec3 position, Quat rotation, String address, int port)
	{
		super(id);
		super.add(new ECName(name));
		super.add(new ECRender(position, rotation, new Vec3(0.4f, 2.5f * 0.5f, 0.4f)));
		super.add(new ECNetwork(address, port));
		super.add(new ECWeapon(0));
		super.add(new ECAudioSource());

		this.dead = false;
		this.hitable = false;
		this.ping = 0;
	}

	int time = 0;
	@Override
	public void update(GameCore core)
	{
		super.update(core);
		this.time++;
		if (this.time > 60 * 5)
			this.hitable = true;

		if (this.newPosition != null)
		{
			Vec3 smoothPosition = this.getPosition().copy().add(this.newPosition.copy().sub(this.getPosition()).mul(0.2f));
			this.setPosition(smoothPosition);
		}
	}

	public String getName()
	{
		return ((ECName) this.get(EComponent.NAME)).getName();
	}

	public Vec3 getPosition()
	{
		if (this.contains(EComponent.RIGIDBODY))
			return ((ECRigidbody) this.get(EComponent.RIGIDBODY)).getBody().getPosition();
		else
			return ((ECRender) this.get(EComponent.RENDER)).getTransform().getPosition();
	}

	public Quat getRotation()
	{
		return ((ECRender) this.get(EComponent.RENDER)).getEyeTransform().getRotation();
	}

	public Transform getTransform()
	{
		return new Transform(this.getPosition(), this.getRotation());
	}

	public void setPositionSmoothly(Vec3 position)
	{
		this.newPosition = position;
	}

	public void setPosition(Vec3 position)
	{
		if (this.contains(EComponent.RIGIDBODY))
			((ECRigidbody) this.get(EComponent.RIGIDBODY)).getBody().setPosition(position);
		else
			((ECRender) this.get(EComponent.RENDER)).getTransform().setLocalPosition(position);
	}

	public void setRotation(Quat rotation)
	{
		((ECRender) this.get(EComponent.RENDER)).getTransform().setLocalRotation(rotation);
	}

	public Vec3 getViewDirection()
	{
		return this.getRotation().getForward();
	}

	public ECNetwork getNetwork()
	{
		return ((ECNetwork) this.get(EComponent.NETWORK));
	}

	public ECWeapon getWeaponManager()
	{
		return ((ECWeapon) super.get(EComponent.WEAPON));
	}

	public ECAudioSource getAudioSource()
	{
		return ((ECAudioSource) super.get(EComponent.AUDIO_SOURCE));
	}

	public Vec3 getEyePosition()
	{
		return this.getPosition().copy().add(0, 2.5f * 0.5f, 0);
	}

	public Transform getEyeTransform()
	{
		return ((ECRender) this.get(EComponent.RENDER)).getEyeTransform();
	}

	public boolean isDead()
	{
		return this.dead;
	}

	public void setDead(boolean dead)
	{
		if (dead)
		{
			this.time = 0;
			this.hitable = false;
		}
		this.dead = dead;
	}

	public Team getTeam()
	{
		return GameCore.getInstance().getGame().getGameMode().getPlayerTeam(this);
	}

	public boolean isHitable()
	{
		return this.hitable;
	}

	public int getPing()
	{
		return this.ping;
	}

	public void setPing(int ping)
	{
		this.ping = ping;
	}
}
