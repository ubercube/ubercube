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

package fr.veridiangames.client.rendering.renderers;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;

import org.lwjgl.opengl.GL11;

/**
 * Created by Marccspro on 20 janv. 2016.
 */
public class Renderer
{
	private static boolean GL33;

	public Renderer()
	{
		this.init();
	}

	public static boolean setDX11()
	{
		String glVersionString = GL11.glGetString(GL11.GL_VERSION).split(" ")[0].trim();
		float glVersion = Float.parseFloat(glVersionString.substring(0, 3));
		System.out.println("Available GL Version " + glVersion);
		if (glVersion < 3.3)
		{
			System.err.println("Not GL33 compatible !");
			return GL33 = false;
		}
		return GL33 = true;
	}

	private void init()
	{
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glClearColor(221f / 255f, 232f / 255f, 255f / 255f, 1.0f);
	}

	public void start()
	{
		glClearColor(221f / 255f, 232f / 255f, 255f / 255f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void prepare3D()
	{
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_FRONT);
	}

	public void prepare2D()
	{
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_CULL_FACE);
	}

	public static void bindTextureCube(int cubemap)
	{
		glBindTexture(GL_TEXTURE_CUBE_MAP, cubemap);
	}

	public static boolean isGL33()
	{
		return GL33;
	}
}
