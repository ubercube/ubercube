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

package fr.veridiangames.client.guis;

import java.util.ArrayList;
import java.util.List;

import fr.veridiangames.client.guis.components.GuiComponent;

/**
 * Created by Marccspro on 31 mars 2016.
 */
public class GuiCanvas
{
	private boolean terminated;
	
	private List<GuiComponent> guis;
	private List<GuiComponent> staticDraws;
	private List<GuiComponent> dynamicDraws;
	
	public GuiCanvas()
	{
		this.terminated = false;
		this.guis = new ArrayList<GuiComponent>();
		this.staticDraws = new ArrayList<GuiComponent>();
		this.dynamicDraws = new ArrayList<GuiComponent>();
	}
	
	public void update()
	{
		for (int i = 0; i < guis.size(); i++)
		{
			GuiComponent e = guis.get(i);
			e.update();
		}
	}
	
	public void add(GuiComponent gui)
	{
		guis.add(gui);

		if (gui.isDynamic())
			dynamicDraws.add(gui);
		else
			staticDraws.add(gui);
	}
	
	public List<GuiComponent> getStaticDraws()
	{
		return staticDraws;
	}

	public List<GuiComponent> getDynamicDraws()
	{
		return dynamicDraws;
	}

	public void terminate()
	{
		this.terminated = true;
	}
	
	public boolean isTerminated()
	{
		return terminated;
	}
}