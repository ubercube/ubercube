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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.data.GameData;
import fr.veridiangames.core.game.data.world.WorldGen;
import fr.veridiangames.core.maths.Mathf;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.maths.Vec3i;
import fr.veridiangames.core.maths.Vec4i;
import fr.veridiangames.core.physics.colliders.AABoxCollider;
import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.core.utils.Indexer;

public class World
{
	private Map<Integer, Chunk> generatingChunks;
	private List<Integer> 		updateRequests;
	private Map<Integer, Chunk> chunks;
	private List<Integer> 		chunkGarbage;
	private List<Vec4i>			modifiedBlocks;

	private WorldGen	worldGen;
	private GameData	gameData;
	private GameCore	core;
	private int			worldSize;
	private boolean 	generated;
	
	public World(GameCore core)
	{
		this.core = core;
		this.gameData = core.getGame().getData();
		this.generatingChunks = new HashMap<Integer, Chunk>();
		this.updateRequests = new ArrayList<Integer>();
		this.chunks = new HashMap<Integer, Chunk>();
		this.chunkGarbage = new ArrayList<Integer>();
		this.modifiedBlocks = new ArrayList<Vec4i>();
		this.generated = false;

		this.initWorldData();
	}

	private void initWorldData()
	{
		worldSize = gameData.getWorldSize();
		worldGen = gameData.getWorldGen();
		World world = this;

				worldGen.addNoisePasses();
				worldGen.calcFinalNoise();
				for (int x = 0; x < worldSize; x++)
				{
					for (int y = 0; y < 5; y++)
					{
						for (int z = 0; z < worldSize; z++)
						{
							int index = Indexer.index3i(x, y, z);
							float[][] 	noise = worldGen.getNoiseChunk(x, z);
							Chunk c = new Chunk(x, y, z, noise, world);
							c.generateChunk();
							c.generateTerrainData();
							chunks.put(index, c);
						}
					}
				}
				for (int x = 0; x < worldSize; x++)
				{
					for (int y = 0; y < 5; y++)
					{
						for (int z = 0; z < worldSize; z++)
						{
							int index = Indexer.index3i(x, y, z);
							Chunk c = chunks.get(index);
							c.generateVegetation();
						}
					}
				}

				generated = true;

	}
	
	public void update()
	{
		for (Chunk c : chunks.values())
		{
			c.update();
		}
	}

	/**** Used for huge maps. If ever needed again. ****/


//	private void updateChunkGeneration()
//	{
//		Vec3 p = core.getGame().getPlayer().getPosition();
//
//		int x0 = (int) ((p.x - gameData.getViewDistance()) / (float) Chunk.SIZE);
//		int x1 = (int) ((p.x) / (float) Chunk.SIZE) + 1;
//		int x2 = (int) ((p.x + gameData.getViewDistance()) / (float) Chunk.SIZE) + 1;
//
//		int y0 = (int) ((p.y - gameData.getViewDistance()) / (float) Chunk.SIZE);
//		int y1 = (int) ((p.y + gameData.getViewDistance()) / (float) Chunk.SIZE);
//
//		int z0 = (int) ((p.z - gameData.getViewDistance()) / (float) Chunk.SIZE);
//		int z1 = (int) ((p.z) / (float) Chunk.SIZE) + 1;
//		int z2 = (int) ((p.z + gameData.getViewDistance()) / (float) Chunk.SIZE) + 1;
//
//		generateChunkCluster(x0, x1, y0, y1, z0, z1, worldSize);
//		generateChunkCluster(x1, x1, y0, y1, z1, z1, worldSize);
//		generateChunkCluster(x1, x2, y0, y1, z1, z2, worldSize);
//		generateChunkCluster(x0, x2, y0, y1, z0, z2, worldSize);
//
//		for (Chunk c : chunks.values())
//		{
//			if (!c.shouldBeRendered())
//			{
//				chunkGarbage.add(Indexer.index3i(c.getPosition()));
//			}
//			c.update();
//		}
//	}
//
//	private void generateChunkCluster(int x0, int x1, int y0, int y1, int z0, int z1, int size)
//	{
//		if (x0 < 0) x0 = 0;
//		if (x1 >= size) x1 = size - 1;
//		if (y0 < 0) y0 = 0;
//		if (y1 >= 2) y1 = 2;
//		if (z0 < 0) z0 = 0;
//		if (z1 >= size) z1 = size - 1;
//		for (int x = x0; x < x1; x++)
//		{
//			for (int y = y0; y < y1; y++)
//			{
//				for (int z = z0; z < z1; z++)
//				{
//					generateChunk(x, y, z);
//				}
//			}
//		}
//	}
//
//	private void generateChunk(int x, int y, int z)
//	{
//		int index = Indexer.index3i(x, y, z);
//		if (!generatingChunks.containsKey(index))
//		{
//			if (chunks.containsKey(index))
//			{
//				Chunk c = chunks.get(index);
//				c.setShouldBeRendered(true);
//				return;
//			}
//			//if (generatingChunks.size() < 10)
//			{
//				float[][] 	noise = worldGen.getNoiseChunk(x, z);
//				Chunk c = new Chunk(x, y, z, noise, this);
//				generatingChunks.put(index, c);
//				c.generateChunk();
//			}
//		}
//		else
//		{
//			Chunk c = generatingChunks.get(index);
//			if (c.isGenerated())
//			{
//				chunks.put(index, c);
//				generatingChunks.remove(index);
//			}
//		}
//	}



