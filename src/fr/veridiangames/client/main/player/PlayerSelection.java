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

package fr.veridiangames.client.main.player;

import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.components.ECRaycast.RaycastHit;
import fr.veridiangames.core.game.entities.components.ECRender;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.maths.Vec3i;

/**
 * Created by Marccspro on 9 avr. 2016.
 */
public class PlayerSelection
{
	private RaycastHit		hit;
	private Vec3			pos;
	private Vec3			size;
	
	public PlayerSelection()
	{
	}
	
	public void update(RaycastHit hit)
	{
		if (this.hit == hit)
			return;
		
		this.hit = hit;
		if (hit != null)
		{
			if (hit.getEntity() != null)
			{
				Entity e = hit.getEntity();
				if (e.contains(EComponent.RENDER))
				{
					Vec3 ePos = ((ECRender) e.get(EComponent.RENDER)).getTransform().getPosition();
					Vec3 eSize = ((ECRender) e.get(EComponent.RENDER)).getSize();
					this.pos = new Vec3(ePos);
					this.size = new Vec3(eSize).add(0.01f);
				}
			}
			else if (hit.getBlock() != 0)
			{
				Vec3i bPos = hit.getBlockPosition();
				this.pos = new Vec3(bPos).add(0.5f);
				this.size = new Vec3(0.51f);
			}
		}
	}
	
	public RaycastHit getHit()
	{
		return hit;
	}

	public Vec3 getPos()
	{
		return pos;
	}

	public Vec3 getSize()
	{
		return size;
	}
	
	public boolean hasSelection()
	{
		if (hit == null)
			return false;
		
		return hit.getEntity() != null || hit.getBlock() != 0;
	}
}
