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

import fr.veridiangames.client.guis.TrueTypeFont;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.StaticFont;
import fr.veridiangames.client.rendering.renderers.guis.FontRenderer;
import fr.veridiangames.client.rendering.shaders.GuiShader;
import fr.veridiangames.core.utils.Color4f;


public class GuiLabel extends GuiComponent
{
	private TrueTypeFont 	font;
	private FontRenderer 	fontRenderer;
	private int 			dropShadow;
	private Color4f 		dropShadowColor;

	public GuiLabel(String text, int x, int y) {
		this(text, x, y, 0, 0);
	}

	public GuiLabel(String text, int x, int y, Font font) {
		this(text, x, y, 0, 0, 0, font);
	}

	public GuiLabel(String text, int x, int y, float size) {
		this(text, x, y, 0, 0, size);
	}

	public GuiLabel(String text, int x, int y, int w, int h) {
		this(text, x, y, w, h, 24f);
	}

	public GuiLabel(String text, int x, int y, int w, int h, float size, Font font) {
		super(x, y, w, h, new Color4f(0, 0, 0, 0.5f));
		this.font = new TrueTypeFont(font, true);
		this.fontRenderer = new FontRenderer(this.font, text, x, y);
		this.dropShadowColor = new Color4f(0, 0, 0, 0.5f);

		this.w = this.fontRenderer.getWidth();
		this.h = this.fontRenderer.getHeight();
	}

	public GuiLabel(String text, int x, int y, int w, int h, float size) {
		super(x, y, w, h, new Color4f(0, 0, 0, 0.5f));
		this.font = new TrueTypeFont(StaticFont.HPSimplified_Rg(Font.PLAIN, size), true);
		this.fontRenderer = new FontRenderer(this.font, text, x, y);
		this.dropShadowColor = new Color4f(0, 0, 0, 0.5f);

		this.w = this.fontRenderer.getWidth();
		this.h = this.fontRenderer.getHeight();
	}

	public void setText(String text) {
		this.fontRenderer.setText(text);
		this.w = this.fontRenderer.getWidth();
		this.h = this.fontRenderer.getHeight();
		this.processOrigin();
	}

	@Override
	public void update() {
		this.w = this.fontRenderer.getWidth();
		this.h = this.fontRenderer.getHeight();
		super.update();

		this.fontRenderer.setPosition(this.x, this.y);
	}

	@Override
	public void render(GuiShader shader) {
		shader.setColor(Color4f.WHITE);
		if (this.dropShadow > 0)
		{
			this.fontRenderer.setPosition(this.x + this.dropShadow - 1, this.y + this.dropShadow);
			this.fontRenderer.render(shader, this.dropShadowColor, 0);
		}
		this.fontRenderer.setPosition(this.x, this.y);
		this.fontRenderer.render(shader, this.color, 0);

	}

	public int getTextWidth() {
		return this.w;
	}

	public int getTextHeight() {
		return this.h;
	}

	public TrueTypeFont.IntObject getCharData(char c) {
		return this.fontRenderer.getCharData(c);
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		this.fontRenderer.setPosition(x, y);
	}

	@Override
	public void dispose() {
		this.fontRenderer.dispose();
	}

	public int getDropShadow()
	{
		return this.dropShadow;
	}

	public void setDropShadow(int dropShadow)
	{
		this.dropShadow = dropShadow;
	}

	public Color4f getDropShadowColor()
	{
		return this.dropShadowColor;
	}

	public void setDropShadowColor(Color4f dropShadowColor)
	{
		this.dropShadowColor = dropShadowColor;
	}
}