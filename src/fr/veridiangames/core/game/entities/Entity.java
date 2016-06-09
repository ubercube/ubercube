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

package fr.veridiangames.core.game.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.components.EComponent;

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
		for (int i = 0; i < keys.size(); i++)
		{
			EComponent e = get(keys.get(i));
			e.init(core);
		}
	}
	
	public void update(GameCore core)
	{
		if (destroyed)
			return;
			
		for (int i = 0; i < keys.size(); i++)
		{
			EComponent e = get(keys.get(i));
			e.update(core);
		}
	}
	
	public boolean contains(int component)
	{
		return components.containsKey(component);
	}
	
	public EComponent get(int component)
	{
		if (!components.containsKey(component))
			return null;
		
		return components.get(component);
	}
	
	public void add(EComponent e)
	{
		e.setParent(this); 
		keys.add(e.getID());
		components.put(e.getID(), e);
	}

	public void remove(int component)
	{
		keys.remove((Integer) component);
		components.remove(component);
	}

	public Map<Integer, EComponent> getComponents()
	{
		return components;
	}
	
	public boolean isDestroyed()
	{
		return destroyed;
	}
	
	public void destroy()
	{
		this.destroyed = true;
	}
	
	public void addTag(String tag)
	{
		tags.add(tag);
	}
	
	public List<String> getTags()
	{
		return tags;
	}
	
	public int getID()
	{
		return id;
	}

	public GameCore getCore()
	{
		return core;
	}


}
