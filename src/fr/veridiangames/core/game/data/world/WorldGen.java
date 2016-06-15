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

package fr.veridiangames.core.game.data.world;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import fr.veridiangames.core.game.world.Chunk;
import fr.veridiangames.core.game.world.structures.Structure;
import fr.veridiangames.core.utils.Color4f;

public class WorldGen
{
	private int				size;
	private List<NoisePass>	noisePasses;
	private float[][] 		noise;
	private List<Structure> structs;
	private Random 			rand;
	
	private int height;
	private boolean usingHeight;
	private float[][] heightData;
	
	public WorldGen(int height)
	{
		this.height = height;
		this.usingHeight = true;
		
		heightData = new float[Chunk.SIZE][Chunk.SIZE];
		for (int x = 0; x < Chunk.SIZE; x++)
		{
			for (int y = 0; y < Chunk.SIZE; y++)
			{
				heightData[x][y] = height;
			}	
		}
	}
	
	public WorldGen(long seed, int size)
	{
		this.noisePasses = new ArrayList<NoisePass>();
		this.size = size;
		this.noise = new float[size * Chunk.SIZE][size * Chunk.SIZE];
		this.rand = new Random(seed);
		
		addNoisePasses(seed);
		calcFinalNoise();
	}

	private void addNoisePasses(long seed)
	{
		add(new NoisePass(seed, 60, 3, 0, 1));
		add(new NoisePass(seed, 20, 10, 0.4f, -1));
		add(new NoisePass(seed, 20, 20, 0, -1));
	}

	private void add(NoisePass pass)
	{
		noisePasses.add(pass);
	}

	private void calcFinalNoise()
	{
		for (int xx = 0; xx < size; xx++)
		{
			for (int yy = 0; yy < size; yy++)
			{
				int xi = xx * Chunk.SIZE;
				int yi = yy * Chunk.SIZE;
				for (int x = 0; x < Chunk.SIZE; x++)
				{
					for (int y = 0; y < Chunk.SIZE; y++)
					{
						float n = -10000;
						for (NoisePass p : noisePasses)
						{
							float nn = p.getNoisePass(x + xi, y + yi);
							if (nn > n) n = nn;
						}
						noise[x + xi][y + yi] = n;
					}
				}
			}
		}
	}

	public float[][] getNoiseChunk(int xp, int yp)
	{
		if (usingHeight)
			return heightData;
		
		float[][] noise = new float[Chunk.SIZE][Chunk.SIZE];
		for (int x = 0; x < Chunk.SIZE; x++)
		{
			for (int y = 0; y < Chunk.SIZE; y++)
			{
				noise[x][y] = this.noise[x + xp * Chunk.SIZE][y + yp * Chunk.SIZE] + 0.5f * Chunk.SIZE;
			}	
		}
		return noise;
	}

	public float getNoise(int x, int y)
	{
		if (usingHeight)
			return height;
		
		return noise[x][y];
	}
	
	public static void main(String[] args)
	{
		int size = 100;
		Random rand = new Random();
		WorldGen gen = new WorldGen(42, size);
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		
		int[] pixels = new int[size * size];
		for (int x = 0; x < size; x++)
		{
			for (int y = 0; y < size; y++)
			{
				float h = gen.getNoise(x, y) + 16;
				int color = 0xffffff;
				Color4f c = Color4f.getColorFromARGB(color);
				int r = (int) (c.r * h * 4);
				int g = (int) (c.g * h * 4);
				int b = (int) (c.b * h * 4);
				
				pixels[x + y * size] = r << 16 | g << 8 | b;
			}	
		}
		img.setRGB(0, 0, size, size, pixels, 0, size);
		JOptionPane.showMessageDialog(null, null, "Another", JOptionPane.YES_NO_OPTION, new ImageIcon(img.getScaledInstance(size * 6, size * 6, Image.SCALE_AREA_AVERAGING)));
	}
}
