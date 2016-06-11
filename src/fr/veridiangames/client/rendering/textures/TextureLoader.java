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

package fr.veridiangames.client.rendering.textures;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;

/**
 * Created by Marccspro on 1 avr. 2016.
 */
public class TextureLoader
{
	private static HashMap<String, Texture>	cache		= new HashMap<String, Texture>();
	private static List<Integer>			textures	= new ArrayList<Integer>();

	public static int createTexture()
	{
		int texture = glGenTextures();
		textures.add(texture);

		return texture;
	}

	public static void clean()
	{
		for (Integer texture : textures)
		{
			glDeleteTextures(texture);
		}

		textures.clear();
	}

	public static Texture loadTexture(String path, int filter, boolean mipmap)
	{
		if (cache.containsKey(path))
		{
			return cache.get(path);
		}

		TextureData texture = TextureLoader.decode(path);

		int id = texture.getID();
		int width = texture.getWidth();
		int height = texture.getHeight();

		glBindTexture(GL_TEXTURE_2D, id);

		if (mipmap)
		{
			if (filter == GL_LINEAR)
			{
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);
				glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -1f);
			}
			else if (filter == GL_NEAREST)
			{
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_NEAREST);
				glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -1f);
			}
		}
		else
		{
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
		}

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, texture.getBuffer());
		if (mipmap)
			glGenerateMipmap(GL_TEXTURE_2D);

		Texture finalTexture = new Texture(id, width, height);
		cache.put(path, finalTexture);

		return finalTexture;
	}

	public static Texture getTexture(String path, BufferedImage image)
	{
		TextureData data = decode(image);

		int id = data.getID();
		int width = data.getWidth();
		int height = data.getHeight();
		IntBuffer buffer = data.getBuffer();

		glBindTexture(GL_TEXTURE_2D, id);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

		glBindTexture(GL_TEXTURE_2D, 0);

		Texture finalTexture = new Texture(id, width, height);

		return finalTexture;
	}

	public static TextureData decode(String path)
	{
		int[] pixels = null;
		int width = 0, height = 0;
		try
		{
			BufferedImage image = ImageIO.read(new File(path));
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		int[] data = new int[pixels.length];
		for (int i = 0; i < pixels.length; i++)
		{
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);

			data[i] = a << 24 | b << 16 | g << 8 | r;
		}

		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();

		return new TextureData(createTexture(), width, height, buffer);
	}

	public static TextureData decode(BufferedImage image)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		int[] pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);

		int[] data = new int[pixels.length];
		for (int i = 0; i < pixels.length; i++)
		{
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);

			data[i] = a << 24 | b << 16 | g << 8 | r;
		}

		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();

		return new TextureData(createTexture(), width, height, buffer);
	}

	public static Texture errorTexture()
	{
		if (cache.get("error") != null)
		{
			return cache.get("error");
		}
		int texture = glGenTextures();
		textures.add(texture);
		int[] data = new int[64 * 64];
		for (int x = 0; x < 64; x++)
		{
			for (int y = 0; y < 64; y++)
			{
				int i = y * 64 + x;
				data[i] = 0xff000000;
				if (x >= 32 && y < 32)
					data[i] = 0xffff00ff;
				if (x < 32 && y >= 32)
					data[i] = 0xffff00ff;
			}
		}
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();

		glBindTexture(GL_TEXTURE_2D, texture);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 64, 64, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		Texture error = new Texture(texture, 64, 64);
		cache.put("error", error);
		return error;
	}

	public static List<Integer> getTextures()
	{
		return textures;
	}

	public static HashMap<String, Texture> getTexturesCache()
	{
		return cache;
	}
}
