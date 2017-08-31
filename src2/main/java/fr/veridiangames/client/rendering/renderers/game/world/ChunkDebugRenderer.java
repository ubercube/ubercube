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

package fr.veridiangames.client.rendering.renderers.game.world;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Map;

import org.lwjgl.BufferUtils;

import fr.veridiangames.client.rendering.buffers.Buffers;
import fr.veridiangames.core.game.world.Chunk;

/**
 * Created by Marccspro on 27 f�vr. 2016.
 */
public class ChunkDebugRenderer
{
	public static final int MAX_CHUNKS = 100 * 100 * 5;

	private static FloatBuffer instanceBuffer;
	private int vao, vbo, vio, ibo;

	public ChunkDebugRenderer()
	{
		ChunkDebugRenderer.instanceBuffer = BufferUtils.createFloatBuffer(MAX_CHUNKS * 3);
		for (int i = 0; i < MAX_CHUNKS; i++)
		{
			instanceBuffer.put(0);
			instanceBuffer.put(0);
			instanceBuffer.put(0);
		}
		instanceBuffer.flip();

		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(this.cubeVertices().length);
		verticesBuffer.put(this.cubeVertices());
		verticesBuffer.flip();

		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(this.cubeIndices().length);
		indicesBuffer.put(this.cubeIndices());
		indicesBuffer.flip();

		this.vao = Buffers.createVertexArray();
		this.vbo = Buffers.createVertexBuffer();
		this.vio = Buffers.createVertexBuffer();
		this.ibo = Buffers.createVertexBuffer();

		glBindVertexArray(this.vao);

		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(2);

		glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0L);

		glBindBuffer(GL_ARRAY_BUFFER, this.vio);
		glBufferData(GL_ARRAY_BUFFER, instanceBuffer, GL_DYNAMIC_DRAW);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 3 * 4, 0L);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

		glVertexAttribDivisor(0, 0);
		glVertexAttribDivisor(2, 1);

		glBindVertexArray(0);
	}

	public void updateInstances(Map<Integer, Chunk> chunks)
	{
		instanceBuffer.clear();
		for (Chunk c : chunks.values())
		{
			instanceBuffer.put(c.getPosition().x * Chunk.SIZE);
			instanceBuffer.put(c.getPosition().y * Chunk.SIZE);
			instanceBuffer.put(c.getPosition().z * Chunk.SIZE);
		}
		instanceBuffer.flip();
		glBindBuffer(GL_ARRAY_BUFFER, this.vio);
		glBufferData(GL_ARRAY_BUFFER, instanceBuffer, GL_DYNAMIC_DRAW);
	}

	public void render(Map<Integer, Chunk> chunks)
	{
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glBindVertexArray(this.vao);
		glDrawElementsInstanced(GL_QUADS, this.cubeIndices().length, GL_UNSIGNED_INT, 0, chunks.size());
		glBindVertexArray(0);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	}

	private float[] cubeVertices()
	{
		float size = 16;
		return new float[]
		{
			0, 0, 0,
			size, 0, 0,
			size, 0, size,
			0, 0, size,

			0, size, 0,
			size, size, 0,
			size, size, size,
			0, size, size
		};
	}

	private int[] cubeIndices()
	{
		return new int[]
		{
//			0, 1, 2, 0, 2, 3,
//			1, 5, 6, 1, 6, 2,
//			5, 4, 7, 5, 7, 6,
//			4, 0, 3, 4, 3, 7,
//			1, 0, 4, 1, 4, 5,
//			3, 2, 6, 3, 6, 7
			0, 1, 2, 3,
			4, 5, 6, 7,
			1, 5, 6, 2,
			4, 0, 3, 7,
			0, 4, 5, 1,
			3, 2, 6, 7
		};
	}
}