	public Chunk getChunk(int x, int y, int z)
	{
		return chunks.get(Indexer.index3i(x, y, z));
	}
	
	public void updateRequest(int x, int y, int z)
	{
		int xc = x / Chunk.SIZE;
		int yc = y / Chunk.SIZE;
		int zc = z / Chunk.SIZE;
		
		Chunk c = getChunk(xc, yc, zc);
		if (c == null)
			return;
		
		int index = Indexer.index3i(c.getPosition());
		updateChunk(index);
		checkChunksAroundOther(c, x % Chunk.SIZE, y % Chunk.SIZE, z % Chunk.SIZE);
	}

	public void updateRequest(int xp, int yp, int zp, int radius)
	{
		for (int x = 0; x < radius * 2; x++)
		{
			for (int y = 0; y < radius * 2; y++)
			{
				for (int z = 0; z < radius * 2; z++)
				{
					int xx = x + xp - radius;
					int yy = y + yp - radius;
					int zz = z + zp - radius;

					updateRequest(xx, yy, zz);
				}
			}
		}
	}

	public void checkChunksAroundOther(Chunk c, int x, int y, int z)
	{
		int xx = c.position.x;
		int yy = c.position.y;
		int zz = c.position.z;
		if (x == 0)
		{
			if (getChunk(xx - 1, yy, zz) != null)
			{
				updateChunk(Indexer.index3i(xx - 1, yy, zz));
			}
		}
		if (y == 0)
		{
			if (getChunk(xx, yy - 1, zz) != null)
			{
				updateChunk(Indexer.index3i(xx, yy - 1, zz));
			}
		}
		if (z == 0)
		{
			if (getChunk(xx, yy, zz - 1) != null)
			{
				updateChunk(Indexer.index3i(xx, yy, zz - 1));
			}
		}
		if (x == Chunk.SIZE - 1)
		{
			if (getChunk(xx + 1, yy, zz) != null)
			{
				updateChunk(Indexer.index3i(xx + 1, yy, zz));
			}
		}
		if (y == Chunk.SIZE - 1)
		{
			if (getChunk(xx, yy + 1, zz) != null)
			{
				updateChunk(Indexer.index3i(xx, yy + 1, zz));
			}
		}
		if (z == Chunk.SIZE - 1)
		{
			if (getChunk(xx, yy, zz + 1) != null)
			{
				updateChunk(Indexer.index3i(xx, yy, zz + 1));
			}
		}

		// Update corners for Ambient Occlusion
		if (x == 0 && z == 0)
		{
			if (getChunk(xx - 1, yy, zz - 1) != null)
			{
				updateChunk(Indexer.index3i(xx - 1, yy, zz - 1));
			}
		}
		if (x == Chunk.SIZE - 1 && z == 0)
		{
			if (getChunk(xx + 1, yy, zz - 1) != null)
			{
				updateChunk(Indexer.index3i(xx + 1, yy, zz - 1));
			}
		}
		if (x == Chunk.SIZE - 1 && z == Chunk.SIZE - 1)
		{
			if (getChunk(xx + 1, yy, zz + 1) != null)
			{
				updateChunk(Indexer.index3i(xx + 1, yy, zz + 1));
			}
		}
		if (x == 0 && z == Chunk.SIZE - 1)
		{
			if (getChunk(xx - 1, yy, zz + 1) != null)
			{
				updateChunk(Indexer.index3i(xx - 1, yy, zz + 1));
			}
		}
	}
	
