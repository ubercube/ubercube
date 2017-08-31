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
import fr.veridiangames.client.rendering.guis.primitives.StaticPrimitive;
import fr.veridiangames.client.rendering.renderers.guis.FontRenderer;
import fr.veridiangames.client.rendering.shaders.GuiShader;
import fr.veridiangames.core.utils.Color4f;


public class GuiTextArea extends GuiComponent
{
	private TrueTypeFont 	font;
	private FontRenderer 	fontRenderer;
	private int 			dropShadow;
	private Color4f 		dropShadowColor;
	private Color4f 		bgColor;

	public GuiTextArea(String text, int x, int y, int w, int h) {
		this(text, x, y, w, h, 24f);
	}

	public GuiTextArea(String text, int x, int y, int w, int h, Font font) {
		super(x, y, w, h, Color4f.WHITE);
		this.font = new TrueTypeFont(font, true);
		this.fontRenderer = new FontRenderer(this.font, text, x, y);
		this.bgColor = new Color4f(0, 0, 0, 0.5f);
	}

	public GuiTextArea(String text, int x, int y, int w, int h, float size) {
		super(x, y, w, h, Color4f.WHITE);
		this.font = new TrueTypeFont(StaticFont.HPSimplified_Rg(Font.PLAIN, size), true);
		this.fontRenderer = new FontRenderer(this.font, text, x, y);
		this.bgColor = new Color4f(0, 0, 0, 0.5f);
	}

	public void setText(String text) {
		this.fontRenderer.setText(text);
		this.processOrigin();
	}

	@Override
	public void update() {
		super.update();

		this.fontRenderer.setPosition(this.x + 10, this.y + 10);
	}

	@Override
	public void render(GuiShader shader) {
		shader.setColor(this.bgColor);
		StaticPrimitive.quadPrimitive().render(shader, this.x + this.w / 2, this.y + this.h / 2, 0, this.w / 2, this.h / 2, 0);
		if (this.dropShadow > 0) {
			this.fontRenderer.setPosition(this.x + this.dropShadow - 1, this.y + this.dropShadow);
			this.fontRenderer.render(shader, this.dropShadowColor, 0);
		}
		this.fontRenderer.setPosition(this.x + 10, this.y + 10);
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

	public Color4f getBgColor()
	{
		return this.bgColor;
	}

	public void setBgColor(Color4f bgColor)
	{
		this.bgColor = bgColor;
	}
}