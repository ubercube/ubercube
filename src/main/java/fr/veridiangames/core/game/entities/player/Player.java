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

import fr.veridiangames.client.Ubercube;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.components.*;
import fr.veridiangames.core.game.gamemodes.Team;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec2;
import fr.veridiangames.core.maths.Vec3;

import static fr.veridiangames.core.maths.Mathf.atan2;
import static fr.veridiangames.core.maths.Mathf.toDegrees;

/**
 * Created by Marccspro on 26 f�vr. 2016.
 */
public abstract class Player extends Entity
{
	public static final int MAX_LIFE = 100;
	public static final float HEIGHT = 2.5f;

	private Vec3 newPosition;
	private boolean dead;
	private boolean hitable;
	private int ping;
	private float movementVelocity = 0;

	private Vec3 rightHandPosition = new Vec3();
	private Vec3 leftHandPosition = new Vec3();

	public Player(int id, String name, Vec3 position, Quat rotation, String address, int port)
	{
		super(id);
		super.add(new ECName(name));
		super.add(new ECRender(position, rotation, new Vec3(0.4f, HEIGHT * 0.5f, 0.4f)));
		super.add(new ECNetwork(address, port));
		super.add(new ECWeapon(0));
		super.add(new ECAudioSource());

		this.dead = false;
		this.hitable = false;
		this.ping = 0;
	}

	int time = 0;
	public void update(GameCore core)
	{
		super.update(core);
		time++;
		if (time > 60 * 5)
			hitable = true;

		if (newPosition != null)
		{
			Vec3 smoothPosition = getPosition().copy().add(newPosition.copy().sub(getPosition()).mul(0.2f));
			setPosition(smoothPosition);
		}
	}

	public abstract void respawn(Vec3 position, Quat rotation);

	public String getName()
	{
		return ((ECName) this.get(EComponent.NAME)).getName();
	}

	public ECRender getRenderer() { return ((ECRender) this.get(EComponent.RENDER)); }

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

	public Transform getTransform()
	{
		return new Transform(getPosition(), getRotation());
	}
	
	public void setPositionSmoothly(Vec3 position)
	{
		if (newPosition != null)
			movementVelocity = newPosition.xz().sub(position.xz()).magnitude();
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
	
	public ECWeapon getWeaponComponent()
	{
		return ((ECWeapon) super.get(EComponent.WEAPON));
	}

	public ECAudioSource getAudioSource()
	{
		return ((ECAudioSource) super.get(EComponent.AUDIO_SOURCE));
	}

	public Vec3 getEyePosition()
	{
		return getPosition().copy().add(0, 2.5f * 0.5f, 0);
	}

	public Transform getEyeTransform()
	{
		return ((ECRender) this.get(EComponent.RENDER)).getEyeTransform();
	}

	public boolean isDead()
	{
		return dead;
	}

	public void setDead(boolean dead)
	{
		if (dead)
		{
			time = 0;
			hitable = false;
		}
		this.dead = dead;
	}

	public Vec3 getEyeRotation()
	{
		Vec3 result = new Vec3();

		Vec2 xz = getRotation().getForward().xz().normalize();
		Vec2 yz = getRotation().getForward().yz().normalize();
		Vec2 xy = getRotation().getRight().xy().normalize();

		result.x = toDegrees(atan2(xz.y, xz.x));
		result.y = toDegrees(atan2(yz.y, yz.x));
		result.z = toDegrees(atan2(xy.y, xy.x));

		return result;
	}

	public float getXRotation()
	{
		return (getRotation().getForward().y * 0.5f + 0.5f) * 180;
	}

	public Team getTeam()
	{
		return GameCore.getInstance().getGame().getGameMode().getPlayerTeam(this.id);
	}

	public boolean isHitable()
	{
		return hitable;
	}

	public int getPing()
	{
		return ping;
	}

	public void setPing(int ping)
	{
		this.ping = ping;
	}

	public float getMovementVelocity() {
		return movementVelocity * 1.5f;
	}

	public Vec3 getLeftHandPosition() {
		return leftHandPosition;
	}

	public Vec3 getRightHandPosition() {
		return rightHandPosition;
	}

	public void setLeftHandPosition(Vec3 leftHandPosition) {
		this.leftHandPosition = leftHandPosition;
	}

	public void setRightHandPosition(Vec3 rightHandPosition) {
		this.rightHandPosition = rightHandPosition;
	}
}
