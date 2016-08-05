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

package fr.veridiangames.core.game.world;

import fr.veridiangames.core.game.world.vegetations.Bush;
import fr.veridiangames.core.game.world.vegetations.Rock;
import fr.veridiangames.core.game.world.vegetations.Tree;
import fr.veridiangames.core.maths.Mathf;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.maths.Vec3i;
import fr.veridiangames.core.maths.Vec4i;
import fr.veridiangames.core.utils.Color4f;

public class Chunk
{
	public static final int SIZE = 16;

	private Thread	generationThread;
	private boolean	generated;
	private boolean structureGeneration;
	private boolean	shouldBeRendered;

	public int[][][]	blocks;
	public Vec3i		position;
	public Vec3			centerPosition;
	
	private float[][] noise;

	private World world;
	
	public Chunk(Vec3i pos)
	{
		this.position = new Vec3i(pos);
		this.noise = null;
		this.generated = true;
		this.structureGeneration = false;
		this.shouldBeRendered = true;
		this.centerPosition = new Vec3(pos.x * SIZE + SIZE / 2, pos.y * SIZE + SIZE / 2, pos.z * SIZE + SIZE / 2);
		this.world = null;
		this.blocks = new int[SIZE][SIZE][SIZE];
	}
	
	public Chunk(int x, int y, int z, float[][] noise, World world)
	{
		this.position = new Vec3i(x, y, z);
		this.noise = noise;
		this.generated = false;
		this.structureGeneration = false;
		this.shouldBeRendered = true;
		this.centerPosition = new Vec3(x * SIZE + SIZE / 2, y * SIZE + SIZE / 2, z * SIZE + SIZE / 2);
		this.world = world;
	}

	public void generateChunk()
	{
		this.blocks = new int[SIZE][SIZE][SIZE];
//		this.generationThread = new Thread("chunk-" + position.x + "-" + position.y + "-" + position.z + "-generation")
//		{
//			public void run()
//			{
//				generateTerrainData();
//				generateVegetation();
//				generated = true;
//			}
//		};
//		generationThread.start();
	}

	public void generateTerrainData()
	{
		for (int x = 0; x < SIZE; x++)
		{
			for (int z = 0; z < SIZE; z++)
			{
				for (int y = 0; y < SIZE; y++)
				{
					int xx = position.x * SIZE + x;
					int yy = position.y * SIZE + y;
					int zz = position.z * SIZE + z;
					Vec4i modifiedBlock = world.getModifiedBlock(xx, yy, zz);
					if (modifiedBlock != null)
					{
						blocks[x][y][z] = modifiedBlock.w;
						continue;
					}

					float noiseHeight = noise[x][z];
					if (yy > noiseHeight)
						continue;
					else if (yy > noiseHeight - 1)
					{
						float snow = (float)(yy - 10) / 6;
						if(snow > 1) snow = 1;
						if(snow > world.getWorldGen().getRandom()){
							Color4f color = new Color4f(0.9f, 0.9f, 0.98f).add(world.getWorldGen().getRandom() * 0.02f);
							color.setAlpha(1f);
							addBlock(x, y, z, color.getARGB());
						}else{
							float cn = Mathf.random(-0.02f, 0.02f);
							Color4f ca = new Color4f(0.05f, 0.10f, 0.05f);
							Color4f cb = new Color4f(0.1f, 0.50f, 0.1f);
							float t = noiseHeight / 30.0f;
							Color4f color = Color4f.mix(ca, cb, t).add(cn);
							color.setAlpha(1f);
							addBlock(x, y, z, color.getARGB());
						}
					}
					else
					{
						float cn = Mathf.random(-0.02f, 0.02f);
						Color4f stone = new Color4f(0.5f, 0.5f, 0.5f).add(cn);
						addBlock(x, y, z, stone.getARGB());
					}
				}
			}
		}
	}

	public void generateVegetation()
	{
		for (int x = 0; x < SIZE; x++)
		{
			for (int z = 0; z < SIZE; z++)
			{
				int xx = position.x * SIZE + x;
				int zz = position.z * SIZE + z;

				float noiseHeight = noise[x][z];

				if (noiseHeight < 13)
				{
					float rand = world.getWorldGen().getRandom();
					if (rand > 0.999f)
					{
						Tree.oakTree(world, xx, (int) noiseHeight, zz);
					}else if(rand > 0.998f && rand < 0.999f)
					{
						Bush.bush(world, xx, (int) noiseHeight, zz);
					}else if(rand > 0.9977f && rand < 0.998f){
						Rock.rock(world, xx, (int) noiseHeight, zz);
					}
				}
				else if(noiseHeight >= 17){
					float rand = world.getWorldGen().getRandom();
					if (rand > 0.998f)
					{
						Tree.firTree(world, xx, (int) noiseHeight, zz);
					}else if(rand > 0.9967f && rand < 0.997f){
						Rock.rock(world, xx, (int) noiseHeight, zz);
					}
				}
				else
				{
					if (world.getWorldGen().getRandom() > 0.9985f)
					{
						Rock.rock(world, xx, (int) noiseHeight, zz);
					}
				}

			}
		}

		generated = true;
	}


	public void update()
	{
		this.shouldBeRendered = false;
	}

	public boolean isGenerated()
	{
		return generated;
	}

	public int[][][] getBlocks()
	{
		return blocks;
	}

	public int getBlock(int x, int y, int z)
	{
		if (x < 0 || y < 0 || z < 0 || x >= SIZE || y > SIZE || z > SIZE)
			return 0;

		return blocks[x][y][z];
	}
	
	public void addBlock(int x, int y, int z, int block)
	{
		if (x < 0 || y < 0 || z < 0 || x >= SIZE || y > SIZE || z > SIZE)
			return;

		blocks[x][y][z] = block;
	}
	
	public void removeBlock(int x, int y, int z)
	{
		if (x < 0 || y < 0 || z < 0 || x >= SIZE || y > SIZE || z > SIZE)
			return;

		blocks[x][y][z] = 0;
	}

	public Vec3i getPosition()
	{
		return position;
	}

	public boolean shouldBeRendered()
	{
		return shouldBeRendered;
	}

	public void setShouldBeRendered(boolean shouldBeRendered)
	{
		this.shouldBeRendered = shouldBeRendered;
	}

	public boolean isStructureGeneration()
	{
		return structureGeneration;
	}

	public void setStructureGeneration(boolean structureGeneration)
	{
		this.structureGeneration = structureGeneration;
	}
	
	public void setWorld(World world)
	{
		this.world = world;
	}
}