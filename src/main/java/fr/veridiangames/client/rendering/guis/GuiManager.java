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

package fr.veridiangames.client.rendering.guis;


import java.util.ArrayList;
import java.util.List;

import fr.veridiangames.client.rendering.Display;

public class GuiManager {
	private List<GuiCanvas> canvases;
	private int currentCanvas;

	public GuiManager() {
		canvases = new ArrayList<>();
		currentCanvas = 0;
	}
	
	public void update() {
		if (currentCanvas != -1)
			canvases.get(currentCanvas).update();
	}

	public void render(Display display) {
		if (currentCanvas != -1)
			canvases.get(currentCanvas).render(display);
	}

	public void add(GuiCanvas canvas)
	{
		canvases.add(canvas);
	}

	public List<GuiCanvas> getCanvases() {
		return canvases;
	}

	public void setCanvas(int canvasID)
	{
		currentCanvas = canvasID;
		if (currentCanvas > canvases.size() - 1)
			currentCanvas = -1;
	}

	public int getCanvas()
	{
		return currentCanvas;
	}
}