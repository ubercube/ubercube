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
import fr.veridiangames.client.rendering.shaders.GuiShader;
import fr.veridiangames.client.rendering.shaders.Shader;
import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.utils.Color4f;

public class GuiCanvas
{
	private GuiCanvas parent;

	private boolean rendered;
	private boolean updated;

	private List<GuiComponent> components;
	private List<GuiCanvas> canvasOverlays;

	private GuiShader shader;

	public GuiCanvas(GuiCanvas parent)
	{
		this.parent = parent;
		this.shader = new GuiShader();
		this.components = new ArrayList<>();
		this.canvasOverlays = new ArrayList<>();
		this.rendered = true;
		this.updated = true;
	}

	public void update()
	{
		for (int i = 0; i < this.components.size(); i++)
			if (this.components.get(i).isUseable())
				this.components.get(i).update();

		for (int i = 0; i < this.canvasOverlays.size(); i++)
			if (this.canvasOverlays.get(i).isUpdated())
				this.canvasOverlays.get(i).update();
	}

	public void render(Display display)
	{
		this.shader.bind();
		this.shader.setProjectionMatrix(Mat4.orthographic(display.getWidth(), 0, 0, display.getHeight(), -1, 1));
		this.shader.setColor(Color4f.WHITE);

		if (this.isRendered())
			for (GuiComponent gui : this.components)
				if (gui.isUseable())
					gui.renderSteps(this.shader);

		for (GuiCanvas canvas : this.canvasOverlays)
			if (canvas.isRendered())
				canvas.render(display);

		this.render(this.shader);
	}

	public void render(GuiShader shader) {}

	public void add(GuiComponent gui) {
		this.components.add(gui);
	}

	public void remove(GuiComponent gui) {
		this.components.remove(gui);
	}

	public void addCanvas(GuiCanvas gui) {
		this.canvasOverlays.add(gui);
	}

	public void removeCanvas(GuiCanvas gui) {
		this.canvasOverlays.remove(gui);
	}

	public List<GuiComponent> getComponents() {
		return this.components;
	}

	public Shader getShader() {
		return this.shader;
	}

	public boolean isRendered()
	{
		return this.rendered;
	}

	public void setRendered(boolean rendered)
	{
		this.rendered = rendered;
	}

	public boolean isUpdated()
	{
		return this.updated;
	}

	public void setUpdated(boolean updated)
	{
		this.updated = updated;
	}

	public GuiCanvas getParent() { return this.parent; }
}