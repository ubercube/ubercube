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

package fr.veridiangames.core.game.entities.weapons.fire_weapons;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.bullets.Bullet;
import fr.veridiangames.core.game.entities.weapons.Weapon;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Indexer;

public class FireWeapon extends Weapon
{
	private int fireFrequency = 10; // per seconds
	
	private Transform shootPoint;
	private boolean shooting;
	private boolean shot;
	private float shootForce;
	
	private int shootTimer = 0;
	
	public FireWeapon(int model)
	{
		super(model);
		this.shootPoint = new Transform();
		this.setShootForce(2);
	}
	
	public void update(GameCore core)
	{
		super.update(core);
		super.updateWeaponVelocity(0, 0);
		
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
		Bullet bullet = new Bullet(Indexer.getUniqueID(), "", this.shootPoint.getPosition(), this.transform.getRotation(), shootForce);
		bullet.setNetwork(net);
		core.getGame().spawn(bullet);
	}
	
	public void shoot()
	{
		shooting = true;
		if (shot)
			return;
		
		Vec3 shootVector = new Vec3(transform.getLocalPosition()).sub(transform.getLocalRotation().getForward().copy().mul(0, 0, 0.2f));
		this.transform.setLocalPosition(shootVector);
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

	public void setShootForce(float shootForce)
	{
		this.shootForce = shootForce;
	}

	public void setShootPoint(Transform shootPoint)
	{
		this.shootPoint = shootPoint;
		this.shootPoint.setParent(this.transform);
	}
}
