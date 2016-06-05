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

package fr.veridiangames.client.guis;

import java.util.ArrayList;
import java.util.List;

public class GuiManager
{
	private int 			canvas;
	private List<GuiCanvas> guis;
	
	public GuiManager()
	{
		this.canvas = 0;
		this.guis = new ArrayList<GuiCanvas>();
	}
	
	public void add(GuiCanvas gui)
	{
		guis.add(gui);
	}
	
	public void update()
	{	
		GuiCanvas gui = guis.get(canvas);
		gui.update();
	}
	
	public List<GuiCanvas> getGuis()
	{
		return guis;
	}

	public void setGuis(List<GuiCanvas> guis)
	{
		this.guis = guis;
	}
	
	public void setCanvas(int canvas)
	{
		this.canvas = canvas;
	}
	
	public GuiCanvas getCanvas()
	{
		return guis.get(canvas);
	}
	
	public int getCanvasID()
	{
		return canvas;
	}
}