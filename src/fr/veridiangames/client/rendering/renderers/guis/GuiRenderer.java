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

import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.client.guis.GuiManager;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.shaders.GuiShader;

public class GuiRenderer
{
	private GuiManager			guiManager;
	private GuiCanvasRenderer	canvasRenderer;
	private GuiShader			shader;

	public GuiRenderer(GuiManager guiManager)
	{
		this.guiManager = guiManager;
		this.shader = new GuiShader();
		this.canvasRenderer = new GuiCanvasRenderer();
	}
	
	public void render(Display display)
	{
		shader.bind();
		shader.setOrtho(display.getWidth(), 0, 0, display.getHeight(), -1, 1);
		shader.setModelViewMatrix(Mat4.identity());
		shader.setColor(1, 1, 1, 1);
		
		canvasRenderer.setCanvas(guiManager.getCanvasID(), guiManager.getCanvas());
		canvasRenderer.render(shader);
	}
}
