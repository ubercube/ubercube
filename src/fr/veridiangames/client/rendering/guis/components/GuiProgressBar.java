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

package fr.veridiangames.client.rendering.guis.components;


import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.primitives.StaticPrimitive;
import fr.veridiangames.client.rendering.shaders.GuiShader;
import fr.veridiangames.core.utils.Color4f;

public class GuiProgressBar extends GuiComponent
{
	
	private float normalizedValue;
	private float min = 0, max = 1, value = 0.5f;
	
	public Color4f bgColor;
	
	public GuiProgressBar(int x, int y, int w) {
		this(x, y, w, 30);
	}
	
	public GuiProgressBar(int x, int y, int w, int h) {
		super(x, y, w, h, Color4f.WHITE);
		
		bgColor = new Color4f(0, 0, 0, 0.5f);
	}
	
	public void update() {
		super.update();
		
		normalizedValue = (value - min) / (max - min);
	}

	public void render(GuiShader shader) {
		shader.setColor(bgColor);
		StaticPrimitive.quadPrimitive().render(shader, x, y, 0, w/2, h/2, 0);
		shader.setColor(color);
		StaticPrimitive.quadPrimitive().render(shader, x + (normalizedValue / 2) * w - w/2, y, 0, w/2 * normalizedValue - 4, h/2 - 4, 0);
	}

	public void dispose() {
		
	}

	public void setMin(float min) {
		this.min = min;
	}

	public void setMax(float max) {
		this.max = max;
	}

	public void setValue(float value) {
		this.value = value;
	}
	
	public float getMin() {
		return min;
	}
	
	public float getMax() {
		return max;
	}
	
	public float getValue() {
		return value;
	}
}	

