/*
 *   Copyright (C) 2016 Team Ubercube
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
 *       along with Ubercube.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.veridiangames.core.game.entities.components;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;

/**
 * Created by Marccspro on 7 f�vr. 2016.
 */
public class ECRender extends EComponent
{
	private Transform 	transform;
	private Transform	eyePoint;
	private Vec3 		size;
	
	public ECRender(Vec3 position, Quat rotation, Vec3 size)
	{
		super(RENDER);
		this.size = size;
		this.transform = new Transform(position, rotation, size);
		this.eyePoint = new Transform();
	}

	public void update(GameCore core)
	{

		this.eyePoint.setLocalPosition(transform.getPosition().copy().add(0, 2.5f / 2.0f, 0));
		this.eyePoint.setLocalRotation(transform.getRotation());
	}
	
	public Transform getTransform()
	{
		return transform;
	}

	public void setTransform(Transform transform)
	{
		this.transform = transform;
	}
	
	public Transform getEyeTransform()
	{
		return eyePoint;
	}

	public void setEyeTransform(Transform transform)
	{
		this.eyePoint = transform;
	}
	
	public Vec3 getSize()
	{
		return size;
	}

	public void setSize(Vec3 size)
	{
		this.size = size;
	}
}
