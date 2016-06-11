/*
 * Copyright (C) 2016 Team Ubercube
 *
 *   This file is part of Ubercube.
 *
 *       Ubercube is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Ubercube is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Ubercube.  If not, see http://www.gnu.org/licenses/.
 */

package fr.veridiangames.core.game.entities.bullets;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.components.ECName;
import fr.veridiangames.core.game.entities.components.ECRender;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;

/**
 * Created by Marccspro on 18 mai 2016.
 */
public class StaticBullet extends Entity
{
	protected Entity parent;

	public StaticBullet(int id, String name, Vec3 spawnPoint, Quat orientation)
	{
		super(id);
		super.add(new ECName(name));
		super.add(new ECRender(spawnPoint, orientation, new Vec3(0.04f, 0.04f, 0.4f)));
		super.addTag("Bullet");
	}
	
	public void update(GameCore core)
	{
		super.update(core);
	}
	
	public Transform getTransform()
	{
		return ((ECRender) this.get(EComponent.RENDER)).getTransform();
	}
	
	public Vec3 getPosition()
	{
		return getTransform().getPosition();
	}
	
	public Quat getRotation()
	{
		return getTransform().getRotation();
	}

	public Entity getParent()
	{
		return parent;
	}
}
