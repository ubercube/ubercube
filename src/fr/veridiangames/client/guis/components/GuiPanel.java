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

package fr.veridiangames.client.guis.components;

import fr.veridiangames.core.maths.Vec2i;
import fr.veridiangames.client.rendering.textures.TextureData;

/**
 * Created by Marccspro on 1 avr. 2016.
 */
public class GuiPanel extends GuiComponent
{
	private TextureData texture;

	public GuiPanel(int x, int y, int w, int h)
	{
		super(false);
		this.position = new Vec2i(x, y);
		this.size = new Vec2i(w, h);
	}
	
	public void update()
	{
		
	}
	
	public boolean isTextured()
	{
		return texture != null;
	}

	public TextureData getTextureData()
	{
		return texture;
	}
}