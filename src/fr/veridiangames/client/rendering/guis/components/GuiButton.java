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
import fr.veridiangames.client.rendering.guis.listeners.GuiActionListener;
import fr.veridiangames.client.rendering.guis.primitives.StaticPrimitive;
import fr.veridiangames.client.rendering.renderers.guis.FontRenderer;
import fr.veridiangames.client.rendering.shaders.GuiShader;
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
		this.fontRenderer = new FontRenderer(font, text, 0, 0);
		
		xTextOffs = offsetMargin;
		yTextOffs = h/2 - fontRenderer.getHeight() / 2;	
		
		setOrigin(GuiOrigin.CENTER);
	}
	
	public void setText(String text) {
		fontRenderer.setText(text);
		
		if (centerText) {
			xTextOffs = w/2 - fontRenderer.getWidth() / 2;
			yTextOffs = h/2 - fontRenderer.getHeight() / 2;
		}else {
			xTextOffs = offsetMargin;
			yTextOffs = h/2 - fontRenderer.getHeight() / 2;			
		}
	}
	
	public void update() {
		super.update();

		if (!clickable) {
			fontColor = Color4f.GRAY;
		}else {			
			fontColor = Color4f.WHITE;
		}
		
		if (mouseInUse) return;

		fontRenderer.setPosition(x + xTextOffs, y + yTextOffs);
		
		if (clickable) {
			if (mouseIn) {
				color = hoverColor;
			}
	
			if (mouseButtonPressed) {
				color = activeColor;
			}
			
			if (mouseExit) {
				color = idleColor;
			}
			
			if (action) {
				actionListener.onAction();
			}
		}else {
			color = idleColor;
		}
	}

	public void render(GuiShader shader) {
		shader.setColor(color);
		StaticPrimitive.quadPrimitive().render(shader, x + w/2, y + h/2, 0, w/2, h/2, 0);
		
		fontRenderer.setPosition(x + xTextOffs, y + yTextOffs + 2);
		fontRenderer.render(shader, Color4f.DARK_GRAY, 0);
		
		fontRenderer.setPosition(x + xTextOffs, y + yTextOffs);
		fontRenderer.render(shader, fontColor, 0);
	}
	
	public void centerText() {
		centerText = true;
		
		if (centerText) {
			xTextOffs = w/2 - fontRenderer.getWidth() / 2;
			yTextOffs = h/2 - fontRenderer.getHeight() / 2;
		}else {
			xTextOffs = offsetMargin;
			yTextOffs = h/2 - fontRenderer.getHeight() / 2;			
		}
	}
	
	public void setOffsetMargin(int margin) {
		this.offsetMargin = margin;
	}
	
	public void dispose() {
		fontRenderer.dispose();
	}

	public boolean isClickable()
	{
		return clickable;
	}

	public void setClickable(boolean clickable)
	{
		this.clickable = clickable;
	}
}