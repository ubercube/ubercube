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

import fr.veridiangames.client.audio.AudioSystem;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.primitives.StaticPrimitive;
import fr.veridiangames.client.rendering.shaders.GuiShader;
import fr.veridiangames.core.audio.Sound;
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

		this.bgColor = new Color4f(0, 0, 0, 0.5f);
		this.bgColorIdle = new Color4f(1, 1, 1, 0.6f);
		this.bgColorHover = Color4f.WHITE;

		this.label = new GuiLabel(text, 0, 0);
		this.label.setOrigin(GuiOrigin.A);
		this.label.setColor(new Color4f(1, 1, 1, 0.4f));
		this.label.setPosition(this.x + w/2 - this.label.getTextWidth(), this.y + h/2 - this.label.getTextHeight() / 2);

		this.textWidth = this.label.getTextWidth();
	}

	@Override
	public void update() {
		if (mouseInUse && !this.inUse)
			return;

		if (this.centerText) this.textWidth = this.label.getTextWidth() / 2;


		if (this.mouseEnter)
			AudioSystem.play(Sound.BEEP);

		super.update();
		this.label.update();
		this.label.setPosition(this.x + this.w/2 - this.textWidth, this.y + this.h/2 - this.label.getTextHeight() / 2);

		if (this.mouseButtonPressed) {
			this.xSlider = Display.getInstance().getInput().getMouse().getX() - this.x - this.w /2;
			mouseInUse = true;
			this.inUse = true;
		}else {
			this.inUse = false;
			mouseInUse = false;
		}

		if (this.xSlider < -this.w/2 + 10) this.xSlider = -this.w/2 + 10;
		if (this.xSlider > this.w/2 - 10) this.xSlider = this.w/2 - 10;

		this.value = ((float) (this.xSlider + this.w/2) - 10) / (this.w - 20);

		if (this.mouseIn || this.mouseButtonPressed)
			this.color = this.bgColorHover;
		else
			this.color = this.bgColorIdle;
	}

	@Override
	public void render(GuiShader shader) {
		shader.setColor(this.bgColor);
		StaticPrimitive.quadPrimitive().render(shader, this.x + this.w/2, this.y + this.h/2, 0, this.w/2, this.h/2, 0);

		this.label.render(shader);

		shader.setColor(this.color);
		StaticPrimitive.quadPrimitive().render(shader, this.x + this.w/2 + this.xSlider, this.y + this.h/2, 0, 6, this.h/2 - 4, 0);
	}

	@Override
	public void dispose() {

	}

	public float getValue() {
		return this.value;
	}

	public GuiLabel getLabel() {
		return this.label;
	}

	public void setValue(float value) {
		this.xSlider = (int) (value * this.w - this.w / 2.0f);
	}

	public void centerText() {
		this.centerText = true;
	}
}