/*
 * Copyright (C) 2016 Team Ubercube
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
 *       along with Ubercube.  If not, see http://www.gnu.org/licenses/.
 */

package fr.veridiangames.client.rendering.renderers.guis;

import java.util.List;

import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.client.guis.GuiCanvas;
import fr.veridiangames.client.guis.components.GuiComponent;
import fr.veridiangames.client.rendering.shaders.GuiShader;

/**
 * Created by Marccspro on 31 mars 2016.
 */
public class GuiCanvasRenderer
{
	private int					canvasID;
	private GuiCanvas			canvas;
	private GuiStaticRenderer	staticRenderer;  

	private List<GuiComponent>	staticDraws;
	private List<GuiComponent>	dynamicDraws;

	public GuiCanvasRenderer()
	{
		staticRenderer = new GuiStaticRenderer();
	}

	public void setCanvas(int canvasID, GuiCanvas canvas)
	{
		this.canvasID = canvasID;
		this.canvas = canvas;
		this.staticDraws = canvas.getStaticDraws();
		this.dynamicDraws = canvas.getDynamicDraws();

		initCanvas();
	}

	public void initCanvas()
	{
		 staticRenderer.set(staticDraws, canvasID);
	}

	public void render(GuiShader shader)
	{
		shader.setColor(Color4f.WHITE);
		shader.enableVColor(true);
		renderStatics();
		shader.enableVColor(false);
		renderDynamics(shader);
	}

	public void renderStatics()
	{
		staticRenderer.render();
	}

	public void renderDynamics(GuiShader shader)
	{
		for (int i = 0; i < dynamicDraws.size(); i++)
		{
			GuiComponent gui = dynamicDraws.get(i);
			shader.setColor(gui.getColor());
			gui.render(shader);
		}
	}
}
