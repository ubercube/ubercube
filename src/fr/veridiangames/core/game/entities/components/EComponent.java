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

import java.util.ArrayList;
import java.util.List;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.exceptions.EComponentException;
import fr.veridiangames.core.game.entities.Entity;

/**
 * Created by Marccspro on 31 janv. 2016.
 */
public abstract class EComponent
{
//	public static final int	POSITION		= 0x01;
//	public static final int	ROTATION		= 0x02;
	public static final int	KEY_MOVEMENT	= 0x03;
	public static final int	MOUSE_LOOK		= 0x04;
	public static final int	RENDER			= 0x05;
	public static final int	MODEL			= 0x06;
	public static final int	GRAVITY			= 0x07;
	public static final int	COLLISION_H		= 0x08;
	public static final int	JUMP			= 0x09;
	public static final int	NETWORK			= 0x10;
	public static final int	NAME			= 0x11;
	public static final int	RIGIDBODY		= 0x12;
	public static final int	RAYCAST			= 0x13;
	public static final int	WEAPON			= 0x14;
	
	protected int				id;
	protected Entity			parent;
	protected List<Integer>		dependencies;
	protected boolean 			enabled;
	
	public EComponent(int id)
	{
		this.id = id;
		this.dependencies = new ArrayList<Integer>();
		this.enabled = true;
	}
	
	public void init(GameCore core)
	{
	}
	
	public void update(GameCore core)
	{
	}

	public int getID()
	{
		return id;
	}
	
	public void addDependencies(int... componentID)
	{
		for (int i = 0; i < componentID.length; i++)
		{
			dependencies.add(componentID[i]);			
		}
	}
	
	public void setParent(Entity parent)
	{
		this.parent = parent;
		for (int i : dependencies)
		{
			if (!parent.contains(i))
			{
				throw new EComponentException("Not enough data for component: " + Integer.toHexString(i));
			}
		}
	}

	public Entity getParent()
	{
		return parent;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
}
