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

public class GuiCheckBox extends GuiComponent
{

	private GuiLabel label;
	public boolean triggered = false;

	public GuiCheckBox(String text, int x, int y, boolean triggered) {
		super(x, y, 18, 18, new Color4f(0, 0, 0, 0.5f));

		this.triggered = triggered;
		this.label = new GuiLabel(text, x + 8, y);
		this.label.setColor(new Color4f(0, 0, 0, 0.75f));
		this.w = this.w + this.label.getTextWidth() + 8;
		this.label.setOrigin(GuiOrigin.A);
	}

	@Override
	public void update() {
		super.update();
		if (mouseInUse) return;

		this.label.update();
		this.label.setPosition(this.x + 18 + 8, this.y - this.label.getTextHeight() / 2 + this.h/2);

		if (this.mouseButtonUp && this.mouseIn) this.triggered = !this.triggered;
	}

	@Override
	public void render(GuiShader shader) {
		shader.setColor(this.color);
		StaticPrimitive.quadPrimitive().render(shader, this.x + 18 / 2, this.y + this.h / 2, 0, 9, 9, 0);
		shader.setColor(Color4f.WHITE);
		StaticPrimitive.quadPrimitive().render(shader, this.x + 18 / 2, this.y + this.h / 2, 0, 6, 6, 0);

		if (this.mouseIn) {
			shader.setColor(Color4f.LIGHT_GRAY);
			StaticPrimitive.quadPrimitive().render(shader, this.x + 18 / 2, this.y + this.h / 2, 0, 4, 4, 0);
			if (this.mouseButtonPressed) {
				shader.setColor(Color4f.GRAY);
				StaticPrimitive.quadPrimitive().render(shader, this.x + 18 / 2, this.y + this.h / 2, 0, 4, 4, 0);
			}
		}

		if (this.triggered) {
			shader.setColor(Color4f.DARK_GREEN);
			StaticPrimitive.quadPrimitive().render(shader, this.x + 18 / 2, this.y + this.h / 2, 0, 4, 4, 0);
			if (this.mouseIn) {
				shader.setColor(Color4f.LIGHT_GRAY);
				StaticPrimitive.quadPrimitive().render(shader, this.x + 18 / 2, this.y + this.h / 2, 0, 4, 4, 0);
				if (this.mouseButtonPressed) {
					shader.setColor(Color4f.GRAY);
					StaticPrimitive.quadPrimitive().render(shader, this.x + 18 / 2, this.y + this.h / 2, 0, 4, 4, 0);
				}
			}
		}

		this.label.render(shader);
	}

	@Override
	public void dispose() {

	}

	public GuiLabel getLabel() {
		return this.label;
	}
}
