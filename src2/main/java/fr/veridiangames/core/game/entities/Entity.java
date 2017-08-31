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

package fr.veridiangames.core.game.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.components.ECRender;
import fr.veridiangames.core.game.entities.components.ECRigidbody;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.game.world.Chunk;
import fr.veridiangames.core.maths.Vec3;

/**
 * Created by Marccspro on 30 janv. 2016.
 */
public class Entity
{
	protected int						id;
	protected boolean					destroyed;
	private List<Integer>				keys;
	private Map<Integer, EComponent>	components;
	private List<String>				tags;
	private GameCore 					core;

	public Entity(int id)
	{
		this.id = id;
		this.destroyed = false;
		this.keys = new ArrayList<Integer>();
		this.components = new HashMap<Integer, EComponent>();
		this.tags = new ArrayList<String>();
	}

	public void init(GameCore core)
	{
		this.core = core;
		for (int i = 0; i < this.keys.size(); i++)
		{
			EComponent e = this.get(this.keys.get(i));
			e.init(core);
		}
	}

	public void update(GameCore core)
	{
		if (this.destroyed)
			return;
		for (int i = 0; i < this.keys.size(); i++)
		{
			EComponent e = this.get(this.keys.get(i));
			e.update(core);
		}
	}

	public boolean outOfMap()
	{
		if (this.contains(EComponent.RIGIDBODY))
		{
			int m0 = 0;
			int m1 = this.core.getGame().getData().getWorldSize() * Chunk.SIZE;
			Vec3 p = ((ECRigidbody) this.get(EComponent.RIGIDBODY)).getBody().getPosition();

			if (p.x < m0 ||p.z < m0 || p.x > m1 || p.z > m1)
				return true;
			if (p.y < 0 || p.y > 100)
				return true;
		}
		else if (this.contains(EComponent.RENDER))
		{
			int m0 = 0;
			int m1 = this.core.getGame().getData().getWorldSize() * Chunk.SIZE;
			Vec3 p = ((ECRender) this.get(EComponent.RENDER)).getTransform().getPosition();

			if (p.x < m0 ||p.z < m0 || p.x > m1 || p.z > m1)
				return true;
			if (p.y < 0 || p.y > 100)
				return true;
			return false;
		}
		return false;
	}

	public boolean contains(int component)
	{
		return this.components.containsKey(component);
	}

	public EComponent get(int component)
	{
		if (!this.components.containsKey(component))
			return null;

		return this.components.get(component);
	}

	public void add(EComponent e)
	{
		e.setParent(this);
		this.keys.add(e.getID());
		this.components.put(e.getID(), e);
	}

	public void remove(int component)
	{
		this.keys.remove((Integer) component);
		this.components.remove(component);
	}

	public Map<Integer, EComponent> getComponents()
	{
		return this.components;
	}

	public boolean isDestroyed()
	{
		return this.destroyed;
	}

	public void destroy()
	{
		this.destroyed = true;
	}

	public void addTag(String tag)
	{
		this.tags.add(tag);
	}

	public List<String> getTags()
	{
		return this.tags;
	}

	public int getID()
	{
		return this.id;
	}

	public GameCore getCore()
	{
		return this.core;
	}


}
