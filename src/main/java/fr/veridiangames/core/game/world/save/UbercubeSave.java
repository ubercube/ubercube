package fr.veridiangames.core.game.world.save;

import java.io.*;
import java.nio.ByteBuffer;

import fr.veridiangames.core.game.Game;
import fr.veridiangames.core.game.world.Chunk;
import fr.veridiangames.core.maths.Vec4i;
import fr.veridiangames.core.utils.FileUtils;

public class UbercubeSave 
{
	public static void writeWorldToStreamAndClose(Game game, OutputStream fos)
	{
		try
		{
			try
			{
				ByteBuffer dataBuffer = ByteBuffer.wrap(new byte[9]);// Max world size (320*320*80*4) + (4*320*320)
				boolean seededWorld = game.getWorld().getWorldGen().getSeed() > 0;
				
				if (seededWorld)
				{
					dataBuffer.put((byte)1);// The world has been generated with a seed
					dataBuffer.putLong(game.getWorld().getWorldGen().getSeed());// Write the seed (42)
					fos.write(dataBuffer.array());
				}
				else
					fos.write(0);
				
				dataBuffer = ByteBuffer.wrap(new byte[65536]);// Max chunk size (4*4*4096)
				Chunk chunk;
				for (int x = 0; x < game.getWorld().getWorldSize() ; x++)
				{
					for (int y = 0; y < 16 ; y++)
					{
						for (int z = 0; z < game.getWorld().getWorldSize() ; z++)
						{
							if ((chunk = game.getWorld().getChunk(x, y, z)) != null)
							{
								if (seededWorld)
								{
									short modifiedBlockNumber = 0; // number of modified block for this chunk
									//Chunk chunk = game.getWorld().getChunk(x, y, z);
									for (int xChunk=0; xChunk < Chunk.SIZE ; xChunk++)
									{
										for (int yChunk=0; yChunk < Chunk.SIZE ; yChunk++)
										{
											for (int zChunk=0; zChunk < Chunk.SIZE ; zChunk++)
											{
												Vec4i v = game.getWorld().getModifiedBlock(x*Chunk.SIZE+xChunk, y*Chunk.SIZE+yChunk, z*Chunk.SIZE+zChunk);
												//if (v == null) enable if not generated with a seed
													//dataBuffer.putInt(chunk.getBlock(xChunk, yChunk, zChunk));
												if (v != null)
												{
													modifiedBlockNumber ++;
													dataBuffer.putInt(v.x);
													dataBuffer.putInt(v.y);
													dataBuffer.putInt(v.z);
													dataBuffer.putInt(v.w);
												}
											}
										}
									}
									fos.write(modifiedBlockNumber >> 8);
									fos.write(modifiedBlockNumber & 0b11111111);
									fos.write(dataBuffer.array(), 0, dataBuffer.position());
									dataBuffer.clear();
								}
								else
								{
									dataBuffer.putShort((short)x);
									dataBuffer.putShort((short)y);
									dataBuffer.putShort((short)z);
									int currentBlock = 0, newBlock;
									short sameBlockNumber = 0, size = 6;//size = 6 cuz 3 short = 6 bytes (x, y, z)
									for (int i=0;i<4096;i++)//XZY
									{
										int xb = (i & 0b1111), yb = (i >> 8)&0b1111, zb = (i >> 4)&0b1111;
										Vec4i v = game.getWorld().getModifiedBlock(x*Chunk.SIZE + xb, y*Chunk.SIZE + yb, z*Chunk.SIZE + zb);
										if (v != null)
											newBlock = v.w;
										else
											newBlock = chunk.blocks[xb][yb][zb];
										
										if (newBlock != currentBlock && i != 0)
										{//Write when block change, but not the first as we don't have a previous one
											dataBuffer.putShort(sameBlockNumber);
											dataBuffer.putInt(currentBlock);
											size += 6; // short + int = 6
											currentBlock = newBlock;
											sameBlockNumber = 1;
										}
										else
											sameBlockNumber++;
									}
									dataBuffer.putShort(sameBlockNumber);
									dataBuffer.putInt(currentBlock);//Write the last blocks
									size += 6;// short + int = 6
									
									fos.write(size >> 8);
									fos.write(size & 0b11111111);
									fos.write(dataBuffer.array(), 0, dataBuffer.position());
									dataBuffer.clear();
								}
							}
						}
					}
				}
			}
			catch(IOException e){e.printStackTrace();}
			finally{fos.close();}
		}
		catch(IOException e){e.printStackTrace();}
	}
	public static void saveGame(Game game)
	{
		if (!FileUtils.fileExist("save"))
			FileUtils.newFile("save");
		try 
		{
			FileOutputStream fos = new FileOutputStream(new File("save/world.ucw"));
			writeWorldToStreamAndClose(game, fos);
		} 
		catch (FileNotFoundException e) {e.printStackTrace();}
	}
}
