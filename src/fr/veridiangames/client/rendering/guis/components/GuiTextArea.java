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

import fr.veridiangames.client.guis.TrueTypeFont;
import fr.veridiangames.client.rendering.guis.GuiComponent;
import fr.veridiangames.client.rendering.guis.StaticFont;
import fr.veridiangames.client.rendering.guis.primitives.StaticPrimitive;
import fr.veridiangames.client.rendering.renderers.guis.FontRenderer;
import fr.veridiangames.client.rendering.shaders.GuiShader;
import fr.veridiangames.core.utils.Color4f;

import java.awt.*;


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
		fontRenderer = new FontRenderer(this.font, text, x, y);
		this.bgColor = new Color4f(0, 0, 0, 0.5f);
	}

	public GuiTextArea(String text, int x, int y, int w, int h, float size) {
		super(x, y, w, h, Color4f.WHITE);
		font = new TrueTypeFont(StaticFont.HPSimplified_Rg(Font.PLAIN, size), true);
		fontRenderer = new FontRenderer(font, text, x, y);
		this.bgColor = new Color4f(0, 0, 0, 0.5f);
	}
	
	public void setText(String text) {
		fontRenderer.setText(text);
		processOrigin();
	}
	
	public void update() {
		super.update();
		
		fontRenderer.setPosition(x + 10, y + 10);
	}

	public void render(GuiShader shader) {
		shader.setColor(bgColor);
		StaticPrimitive.quadPrimitive().render(shader, x + w / 2, y + h / 2, 0, w / 2, h / 2, 0);
		if (dropShadow > 0) {
			fontRenderer.setPosition(x + dropShadow - 1, y + dropShadow);
			fontRenderer.render(shader, dropShadowColor, 0);
		}
		fontRenderer.setPosition(x + 10, y + 10);
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

	public Color4f getBgColor()
	{
		return bgColor;
	}

	public void setBgColor(Color4f bgColor)
	{
		this.bgColor = bgColor;
	}
}