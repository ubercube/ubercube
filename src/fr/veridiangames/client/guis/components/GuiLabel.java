/*
 * Copyright (C) 2016 Team Ubercube
 *
 *   This file is part of Ubercube.
 *
 *       Ubercube is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Ubercube is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Ubercube.  If not, see http://www.gnu.org/licenses/.
 */

package fr.veridiangames.client.guis.components;

import fr.veridiangames.client.guis.TrueTypeFont;
import fr.veridiangames.client.rendering.renderers.guis.FontRenderer;
import fr.veridiangames.client.rendering.shaders.GuiShader;

/**
 * Created by Marccspro on 1 avr. 2016.
 */
public class GuiLabel extends GuiComponent
{
	private String			text;
	private int				x, y;
	private TrueTypeFont	font;
	private FontRenderer	renderer;
	private float 			shadowDistance;

	public GuiLabel(String text, int x, int y, TrueTypeFont font)
	{
		super(true);
		this.text = text;
		this.x = x;
		this.y = y;
		this.font = font;
		this.renderer = new FontRenderer(font, text, x, y);
	}

	public void update()
	{

	}
	
	public void render(GuiShader shader)
	{
		renderer.render(shader, color, shadowDistance);
	}

	public String getText()
	{
		return text;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public TrueTypeFont getFont()
	{
		return font;
	}

	public float getShadowDistance()
	{
		return shadowDistance;
	}

	public GuiLabel setShadowDistance(int shadowDistance)
	{
		this.shadowDistance = shadowDistance;
		return this;
	}
}
