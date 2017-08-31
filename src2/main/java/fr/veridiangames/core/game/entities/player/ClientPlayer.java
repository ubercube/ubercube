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

import java.util.ArrayList;
import java.util.List;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.audio.Sound;
import fr.veridiangames.core.game.entities.audio.AudioSource;
import fr.veridiangames.core.game.entities.components.ECDebug;
import fr.veridiangames.core.game.entities.components.ECKeyMovement;
import fr.veridiangames.core.game.entities.components.ECMouseLook;
import fr.veridiangames.core.game.entities.components.ECRaycast;
import fr.veridiangames.core.game.entities.components.ECRigidbody;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.game.entities.particles.ParticleSnow;
import fr.veridiangames.core.game.entities.particles.ParticleSystem;
import fr.veridiangames.core.game.entities.particles.ParticlesBlood;
import fr.veridiangames.core.maths.Mathf;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.Protocol;
import fr.veridiangames.core.network.packets.EntityMovementPacket;
import fr.veridiangames.core.network.packets.SoundPacket;
import fr.veridiangames.core.network.packets.WeaponPositionPacket;
import fr.veridiangames.core.physics.Rigidbody;
import fr.veridiangames.core.physics.colliders.AABoxCollider;
import fr.veridiangames.core.utils.Indexer;

/**
 * Created by Marccspro on 3 f�vr. 2016.
 */
public class ClientPlayer extends Player
{
	private NetworkableClient net;

	private int life;
	private int timeoutTime;
	private boolean timedOut;
	private boolean kicked;
	private String kickMessage;
	private List<ParticleSystem> particleSystems;

	private ParticleSnow	snow;
	private boolean			renderSnow;

	public ClientPlayer(int id, String name, Vec3 position, Quat rotation, String address, int port)
	{
		super(id, name, position, rotation, address, port);
		super.add(new ECRigidbody(this, position, rotation, new AABoxCollider(new Vec3(0.3f, 2.8f * 0.5f, 0.3f)), false));
		super.add(new ECKeyMovement(0.01f, 0.015f, 0.35f));
		super.add(new ECMouseLook(0.3f));
		super.add(new ECRaycast(5, 0.01f, "ClientPlayer", "Bullet", "ParticleSystem"));
		super.add(new ECDebug());
		super.addTag("ClientPlayer");

		this.getRigidBody().getBody().setFrictionFactor(0.899f);
		this.getRigidBody().getBody().setAirDragFactor(0.9f);

		this.life = Player.MAX_LIFE;

		this.particleSystems = new ArrayList<>();

		this.renderSnow = false;
		this.snow = new ParticleSnow(Indexer.getUniqueID(), position.copy().add(0, 10, 0));
		GameCore.getInstance().getGame().spawn(this.snow);
	}

	@Override
	public void init(GameCore core)
	{
		super.init(core);
		this.getWeaponManager().getWeapon().setNet(this.net);
	}

