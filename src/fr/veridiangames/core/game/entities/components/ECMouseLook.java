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

package fr.veridiangames.core.game.entities.components;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.maths.Mathf;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;

/**
 * Created by Marccspro on 31 janv. 2016.
 */
public class ECMouseLook extends EComponent
{
	private float	dx, dy;
	private float 	rotAmnt;
	private float	speed;

	public ECMouseLook(float speed)
	{
		super(MOUSE_LOOK);

		this.speed = speed;
		this.rotAmnt = 0;
	}

	public void update()
	{
		Transform transform = ((ECRender) parent.get(EComponent.RENDER)).getTransform();

		float DY = dy * speed;

		if (rotAmnt + DY > 89)
		{
			DY = 89 - rotAmnt;
			rotAmnt = 89;

		}
		else if (rotAmnt + DY < -89)
		{
			DY = -89 - rotAmnt;
			rotAmnt = -89;
		}
		else
			rotAmnt += DY;

		transform.rotate(Vec3.UP, dx * speed);
		transform.rotate(transform.getRight(), DY);
	}

	public void setDX(float dx)
	{
		this.dx = dx;
	}
	
	public void setDY(float dy)
	{
		this.dy = dy;
	}

	public float getDx()
	{
		return dx;
	}

	public float getDy()
	{
		return dy;
	}
}
