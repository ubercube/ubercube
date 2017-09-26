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

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.data.GameData;
import fr.veridiangames.core.game.data.world.WorldGen;
import fr.veridiangames.core.game.data.world.WorldType;
import fr.veridiangames.core.game.world.save.MinecraftBlock;
import fr.veridiangames.core.game.world.save.MinecraftFile;
import fr.veridiangames.core.game.world.save.NBTReader;
import fr.veridiangames.core.maths.Mathf;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.maths.Vec3i;
import fr.veridiangames.core.maths.Vec4i;
import fr.veridiangames.core.network.packets.WorldFileSizePacket;
import fr.veridiangames.core.physics.colliders.AABoxCollider;
import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.core.utils.Indexer;
import fr.veridiangames.core.utils.Log;
import fr.veridiangames.server.FileManager;

public class World
{
	private Map<Integer, Chunk> generatingChunks;
	private List<Integer> 		updateRequests;
	private Map<Integer, Chunk> chunks;
	private List<Integer> 		chunkGarbage;
	private List<Vec4i>			modifiedBlocks;

	private GameData	data;
	private GameCore	core;
	private boolean 	generated;
	
	public World(GameCore core, long seed)
	{
		this.core = core;
		this.data = core.getGame().getData();
		this.generatingChunks = new HashMap<Integer, Chunk>();
		this.updateRequests = new ArrayList<Integer>();
		this.chunks = new HashMap<Integer, Chunk>();
		this.chunkGarbage = new ArrayList<Integer>();
		this.modifiedBlocks = new ArrayList<Vec4i>();
		this.generated = false;

		if (seed != -1)
		{
			if (FileManager.shouldLoadMinecraftWorld())
				this.readMinecraftData();
			else if (FileManager.shouldLoadUbercubeWorld())
				this.readUbercubeSaveFile();
			else
				this.initWorldData(seed);
		}
	}

	private void readMinecraftData()
	{
		Log.println("Converting minecraft world");
		this.data.createWorld(new WorldGen(-1, 15), WorldType.NORMAL);
		MinecraftFile file = new MinecraftFile(new File("save/r.0.0.mca"));
		for (int x=0;x<data.getWorldSize();x++)
		{
			for (int z=0;z<data.getWorldSize();z++)
			{
				if (file.hasChunk(x, z))
				{
					try
					{
						DataInputStream dis = file.getChunkDataInputStream(x, z);
						while (dis.available() != 0)
						{
							try
							{
								NBTReader reader = new NBTReader(dis);
								
								byte[] blocks = reader.getNextByteArrayTagData("Blocks");
								byte y = reader.getNextByteTagData("Y");
								byte[] add = reader.getNextByteArrayTagData("Data");
								
								int index = Indexer.index3i(data.getWorldSize()-1-x, y, z);
								Chunk c = new Chunk(new Vec3i(data.getWorldSize()-1-x, y, z));
								chunks.put(index, c);
								
								for (int i=0;i<4096;i++)
								{
									int bx = (i>>0)&0b1111;
									int by = (i>>8)&0b1111;
									int bz = (i>>4)&0b1111;
									c.blocks[15 - bx][by][bz] = MinecraftBlock.getColor(blocks[i], (i % 2 == 1) ? add[i/2] >> 4 : add[i/2] & 0b1111);
								}
							}
							catch(Exception e1)
							{
								// e1.printStackTrace(); //Probably end of stream
							}
						}
						dis.close();
					}
					catch(Exception e){e.printStackTrace();}
				}
			}
		}
		try
		{
			file.close();
		}
		catch(IOException e){e.printStackTrace();}
		generated = true;
	}
	private void readUbercubeSaveFile()
	{
		try 
		{
			readWorldData(new DataInputStream(new FileInputStream(new File("save/world.ucw"))));
		} 
		catch (FileNotFoundException e) {e.printStackTrace();}
	}
	/**
	 * Client side only
	 */
	public void readServerWorld()
	{
		readWorldData(new DataInputStream(new ByteArrayInputStream(WorldFileSizePacket.completeDatas)));
	}
	
