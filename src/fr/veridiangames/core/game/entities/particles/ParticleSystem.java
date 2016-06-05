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
 *     along with Ubercube.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.veridiangames.core.game.entities.particles;

import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.components.ECName;
import fr.veridiangames.core.game.entities.components.FuckedECPosition;
import fr.veridiangames.core.game.entities.components.FuckedECRotation;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec2;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;

public class ParticleSystem extends Entity
{
	private Transform	transform;
	
	private int			particleCount;
	
	private int 		particleLifeTime;
	private int			particleLifeTimeRandomness;
	
	private float 		particleSize;
	private float 		particleSizeRandomness;
	
	private Color4f 	particleColor;
	private Color4f		particleColorRandomness;
	
	
	
	public ParticleSystem(int id, String name)
	{
		super(id);
		super.add(new ECName(""));
		super.add(new FuckedECPosition(new Vec3()));
		super.add(new FuckedECRotation(new Quat()));
	}

	public void setPosition(Vec3 position)
	{
		transform.setLocalPosition(position);
	}

	public void setRotation(Quat rotation)
	{
		transform.setLocalRotation(rotation);
	}

	public void setParent(Transform parent)
	{
		transform.setParent(parent);
	}
}