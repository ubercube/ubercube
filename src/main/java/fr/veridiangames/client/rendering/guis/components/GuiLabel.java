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
		fontRenderer = new FontRenderer(this.font, text, x, y);
		dropShadowColor = new Color4f(0, 0, 0, 0.5f);

		this.w = fontRenderer.getWidth();
		this.h = fontRenderer.getHeight();
	}
	
	public GuiLabel(String text, int x, int y, int w, int h, float size) {
		super(x, y, w, h, new Color4f(0, 0, 0, 0.5f));
		font = new TrueTypeFont(StaticFont.HPSimplified_Rg(Font.PLAIN, size), true);
		fontRenderer = new FontRenderer(font, text, x, y);
		dropShadowColor = new Color4f(0, 0, 0, 0.5f);
	
		this.w = fontRenderer.getWidth();
		this.h = fontRenderer.getHeight();
	}
	
	public void setText(String text) {
		fontRenderer.setText(text);
		this.w = fontRenderer.getWidth();
		this.h = fontRenderer.getHeight();
		processOrigin();
	}
	
	public void update() {
		w = fontRenderer.getWidth();
		h = fontRenderer.getHeight();
		super.update();
		
		fontRenderer.setPosition(x, y);
	}

	public void render(GuiShader shader) {
		shader.setColor(Color4f.WHITE);
		if (dropShadow > 0)
		{
			fontRenderer.setPosition(x + dropShadow - 1, y + dropShadow);
			fontRenderer.render(shader, dropShadowColor, 0);
		}
		fontRenderer.setPosition(x, y);
		fontRenderer.render(shader, color, 0);

	}
	
	public int getTextWidth() {
		return w;
	}
	
	public int getTextHeight() {
		return h;
	}
	
	public TrueTypeFont.IntObject getCharData(char c) {
		return fontRenderer.getCharData(c);
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		fontRenderer.setPosition(x, y);
	}
	
	public void dispose() {
		fontRenderer.dispose();
	}

	public int getDropShadow()
	{
		return dropShadow;
	}

	public void setDropShadow(int dropShadow)
	{
		this.dropShadow = dropShadow;
	}

	public Color4f getDropShadowColor()
	{
		return dropShadowColor;
	}

	public void setDropShadowColor(Color4f dropShadowColor)
	{
		this.dropShadowColor = dropShadowColor;
	}
}