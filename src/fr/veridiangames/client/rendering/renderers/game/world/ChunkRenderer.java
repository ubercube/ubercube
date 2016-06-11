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

package fr.veridiangames.client.rendering.renderers.game.world;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import fr.veridiangames.core.game.world.Chunk;
import fr.veridiangames.core.game.world.World;
import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.client.rendering.Camera;
import fr.veridiangames.client.rendering.buffers.Buffers;

public class ChunkRenderer
{
	public static final int	BLOCK_SIZE	= 6 * 4 * (3 + 4 + 3);
	public static final int	CHUNK_SIZE	= Chunk.SIZE * Chunk.SIZE * Chunk.SIZE;

	private static final int	VERTEX_LOCATION	= 0;
	private static final int	COLOR_LOCATION	= 1;
	private static final int	NORMAL_LOCATION	= 2;

	private boolean				enableAO	= true;
	private int					vao, vbo, cbo;
	private static FloatBuffer	vertexBuffer;
	private int					bufferSize;
	
	private boolean isInViewDistance = false;
	private boolean isInViewFrustum = false;
	
	private Chunk	chunk;
	private World	world;

	private boolean removed = false;

	public ChunkRenderer(Chunk chunk, World world)
	{
		this.chunk = chunk;
		this.world = world;

		createBuffer();
	}

	public void createBuffer()
	{
		vao = Buffers.createVertexArray();
		vbo = Buffers.createVertexBuffer();
		cbo = Buffers.createVertexBuffer();

		if (vertexBuffer == null)
		{
			vertexBuffer = BufferUtils.createFloatBuffer(CHUNK_SIZE * BLOCK_SIZE);
		}

		update();
	}
	
	public void update()
	{
		updateChunk();
		updateBuffer();
	}
	
	public void updateChunk()
	{
		bufferSize = 0;
		for (int x = 0; x < Chunk.SIZE; x++)
		{
			for (int y = 0; y < Chunk.SIZE; y++)
			{
				for (int z = 0; z < Chunk.SIZE; z++)
				{
					int block = chunk.blocks[x][y][z];
					if (block == 0)
						continue;

					int xx = chunk.position.x * Chunk.SIZE + x;
					int yy = chunk.position.y * Chunk.SIZE + y;
					int zz = chunk.position.z * Chunk.SIZE + z;

					int upBlock = world.getBlock(xx, yy + 1, zz);
					int downBlock = world.getBlock(xx, yy - 1, zz);
					int leftBlock = world.getBlock(xx - 1, yy, zz);
					int rightBlock = world.getBlock(xx + 1, yy, zz);
					int frontBlock = world.getBlock(xx, yy, zz - 1);
					int backBlock = world.getBlock(xx, yy, zz + 1);

					boolean up = upBlock == 0;
					boolean down = downBlock == 0;
					boolean left = leftBlock == 0;
					boolean right = rightBlock == 0;
					boolean front = frontBlock == 0;
					boolean back = backBlock == 0;

					if (!up && !down && !left && !right && !front && !back)
						continue;

					float brightness = 1;
					int size = calcBlock(vertexBuffer, xx, yy, zz, block, brightness, up, down, left, right, front, back);
					
					bufferSize += size * 4;
				}
			}
		}

		vertexBuffer.flip();
	}

	public void updateBuffer()
	{
		glBindVertexArray(vao);

		glEnableVertexAttribArray(VERTEX_LOCATION);
		glEnableVertexAttribArray(COLOR_LOCATION);
		glEnableVertexAttribArray(NORMAL_LOCATION);

		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(VERTEX_LOCATION, 3, GL_FLOAT, false, 10 * 4, 0L);
		glVertexAttribPointer(COLOR_LOCATION, 4, GL_FLOAT, false, 10 * 4, 12L);
		glVertexAttribPointer(NORMAL_LOCATION, 3, GL_FLOAT, false, 10 * 4, 28L);

		glBindVertexArray(0);

		vertexBuffer.clear();
	}

	public void updateCulling(Camera camera)
	{
		isInViewDistance = camera.isInViewDistance(chunk.centerPosition, 48);
		isInViewFrustum = camera.isInViewFrustum(chunk.centerPosition, 16);
	}

	public void render()
	{
		removed = true;
		if (bufferSize == 0)
			return;
		if (!isInViewDistance)
			return;
		if (!isInViewFrustum)
			return;

		glBindVertexArray(vao);
		glDrawArrays(GL_QUADS, 0, bufferSize);
		glBindVertexArray(0);
	}