	public void updateChunk(int index)
	{
		if (!updateRequests.contains(index))
			updateRequests.add(index);
	}
	
	public int getBlock(int x, int y, int z)
	{
		int xc = x / Chunk.SIZE;
		int yc = y / Chunk.SIZE;
		int zc = z / Chunk.SIZE;
		
		Chunk c = getChunk(xc, yc, zc);
		if (c == null)
			return 0;
		
		int xx = x % Chunk.SIZE;
		int yy = y % Chunk.SIZE;
		int zz = z % Chunk.SIZE;
		
		return c.getBlock(xx, yy, zz);
	}
	
	public void addBlock(int x, int y, int z, int block)
	{
		//addModifiedBlock(x, y, z, block);
		int xc = x / Chunk.SIZE;
		int yc = y / Chunk.SIZE;
		int zc = z / Chunk.SIZE;
		
		Chunk c = getChunk(xc, yc, zc);
		if (c == null)
			return;

		int xx = x % Chunk.SIZE;
		int yy = y % Chunk.SIZE;
		int zz = z % Chunk.SIZE;

		c.addBlock(xx, yy, zz, block);
	}
	
	public void removeBlock(int x, int y, int z)
	{
		addModifiedBlock(x, y, z, 0);
		int xc = x / Chunk.SIZE;
		int yc = y / Chunk.SIZE;
		int zc = z / Chunk.SIZE;
		
		Chunk c = getChunk(xc, yc, zc);
		if (c == null)
			return;
		
		int xx = x % Chunk.SIZE;
		int yy = y % Chunk.SIZE;
		int zz = z % Chunk.SIZE;
		
		c.removeBlock(xx, yy, zz);
	}
	
	public int getBlockAt(Vec3 point)
	{
		Vec3i ip = point.getInts();
		return getBlock(ip.x, ip.y, ip.z);
	}

	public List<Integer> getBlockInRange(Vec3 pos, int range)
	{
		List<Integer> result = new ArrayList<Integer>();

		int x0 = (int) pos.x - range;
		int x1 = (int) pos.x + range;
		int y0 = (int) pos.y - range;
		int y1 = (int) pos.y + range;
		int z0 = (int) pos.z - range;
		int z1 = (int) pos.z + range;
		
		for (int x = x0; x < x1; x++)
		{
			for (int y = y0; y < y1; y++)
			{
				for (int z = z0; z < z1; z++)
				{
					int block = getBlock(x, y, z);
					if (block != 0)
					{
						result.add(block);			
					}
				}	
			}	
		}
		
		return result;
	}
	
