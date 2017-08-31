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

import java.awt.Font;

import fr.veridiangames.client.audio.AudioSystem;
import fr.veridiangames.client.guis.TrueTypeFont;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.StaticFont;
import fr.veridiangames.client.rendering.guis.listeners.GuiActionListener;
import fr.veridiangames.client.rendering.guis.primitives.StaticPrimitive;
import fr.veridiangames.client.rendering.renderers.guis.FontRenderer;
import fr.veridiangames.client.rendering.shaders.GuiShader;
import fr.veridiangames.core.audio.Sound;
import fr.veridiangames.core.utils.Color4f;

public class GuiButton extends GuiComponent
{

	private GuiActionListener actionListener;

	boolean centerText = false;
	private TrueTypeFont font;
	private FontRenderer fontRenderer;
	private int xTextOffs, yTextOffs;
	private int offsetMargin = 10;
	private boolean clickable;

	public Color4f idleColor, hoverColor, activeColor, fontColor;

	public GuiButton(String text, int x, int y, int w, GuiActionListener actionListener) {
		this(text, x, y, w, 30, actionListener);
	}

	public GuiButton(String text, int x, int y, int w, int h, GuiActionListener actionListener) {
		super(x, y, w, h, new Color4f(0, 0, 0, 0.5f));
		this.idleColor = new Color4f(0, 0, 0, 0.5f);
		this.hoverColor = new Color4f(1, 1, 1, 0.4f);
		this.activeColor = new Color4f(0, 0, 0, 0.7f);
		this.fontColor = Color4f.WHITE;
		this.actionListener = actionListener;

		this.font = new TrueTypeFont(StaticFont.square_bold(Font.PLAIN, 24f), true);
		this.fontRenderer = new FontRenderer(this.font, text, 0, 0);

		this.xTextOffs = this.offsetMargin;
		this.yTextOffs = h/2 - this.fontRenderer.getHeight() / 2;

		this.setOrigin(GuiOrigin.CENTER);
	}

	public void setText(String text) {
		this.fontRenderer.setText(text);

		if (this.centerText) {
			this.xTextOffs = this.w/2 - this.fontRenderer.getWidth() / 2;
			this.yTextOffs = this.h/2 - this.fontRenderer.getHeight() / 2;
		}else {
			this.xTextOffs = this.offsetMargin;
			this.yTextOffs = this.h/2 - this.fontRenderer.getHeight() / 2;
		}
	}

	@Override
	public void update() {
		super.update();

		if (!this.clickable)
			this.fontColor = Color4f.GRAY;
		else
			this.fontColor = Color4f.WHITE;

		if (mouseInUse) return;

		this.fontRenderer.setPosition(this.x + this.xTextOffs, this.y + this.yTextOffs);

		if (this.mouseEnter)
			AudioSystem.play(Sound.BEEP);

		if (this.clickable) {
			this.color = this.idleColor;

			if (this.mouseIn)
				this.color = this.hoverColor;

			if (this.mouseButtonPressed)
				this.color = this.activeColor;

			if (this.mouseExit)
				this.color = this.idleColor;

			if (this.action)
				this.actionListener.onAction();
		} else
			this.color = this.idleColor;
	}

	@Override
	public void render(GuiShader shader) {
		shader.setColor(this.color);
		StaticPrimitive.quadPrimitive().render(shader, this.x + this.w/2, this.y + this.h/2, 0, this.w/2, this.h/2, 0);

		this.fontRenderer.setPosition(this.x + this.xTextOffs, this.y + this.yTextOffs + 2);
		this.fontRenderer.render(shader, Color4f.DARK_GRAY, 0);

		this.fontRenderer.setPosition(this.x + this.xTextOffs, this.y + this.yTextOffs);
		this.fontRenderer.render(shader, this.fontColor, 0);
	}

	public void centerText() {
		this.centerText = true;

		if (this.centerText) {
			this.xTextOffs = this.w/2 - this.fontRenderer.getWidth() / 2;
			this.yTextOffs = this.h/2 - this.fontRenderer.getHeight() / 2;
		}else {
			this.xTextOffs = this.offsetMargin;
			this.yTextOffs = this.h/2 - this.fontRenderer.getHeight() / 2;
		}
	}

	public void setOffsetMargin(int margin) {
		this.offsetMargin = margin;
	}

	@Override
	public void dispose() {
		this.fontRenderer.dispose();
	}

	public boolean isClickable()
	{
		return this.clickable;
	}

	public void setClickable(boolean clickable)
	{
		this.clickable = clickable;
	}
}