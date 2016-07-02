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
import fr.veridiangames.client.rendering.textures.Texture;
import fr.veridiangames.core.utils.Color4f;


public class GuiPanel extends GuiComponent {
	
	public Texture texture;
	Color4f borderColor;
	int borderSize = 0;
	
	public GuiPanel(int x, int y, int w, int h) {
		super(x, y, w, h, Color4f.WHITE);
		setOrigin(GuiComponent.GuiOrigin.A);
	}
	
	public void update() {
		super.update();
	}
	
	public void setBorder(Color4f color, int size) {
		this.borderColor = color;
		this.borderSize = size;
	}
	
	public void render(GuiShader shader) {
		if (borderSize > 0) {
			shader.setColor(borderColor);
			StaticPrimitive.quadPrimitive().render(shader, x + w/2, y + h/2, 0, w/2 + borderSize, h/2 + borderSize, 0);
		}
		shader.setColor(color);
		if (texture != null) texture.bind(shader); 
		StaticPrimitive.quadPrimitive().render(shader, x + w/2, y + h/2, 0, w/2, h/2, 0);
		if (texture != null) Texture.unbind(shader);
	}
	
	public void dispose() {
		
	}
}