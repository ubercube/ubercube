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
	public static final int	KEY_MOVEMENT	= 3;
	public static final int	MOUSE_LOOK		= 4;
	public static final int	RENDER			= 5;
	public static final int	MODEL			= 6;
	public static final int	GRAVITY			= 7;
	public static final int	COLLISION_H		= 8;
	public static final int	JUMP			= 9;
	public static final int	NETWORK			= 10;
	public static final int	NAME			= 11;
	public static final int	RIGIDBODY		= 12;
	public static final int	RAYCAST			= 13;
	public static final int	WEAPON			= 14;
	public static final int DEBUG			= 15;
	public static final int AUDIO_SOURCE	= 16;

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
		return this.id;
	}

	public void addDependencies(int... componentID)
	{
		for (int i = 0; i < componentID.length; i++)
			this.dependencies.add(componentID[i]);
	}

	public void setParent(Entity parent)
	{
		this.parent = parent;
		for (int i : this.dependencies)
			if (!parent.contains(i))
				throw new EComponentException("Not enough data for component: " + Integer.toHexString(i));
	}

	public Entity getParent()
	{
		return this.parent;
	}

	public boolean isEnabled()
	{
		return this.enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
}