	public void dispose()
	{
		Buffers.deleteVertexArray(vao);
		Buffers.deleteVertexBuffer(vbo);
		Buffers.deleteVertexBuffer(cbo);
	}

	private int calcBlock(FloatBuffer buffer, int xx, int yy, int zz, int block, float brightness, boolean up, boolean down, boolean left, boolean right, boolean front, boolean back)
	{
		float ao = 0.95f;
		Color4f color = new Color4f(block);
		int size = 0;
		if (up)
		{
			float[] shading = new float[]
					{ 1, 1, 1, 1 };

			if (enableAO)
			{
				if (world.getBlock(xx - 1, yy + 1, zz) != 0)
				{
					shading[0] *= ao;
					shading[3] *= ao;
				}
				if (world.getBlock(xx - 1, yy + 1, zz - 1) != 0)
				{
					shading[0] *= ao;
				}
				if (world.getBlock(xx, yy + 1, zz - 1) != 0)
				{
					shading[0] *= ao;
					shading[1] *= ao;
				}
				if (world.getBlock(xx + 1, yy + 1, zz) != 0)
				{
					shading[1] *= ao;
					shading[2] *= ao;
				}
				if (world.getBlock(xx + 1, yy + 1, zz - 1) != 0)
				{
					shading[1] *= ao;
				}
				if (world.getBlock(xx + 1, yy + 1, zz + 1) != 0)
				{
					shading[2] *= ao;
				}
				if (world.getBlock(xx, yy + 1, zz + 1) != 0)
				{
					shading[2] *= ao;
					shading[3] *= ao;
				}
				if (world.getBlock(xx - 1, yy + 1, zz + 1) != 0)
				{
					shading[3] *= ao;
				}
			}
			buffer.put(BlockData.blockDataUp(xx, yy, zz, brightness, shading, color));
			size++;
		}
		if (down)
		{
			float[] shading = new float[]
			{ 1, 1, 1, 1 };
			if (enableAO)
			{

				if (world.getBlock(xx - 1, yy - 1, zz) != 0)
				{
					shading[1] *= ao;
					shading[2] *= ao;
				}
				if (world.getBlock(xx - 1, yy - 1, zz - 1) != 0)
				{
					shading[1] *= ao;
				}
				if (world.getBlock(xx, yy - 1, zz - 1) != 0)
				{
					shading[1] *= ao;
					shading[0] *= ao;
				}
				if (world.getBlock(xx + 1, yy - 1, zz) != 0)
				{
					shading[0] *= ao;
					shading[3] *= ao;
				}
				if (world.getBlock(xx + 1, yy - 1, zz - 1) != 0)
				{
					shading[0] *= ao;
				}
				if (world.getBlock(xx + 1, yy - 1, zz + 1) != 0)
				{
					shading[3] *= ao;
				}
				if (world.getBlock(xx, yy - 1, zz + 1) != 0)
				{
					shading[3] *= ao;
					shading[2] *= ao;
				}
				if (world.getBlock(xx - 1, yy - 1, zz + 1) != 0)
				{
					shading[2] *= ao;
				}
			}
			buffer.put(BlockData.blockDataDown(xx, yy, zz, brightness, shading, color));
			size++;
		}
		if (left)
		{
			float[] shading = new float[]
			{ 1, 1, 1, 1 };
			if (enableAO)
			{

				if (world.getBlock(xx - 1, yy - 1, zz) != 0)
				{
					shading[0] *= ao;
					shading[3] *= ao;
				}
				if (world.getBlock(xx - 1, yy - 1, zz - 1) != 0)
				{
					shading[0] *= ao;
				}
				if (world.getBlock(xx - 1, yy, zz - 1) != 0)
				{
					shading[0] *= ao;
					shading[1] *= ao;
				}
				if (world.getBlock(xx - 1, yy + 1, zz) != 0)
				{
					shading[1] *= ao;
					shading[2] *= ao;
				}
				if (world.getBlock(xx - 1, yy + 1, zz - 1) != 0)
				{
					shading[1] *= ao;
				}
				if (world.getBlock(xx - 1, yy + 1, zz + 1) != 0)
				{
					shading[2] *= ao;
				}
				if (world.getBlock(xx - 1, yy, zz + 1) != 0)
				{
					shading[2] *= ao;
					shading[3] *= ao;
				}
				if (world.getBlock(xx - 1, yy - 1, zz + 1) != 0)
				{
					shading[3] *= ao;
				}
			}
			buffer.put(BlockData.blockDataLeft(xx, yy, zz, brightness, shading, color));
			size++;
		}
		if (right)
		{
			float[] shading = new float[]
			{ 1, 1, 1, 1 };

			if (enableAO)
			{
				if (world.getBlock(xx + 1, yy - 1, zz) != 0)
				{
					shading[1] *= ao;
					shading[2] *= ao;
				}
				if (world.getBlock(xx + 1, yy - 1, zz - 1) != 0)
				{
					shading[1] *= ao;
				}
				if (world.getBlock(xx + 1, yy, zz - 1) != 0)
				{
					shading[1] *= ao;
					shading[0] *= ao;
				}
				if (world.getBlock(xx + 1, yy + 1, zz) != 0)
				{
					shading[0] *= ao;
					shading[3] *= ao;
				}
				if (world.getBlock(xx + 1, yy + 1, zz - 1) != 0)
				{
					shading[0] *= ao;
				}
				if (world.getBlock(xx + 1, yy + 1, zz + 1) != 0)
				{
					shading[3] *= ao;
				}
				if (world.getBlock(xx + 1, yy, zz + 1) != 0)
				{
					shading[3] *= ao;
					shading[2] *= ao;
				}
				if (world.getBlock(xx + 1, yy - 1, zz + 1) != 0)
				{
					shading[2] *= ao;
				}
			}
			buffer.put(BlockData.blockDataRight(xx, yy, zz, brightness, shading, color));
			size++;
		}
		if (front)
		{
			float[] shading = new float[]
			{ 1, 1, 1, 1 };
			if (enableAO)
			{
				if (world.getBlock(xx, yy - 1, zz - 1) != 0)
				{
					shading[0] *= ao;
					shading[1] *= ao;
				}
				if (world.getBlock(xx - 1, yy - 1, zz - 1) != 0)
				{
					shading[0] *= ao;
				}
				if (world.getBlock(xx - 1, yy, zz - 1) != 0)
				{
					shading[0] *= ao;
					shading[3] *= ao;
				}

				if (world.getBlock(xx + 1, yy - 1, zz - 1) != 0)
				{
					shading[1] *= ao;
				}
				if (world.getBlock(xx + 1, yy, zz - 1) != 0)
				{
					shading[1] *= ao;
					shading[2] *= ao;
				}

				if (world.getBlock(xx, yy + 1, zz - 1) != 0)
				{
					shading[2] *= ao;
					shading[3] *= ao;
				}
				if (world.getBlock(xx + 1, yy + 1, zz - 1) != 0)
				{
					shading[2] *= ao;
				}
				if (world.getBlock(xx - 1, yy + 1, zz - 1) != 0)
				{
					shading[3] *= ao;
				}
			}
			buffer.put(BlockData.blockDataFront(xx, yy, zz, brightness, shading, color));
			size++;
		}
		if (back)
		{
			float[] shading = new float[]
			{ 1, 1, 1, 1 };
			if (enableAO)
			{
				if (world.getBlock(xx, yy - 1, zz + 1) != 0)
				{
					shading[1] *= ao;
					shading[0] *= ao;
				}
				if (world.getBlock(xx - 1, yy - 1, zz + 1) != 0)
				{
					shading[1] *= ao;
				}
				if (world.getBlock(xx - 1, yy, zz + 1) != 0)
				{
					shading[1] *= ao;
					shading[2] *= ao;
				}

				if (world.getBlock(xx + 1, yy - 1, zz + 1) != 0)
				{
					shading[0] *= ao;
				}
				if (world.getBlock(xx + 1, yy, zz + 1) != 0)
				{
					shading[0] *= ao;
					shading[3] *= ao;
				}

				if (world.getBlock(xx, yy + 1, zz + 1) != 0)
				{
					shading[3] *= ao;
					shading[2] *= ao;
				}
				if (world.getBlock(xx + 1, yy + 1, zz + 1) != 0)
				{
					shading[3] *= ao;
				}
				if (world.getBlock(xx - 1, yy + 1, zz + 1) != 0)
				{
					shading[2] *= ao;
				}
			}
			buffer.put(BlockData.blockDataBack(xx, yy, zz, brightness, shading, color));
			size++;
		}

		return size;
	}

	public boolean isRemoved()
	{
		return removed;
	}

	public void setRemoved(boolean removed)
	{
		this.removed = removed;
	}
}