	int movementTime = 0;
	int timeOutTime = 0;
	@Override
	public void update(GameCore core)
	{
		super.update(core);

		if (this.isDead())
		{
			((AABoxCollider)this.getRigidBody().getBody().getCollider()).setSize(new Vec3(0.3f, 0.1f, 0.3f));
			return;
		}
		((AABoxCollider)this.getRigidBody().getBody().getCollider()).setSize(new Vec3(0.3f, 2.8f * 0.5f, 0.3f));

		this.movementTime++;
		if (this.movementTime % 60 == 5)
		{
			this.net.send(new EntityMovementPacket(this), Protocol.UDP);
			this.movementTime = 0;
		}
		this.timeOutTime++;
		if (this.timeOutTime % 60 == 0)
		{
			this.timeoutTime++;
			if (this.timeoutTime > 20)
				this.timedOut = true;
		}

		this.playSounds(core);

		if (this.life < 0) this.life = 0;
		if (this.life > 100) this.life = 100;

		if (this.getWeaponManager().getWeapon().hasPositionChanged())
		{
			this.getWeaponManager().getWeapon().setPositionChanged(false);
			this.net.send(new WeaponPositionPacket(this.getID(), this.getWeaponManager().getWeapon().getCurrentPosition()), Protocol.UDP);
		}

		/** Debug **/
		if (this.getDebugComponent().isParticleSpawn())
			this.particleSystems.add(new ParticlesBlood(Indexer.getUniqueID(), this.getPosition().copy())
					.setParticleVelocity(this.getRaycast().getDirection().copy().normalize().mul(0.05f))
					.setNetwork(this.net));

        if (this.getDebugComponent().isParticleRemove())
        {
            for(int i = 0; i < this.particleSystems.size(); i++)
            {
                ParticleSystem p = this.particleSystems.get(i);
                p.destroy();
                //core.getGame().getEntityManager().remove(p.getID());
            }
            this.particleSystems.clear();
        }

		/* STEP */
		Vec3 block = new Vec3(this.getPosition().copy().add(this.getViewDirection().copy().normalize().mul(1.5f).add(0, 1, 0)));
		if((this.getKeyComponent().isUp() || this.getKeyComponent().isRight() || this.getKeyComponent().isLeft()) && !this.getKeyComponent().isJump() && !this.getKeyComponent().isRun()
				&& core.getGame().getWorld().getBlock((int)block.x, (int)this.getPosition().y - 1, (int)block.z) != 0
				&& core.getGame().getWorld().getBlock((int)block.x, (int)this.getPosition().y, (int)block.z) == 0)
		{
            Rigidbody body = this.getRigidBody().getBody();
            if(body.isGrounded())
                body.applyForce(Vec3.UP, 0.18f);
		}
		if (this.renderSnow)
		{
			if (this.snow == null)
			{
				this.snow = new ParticleSnow(Indexer.getUniqueID(), this.getPosition().copy().add(0, 10, 0));
				GameCore.getInstance().getGame().spawn(this.snow);
			}
			this.snow.setPosition(this.getPosition().copy().add(0, 15, 0).add(this.getTransform().getForward().copy().mul(20, 0, 20)));
		} else if (this.snow != null)
		{
			GameCore.getInstance().getGame().remove(this.snow.getID());
			this.snow = null;
		}
	}

	float walkTimer = 0;
	float walkSide = 1;
	private void playSounds(GameCore core)
	{
		if (this.getKeyComponent().getForwardDirection().magnitude() * this.getRigidBody().getBody().getVelocity().magnitude() >= this.getKeyComponent().getSpeed() && this.getRigidBody().getBody().getVelocity().y > -0.05f)
		{
			if (Mathf.sin(this.walkTimer * this.getKeyComponent().getSpeed() * 10) * this.walkSide >= 0)
			{
				this.walkSide = -this.walkSide;
				core.getGame().spawn(new AudioSource(Sound.FOOTSTEP[(int) Mathf.random(0, 3)]));
				this.net.send(new SoundPacket(this.getID(), new AudioSource(Sound.FOOTSTEP[(int) Mathf.random(0, 3)], this.getPosition())), Protocol.UDP);
			}
			this.walkTimer++;
		}
		if (this.getRigidBody().getBody().isHitGrounded())
			core.getGame().spawn(new AudioSource(Sound.LAND));
	}

	public ECRaycast getRaycast()
	{
		return ((ECRaycast) super.get(EComponent.RAYCAST));
	}

	public ECKeyMovement getKeyComponent()
	{
		return ((ECKeyMovement) super.get(EComponent.KEY_MOVEMENT));
	}

	public ECMouseLook getMouseComponent()
	{
		return ((ECMouseLook) super.get(EComponent.MOUSE_LOOK));
	}

	public ECDebug getDebugComponent()
	{
		return ((ECDebug) super.get(EComponent.DEBUG));
	}

	public ECRigidbody getRigidBody() { return ((ECRigidbody)this.get(EComponent.RIGIDBODY)); }

	public NetworkableClient getNet()
	{
		return this.net;
	}

	public void setNetwork(NetworkableClient net)
	{
		this.net = net;
	}

	public int getLife()
	{
		return this.life;
	}

	public void setLife(int life)
	{
		this.life = life;
	}

	public int getTimeoutTime()
	{
		return this.timeoutTime;
	}

	public void setTimeoutTime(int timeoutTime)
	{
		this.timeoutTime = timeoutTime;
	}

	public boolean isTimedOut()
	{
		return this.timedOut;
	}

	public void setTimedOut(boolean timedOut)
	{
		this.timedOut = timedOut;
	}

	public boolean isKicked()
	{
		return this.kicked;
	}

	public void setKickMessage(String kickMessage) {
		this.kickMessage = kickMessage;
	}

	public String getKickMessage() {
		return this.kickMessage;
	}

	public void setKicked(boolean kicked)
	{
		this.kicked = kicked;
	}

	public boolean isRenderSnow() { return this.renderSnow; }
	public void setRenderSnow(boolean renderSnow) { this.renderSnow = renderSnow; }
}