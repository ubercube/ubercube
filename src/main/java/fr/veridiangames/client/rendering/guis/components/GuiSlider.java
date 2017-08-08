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

import fr.veridiangames.client.inputs.Mouse;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.primitives.StaticPrimitive;
import fr.veridiangames.client.rendering.shaders.GuiShader;
import fr.veridiangames.core.utils.Color4f;

public class GuiSlider extends GuiComponent
{
	public Color4f bgColor;
	public Color4f bgColorIdle, bgColorHover;
	private int xSlider = 0;
	private GuiLabel label;
	
	private float value = 0; 
	private int textWidth;
	private boolean centerText = false;
	private boolean inUse = false;
	
	public GuiSlider(String text, int x, int y, int w) {
		this(text, x, y, w, 30);
	}
	
	public GuiSlider(String text, int x, int y, int w, int h) {
		super(x, y, w, h, Color4f.WHITE);
		
		bgColor = new Color4f(0, 0, 0, 0.5f);
		bgColorIdle = new Color4f(0, 0, 0, 0.6f);
		bgColorHover = Color4f.WHITE;
		
		label = new GuiLabel(text, 0, 0);
		label.setOrigin(GuiOrigin.A);
		label.setColor(new Color4f(1, 1, 1, 0.4f));
		label.setPosition(this.x + w/2 - label.getTextWidth(), this.y + h/2 - label.getTextHeight() / 2);
		
		textWidth = label.getTextWidth();
	}
	
	public void update() {
		if (mouseInUse && !inUse) {
			return;
		}
		
		if (centerText) textWidth = label.getTextWidth() / 2;
		
		super.update();
		label.update();
		label.setPosition(x + w/2 - textWidth, y + h/2 - label.getTextHeight() / 2);
		
		if (mouseButtonPressed) {
			xSlider = Display.getInstance().getInput().getMouse().getX() - x - w /2;
			mouseInUse = true;
			inUse = true;
		}else {
			inUse = false;
			mouseInUse = false;			
		}
		
		if (xSlider < -w/2 + 10) xSlider = -w/2 + 10;
		if (xSlider > w/2 - 10) xSlider = w/2 - 10;
		
		value = ((float) (xSlider + w/2) - 10) / (w - 20);
		
		if (mouseIn || mouseButtonPressed) {
			color = bgColorHover;
		}else {
			color = bgColorIdle;
		}
	}

	public void render(GuiShader shader) {
		shader.setColor(bgColor);
		StaticPrimitive.quadPrimitive().render(shader, x + w/2, y + h/2, 0, w/2, h/2, 0);
		
		label.render(shader);
		
		shader.setColor(color);
		StaticPrimitive.quadPrimitive().render(shader, x + w/2 + xSlider, y + h/2, 0, 6, h/2 - 4, 0);
	}

	public void dispose() {
		
	}
	
	public float getValue() {
		return value;
	}
	
	public GuiLabel getLabel() {
		return label;
	}
	
	public void setValue(float value) {
		xSlider = (int) (value * (float)w - (float) w / 2.0f);		
	}
	
	public void centerText() {
		centerText = true;
	}
}