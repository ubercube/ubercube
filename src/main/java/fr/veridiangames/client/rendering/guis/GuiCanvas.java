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


import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.shaders.GuiShader;
import fr.veridiangames.client.rendering.shaders.Shader;
import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.utils.Color4f;

import java.util.ArrayList;
import java.util.List;

public class GuiCanvas
{
	private GuiCanvas parent;

	private boolean rendered;
	private boolean updated;

	private List<GuiComponent> components;
	private List<GuiCanvas> canvasOverlays;

	private GuiShader shader;
	private boolean enabled;

	public GuiCanvas(GuiCanvas parent)
	{
		this.parent = parent;
		shader = new GuiShader();
		components = new ArrayList<>();
		canvasOverlays = new ArrayList<>();
		rendered = true;
		updated = true;
		enabled = true;
	}
	
	public void update()
	{
		for (int i = 0; i < components.size(); i++)
		{
			if (components.get(i).isUseable())
				components.get(i).update();
		}

		for (int i = 0; i < canvasOverlays.size(); i++)
		{
			if (canvasOverlays.get(i).isUpdated() && canvasOverlays.get(i).isEnabled())
				canvasOverlays.get(i).update();
		}
	}

	public void render(Display display)
	{
		shader.bind();
		shader.setProjectionMatrix(Mat4.orthographic(display.getWidth(), 0, 0, display.getHeight(), -1, 1));
		shader.setColor(Color4f.WHITE);

		if (isRendered())
			for (GuiComponent gui : components)
				if (gui.isUseable())
					gui.renderSteps(shader);

		for (GuiCanvas canvas : canvasOverlays)
			if (canvas.isRendered() && canvas.isEnabled())
				canvas.render(display);

		render(shader);
	}

	public void render(GuiShader shader) {}
	
	public void add(GuiComponent gui) {
		gui.setCanvas(this);
		components.add(gui);
	}
	
	public void remove(GuiComponent gui) {
		components.remove(gui);
	}

	public void addCanvas(GuiCanvas gui) {
		canvasOverlays.add(gui);
	}

	public void removeCanvas(GuiCanvas gui) {
		canvasOverlays.remove(gui);
	}

	public List<GuiComponent> getComponents() {
		return components;
	}
	
	public Shader getShader() {
		return shader;
	}

	public boolean isRendered()
	{
		return rendered;
	}

	public void setRendered(boolean rendered)
	{
		this.rendered = rendered;
	}

	public boolean isUpdated()
	{
		return updated;
	}

	public void setUpdated(boolean updated)
	{
		this.updated = updated;
	}

	public GuiCanvas getParent() { return parent; }

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}
}