	private void readWorldData(DataInputStream dis)
	{
		Log.println("Reading world data");
		try
		{
			try
			{
				boolean seeded = dis.read() == 1;
				if (seeded)
				{
					long seed = dis.readLong();
					Log.println("World seed : "+seed);
					initWorldData(seed);
					for (int x = 0; x < getWorldSize(); x++)
					{
						for (int y = 0; y < 5; y++)
						{
							for (int z = 0; z < getWorldSize(); z++)
							{
								short modifiedBlockNumber = dis.readShort();
								byte[] chunk = new byte[modifiedBlockNumber * 16];
								dis.read(chunk);
								ByteBuffer dataBuffer = ByteBuffer.wrap(chunk);
								
								for (int i=0;i<modifiedBlockNumber;i++)
									this.modifiedBlocks.add(new Vec4i(dataBuffer.getInt(),dataBuffer.getInt(),dataBuffer.getInt(),dataBuffer.getInt()));
							}
						}
					}
				}
				else
				{
					this.data.createWorld(new WorldGen(-1, 15), WorldType.NORMAL);
					Log.println("Reading unseeded world");
					while (dis.available() != 0)
					{
						int size = (dis.read() << 8) + dis.read();
						byte[] chunk = new byte[size];
						dis.read(chunk);
						ByteBuffer dataBuffer = ByteBuffer.wrap(chunk);
						
						int x = dataBuffer.getShort(), y = dataBuffer.getShort(), z = dataBuffer.getShort();
						
						if (x >= 0 && x < data.getWorldSize() && z >= 0 && z < data.getWorldSize() && y >= 0 && y < 16)
						{
							int index = Indexer.index3i(x, y, z);
							Chunk c = new Chunk(new Vec3i(x, y, z));
							chunks.put(index, c);
							
							int blocksRed = 0;
							while (blocksRed < 4096)
							{
								short number = dataBuffer.getShort();
								int blockType = dataBuffer.getInt();
								for (int i=0;i<number;i++)
								{
									int total = blocksRed + i;
									int bx = (total>>0)&0b1111;
									int by = (total>>8)&0b1111;
									int bz = (total>>4)&0b1111;
									c.blocks[total&0b1111][(total >> 8)&0b1111][(total >> 4)&0b1111] = blockType;
								}
								blocksRed += number;
							}
						}
					}
				}
			}
			catch(IOException e){e.printStackTrace();}
			finally{dis.close();}
			generated = true;
		}
		catch(IOException e){e.printStackTrace();initWorldData(42);}
	}
	
	private void initWorldData(long seed)
	{
		this.data.createWorld(new WorldGen(seed, 15), (seed % 2) == 0 ? WorldType.SNOWY : WorldType.NORMAL);
		World world = this;

		data.getWorldGen().addNoisePasses(data.getWorldType());
		data.getWorldGen().calcFinalNoise();
		for (int x = 0; x < data.getWorldSize(); x++)
		{
			for (int y = 0; y < 5; y++)
			{
				for (int z = 0; z < data.getWorldSize(); z++)
				{
					int index = Indexer.index3i(x, y, z);
					float[][] 	noise = data.getWorldGen().getNoiseChunk(x, z);
					Chunk c = new Chunk(x, y, z, noise, world);
					c.generateChunk();
					c.generateTerrainData();
					chunks.put(index, c);
				}
			}
		}
		for (int x = 0; x < data.getWorldSize(); x++)
		{
			for (int y = 0; y < 1; y++)
			{
				for (int z = 0; z < data.getWorldSize(); z++)
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
		if (y == 0)
			return;

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
		return applyBlockDamage(x, y, z, damage);
	}

	public int applyBlockDamage(int x, int y, int z, float damage)
	{
		int blockID = 0;


		Vec4i modBlock = getModifiedBlock(x, y, z);
		if (modBlock != null)
			blockID = modBlock.w;
		else
			blockID = getBlock(x, y, z);

		Color4f block = Color4f.getColorFromARGB(blockID);

		block.setAlpha(block.getAlpha() - damage);

		Color4f a = block;
		Color4f b = Color4f.BLACK;

		block = Color4f.mix(a, b, 1.0f - (block.getAlpha() * 0.3f + 0.7f));

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

						int block = applyBlockDamage(xp, yp, zp, dammageAmnt);
						float alpha = Color4f.getColorFromARGB(block).getAlpha();
						if (alpha <= 0)
							block = 0;
						if (setBlockAndUpdate)
						{
							if (block == 0)
								removeBlock(xp, yp, zp);
							else
								addBlock(xp, yp, zp, block);
							updateRequest(xp, yp, zp);
						}
						addModifiedBlock(xp, yp, zp, block);
					}
				}
			}
		}
	}

	public WorldGen getWorldGen()
	{
		return data.getWorldGen();
	}

	public WorldType getWorldType() {
		return data.getWorldType();
	}

	public boolean isGenerated()
	{
		return generated;
	}

	public int getWorldSize() {
		return data.getWorldSize();
	}
	
	public void setGenerated()
	{
		generated = true;
	}
}
/*
[ERR] java.lang.NullPointerException
[ERR]     at org.lwjgl.system.APIUtil.apiGetManifestValue(APIUtil.java:97)
[ERR]     at org.lwjgl.system.Library.chec
[OUT] 	  glfwInit... kHash(Library.java:260)
[OUT] ====== Loading Display... ======
[ERR]     at org.lwjgl.system.Library.<clinit>(Library.java:44)
[ERR]     at org.lwjgl.system.MemoryAccess.<clinit>(MemoryAccess.java:17)
[ERR]     at org.lwjgl.system.ThreadLocalUtil$UnsafeState.<clinit>(ThreadL
[ERR]     at org.lwjgl.system.ThreadLocalUtil.getInstance(ThreadLocalUtil.java:43) ocalUtil.java:86)
[ERR]     at org.lwjgl.system.ThreadLocalUtil.<clinit>(ThreadLocalUtil.java:20)
[ERR]     at org.lwjgl.system.MemoryStack.stackPush(MemoryStack.java:577)
[ERR]     at org.lwjgl.system.Callback.<clinit>(Callback.java:35)
[ERR]     at fr.veridiangames.client.inputs.Input.<init>(Input.java:199)
[ERR]     at fr.veridiangames.client.rendering.Display.<init>(Display.java:67)
[ERR]     at fr.veridiangames.client.MainComponent.main(MainComponent.java:36)
*/