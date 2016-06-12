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

package fr.veridiangames.client.rendering.textures;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import org.lwjgl.opengl.GL12;

import fr.veridiangames.client.rendering.renderers.Renderer;

public class CubeMap
{
	public static final CubeMap DEFAULT_CUBEMAP = new CubeMap(new String[]{
			"res/cubemap/posx.jpg",
			"res/cubemap/negx.jpg",
			"res/cubemap/posy.jpg",
			"res/cubemap/negy.jpg",
			"res/cubemap/posz.jpg",
			"res/cubemap/negz.jpg"
	});
	
	private int textureID;
	
	public CubeMap(String[] textures)
	{
		if (!Renderer.isDX11())
			return;
		
		textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_CUBE_MAP, textureID);

		for (int i = 0; i < textures.length; i++)
		{
			TextureData data = TextureLoader.decode(textures[i]);

			glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA, data.getWidth(), data.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, data.getBuffer());
		}
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
	}
	
	public CubeMap(int id)
	{
		this.textureID = id;
	}
	
	public int getTexture()
	{
		return textureID;
	}
}
