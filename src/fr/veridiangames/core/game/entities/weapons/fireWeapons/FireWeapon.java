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

package fr.veridiangames.core.game.entities.weapons.fireWeapons;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.audio.Sound;
import fr.veridiangames.core.game.entities.audio.AudioSource;
import fr.veridiangames.core.game.entities.bullets.Bullet;
import fr.veridiangames.core.game.entities.components.ECKeyMovement;
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.game.entities.weapons.Weapon;
import fr.veridiangames.core.maths.Mathf;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.Protocol;
import fr.veridiangames.core.network.packets.BulletShootPacket;
import fr.veridiangames.core.utils.Indexer;

public class FireWeapon extends Weapon
{
	private int fireFrequency = 10; // per seconds
	
	private Transform shootPoint;
	private boolean shooting;
	private boolean shot;
	private float shootForce;
	private float shootPecision;
	
	private int shootTimer = 0;

	private int maxBullets;
	private int bulletsLeft;

	private float audioGain;
	
	public FireWeapon(int model)
	{
		super(model);
		this.shootPoint = new Transform();
		this.setShootForce(2);
		this.maxBullets = 30;
		this.bulletsLeft = maxBullets;
		this.runRotation = new Vec3(10f, -20f, 0);
		this.shootPecision = 0.02f;
		this.audioGain = 1.0f;
	}
	
	public void update(GameCore core)
	{
		super.update(core);
		ClientPlayer player = core.getGame().getPlayer();
		if (holder.getID() == player.getID() && !zoomed)
		{
			ECKeyMovement movement = player.getKeyComponent();
			float dx = player.getMouseComponent().getDx();
			float dy = player.getMouseComponent().getDy();
			Vec3 movementVelocity = new Vec3(movement.getVelocity(1)).mul(1, 0, 1);
			if (movement.isRun())
			{
				super.updateBobbing(movementVelocity.magnitude(), 0.2f, 0.3f);
				super.updateRunPosition();
			}
			else
			{
				super.updateBobbing(movementVelocity.magnitude(), 0.15f, 0.2f);
				super.updateWeaponVelocity(movement.getVelocity(1), dx, dy, 0.0008f);
			}
		}

		if (shooting)
		{
			if (!shot)
			{
				shot = true;
				shootTimer = 0;
				shootBullet(core);
			}
			
			if (shootTimer > 60 / fireFrequency)
			{
				shot = false;
			}
		}
		if (shot)
		{
			shootTimer++;
		}
		shooting = false;
	}
	
	private void shootBullet(GameCore core)
	{
		Vec3 shootPosition = this.holder.getEyePosition().copy().add(holder.getTransform().getForward());
		Bullet bullet = new Bullet(Indexer.getUniqueID(), holder.getID(), "", this.shootPoint.getPosition(), this.transform.getRotation(), shootForce);
		net.send(new BulletShootPacket(holder.getID(), bullet), Protocol.UDP);
		bullet.setNetwork(net);
		core.getGame().spawn(bullet);

		this.rotationFactor.add(-0.1f, 0, 0);
	}
	
	public void onAction()
	{
		shoot();
	}

	public void onActionUp()
	{
	}

	public void onActionDown()
	{
	}

	public void shoot()
	{
		shooting = true;
		if (shot)
			return;

		Vec3 shootVector = new Vec3(transform.getLocalPosition()).sub(transform.getLocalRotation().getForward().copy().mul(0, 0, 0.2f));
		this.transform.setLocalPosition(shootVector);
		this.removeBullet();
		this.holder.getCore().getGame().spawn(new AudioSource(Sound.AK47_SHOOT, audioGain));
		if (!zoomed)
		{
			this.rotationFactor.add(Mathf.random(-shootPecision, shootPecision), Mathf.random(-shootPecision, shootPecision), 0);
		}
	}

	private void removeBullet()
	{
		bulletsLeft--;

		if (bulletsLeft < 0)
		{
			bulletsLeft = 0;
			reloadBullets();
		}
	}

	public void reloadBullets()
	{
		bulletsLeft = maxBullets;
	}

	public int getFireFrequency()
	{
		return fireFrequency;
	}

	public void setFireFrequency(int fireFrequency)
	{
		this.fireFrequency = fireFrequency;
	}

	public Transform getShootPoint()
	{
		return shootPoint;
	}

	public float getShootForce()
	{
		return shootForce;
	}

	public float getAudioGain() { return audioGain; }

	public void setShootForce(float shootForce)
	{
		this.shootForce = shootForce;
	}

	public void setShootPoint(Transform shootPoint)
	{
		this.shootPoint = shootPoint;
		this.shootPoint.setParent(this.transform);
	}

	public int getMaxBullets()
	{
		return maxBullets;
	}

	public void setMaxBullets(int maxBullets)
	{
		this.maxBullets = maxBullets;
	}

	public int getBulletsLeft()
	{
		return bulletsLeft;
	}

	public void setBulletsLeft(int bulletsLeft)
	{
		this.bulletsLeft = bulletsLeft;
	}

	public void setAudioGain(float audioGain) { this.audioGain = audioGain; }
}
