/*
 *   Copyright (C) 2016 Team Ubercube
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
 *       along with Ubercube.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.veridiangames.client.guis.components;

import fr.veridiangames.core.maths.Vec2i;
import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.client.rendering.shaders.GuiShader;

/**
 * Created by Marccspro on 31 mars 2016.
 */
public abstract class GuiComponent
{
	private boolean dynamic;

	protected Vec2i 	position;
	protected Vec2i 	size;
	protected Color4f 	color;
	private int 		border;
	private Color4f 	borderColor;
	
	public GuiComponent(boolean dynamic)
	{
		this.dynamic = dynamic;
		this.color = Color4f.WHITE;
	}
	
	public abstract void update();
	public void render(GuiShader shader) {}
	
	public GuiComponent setBorder(int size, Color4f color)
	{
		this.border = size;
		this.borderColor = color;
		return this;
	}
	
	public Color4f getColor()
	{
		return color;
	}

	public GuiComponent setColor(Color4f color)
	{
		this.color = color;
		return this;
	}

	public boolean isDynamic()
	{
		return dynamic;
	}
	
	public Vec2i getPosition()
	{
		return position;
	}

	public void setPosition(Vec2i position)
	{
		this.position = position;
	}

	public Vec2i getSize()
	{
		return size;
	}

	public void setSize(Vec2i size)
	{
		this.size = size;
	}
	
	public int getBorder()
	{
		return border;
	}

	public Color4f getBorderColor()
	{
		return borderColor;
	}
	
	public void setDynamic(boolean dynamic)
	{
		this.dynamic = dynamic;
	}
}