	public List<AABoxCollider> getAABoxInRange(Vec3 pos, int range)
	{
		List<AABoxCollider> result = new ArrayList<AABoxCollider>();

		int x0 = (int) pos.x - range;
		int x1 = (int) pos.x + range;
		int y0 = (int) pos.y - range;
		int y1 = (int) pos.y + range;
		int z0 = (int) pos.z - range;
		int z1 = (int) pos.z + range;
		
		for (int x = x0; x < x1; x++)
		{
			for (int y = y0; y < y1; y++)
			{
				for (int z = z0; z < z1; z++)
				{
					if (getBlock(x, y, z) != 0)
					{
						AABoxCollider c = new AABoxCollider();
						c.setPosition(new Vec3(x + 0.5f, y + 0.5f, z + 0.5f));
						result.add(c);
					}
				}	
			}	
		}
		
		return result;
	}
	
	public Map<Integer, Chunk> getRenderableChunks()
	{
		return chunks;
	}
	
	public Map<Integer, Chunk> getGeneretingChunks()
	{
		return chunks;
	}
	
	public List<Integer> getChunkGarbage()
	{
		return chunkGarbage;
	}

	public List<Integer> getUpdateRequests()
	{
		return updateRequests;
	}
	
	public void addModifiedBlock(int x, int y, int z, int block)
	{
		Vec4i v = getModifiedBlock(x, y, z);
		if (v != null)
		{
			int index = modifiedBlocks.indexOf(v);
			modifiedBlocks.get(index).w = block;
			return;
		}
		modifiedBlocks.add(new Vec4i(x, y, z, block));
	}
	
	public List<Vec4i> getModifiedBlocks()
	{
		return modifiedBlocks;
	}
	
	public Vec4i getModifiedBlock(int x, int y, int z)
	{
		for (int i = 0; i < modifiedBlocks.size(); i++)
		{
			Vec4i b = modifiedBlocks.get(i);
			if (b.x == x && b.y == y && b.z == z)
				return b;
		}
		return null;
	}

	public int getBlockDamage(int x, int y, int z, float damage)
	{
		return applyBlockDamage(getBlock(x, y, z), damage);
	}

	public int applyBlockDamage(int blck, float damage)
	{
		Color4f block = Color4f.getColorFromARGB(blck);

		block.setAlpha(block.getAlpha() - damage);

		Color4f a = block;
		Color4f b = Color4f.BLACK;

		System.out.println("A: " + block.getAlpha() + " " + damage);
		block = Color4f.mix(a, b, 1.0f - (block.getAlpha() * 0.5f + 0.5f));
		System.out.println("B: " + block.getAlpha() + " " + damage);

		return block.getARGB();
	}

	public void applyDamageForce(Vec3 pos, float force, boolean setBlockAndUpdate)
	{
		for (int x = 0; x < force * 2; x++)
		{
			for (int y = 0; y < force * 2; y++)
			{
				for (int z = 0; z < force * 2; z++)
				{
					int xp = (int) (pos.x + x - force);
					int yp = (int) (pos.y + y - force);
					int zp = (int) (pos.z + z - force);

					if (getBlock(xp, yp, zp) == 0)
						continue;

					float xx = x - force;
					float yy = y - force;
					float zz = z - force;

					float dist = Mathf.sqrt(xx * xx + yy * yy + zz * zz);

					if (dist < force)
					{
						float dammageAmnt = (1.0f - dist / force) * 3f;
						if (dammageAmnt < 0) dammageAmnt = 0;
						if (dammageAmnt > 1) dammageAmnt = 1;

						int block = applyBlockDamage(getBlock(xp, yp, zp), dammageAmnt);
						float alpha = Color4f.getColorFromARGB(block).getAlpha();
						if (alpha <= 0)
							block = 0;
						addModifiedBlock(xp, yp, zp, block);
						if (setBlockAndUpdate)
						{
							if (block == 0)
								removeBlock(xp, yp, zp);
							else
								addBlock(xp, yp, zp, block);
							updateRequest(xp, yp, zp);
						}
					}
				}
			}
		}
	}

	public WorldGen getWorldGen()
	{
		return worldGen;
	}

	public boolean isGenerated()
	{
		return generated;
	}
}
