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

	private Texture texture;
	Color4f borderColor;
	int borderSize = 0;

	public GuiPanel(int x, int y, int w, int h) {
		super(x, y, w, h, Color4f.WHITE);
		this.setOrigin(GuiComponent.GuiOrigin.A);
	}

	@Override
	public void update() {
		super.update();
	}

	public void setBorder(Color4f color, int size) {
		this.borderColor = color;
		this.borderSize = size;
	}

	@Override
	public void render(GuiShader shader) {
		if (this.borderSize > 0) {
			shader.setColor(this.borderColor);
			StaticPrimitive.quadPrimitive().render(shader, this.x + this.w/2, this.y + this.h/2, 0, this.w/2 + this.borderSize, this.h/2 + this.borderSize, 0);
		}
		shader.setColor(this.color);
		if (this.texture != null) this.texture.bind(shader);
		StaticPrimitive.quadPrimitive().render(shader, this.x + this.w/2, this.y + this.h/2, 0, this.w/2, this.h/2, 0);
		if (this.texture != null) Texture.unbind(shader);
	}

	@Override
	public void dispose() {

	}

	public Texture getTexture()
	{
		return this.texture;
	}

	public void setTexture(Texture texture)
	{
		this.texture = texture;
	}
}