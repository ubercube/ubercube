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

package fr.veridiangames.client.rendering.renderers.game.entities;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;

import fr.veridiangames.client.rendering.buffers.Buffers;
import fr.veridiangames.client.rendering.shaders.Shader;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.components.ECRender;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.game.entities.particles.ParticleSystem;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.utils.Color4f;

/**
 * Created by Marccspro on 8 f�vr. 2016.
 */
public class EntityRenderer
{
	public static final int MAX_ENTITIES = 2000;

	private FloatBuffer instanceBuffer;
	private int vao, vbo, vio, ibo;
	private int renderCount;

	public EntityRenderer()
	{
		this.instanceBuffer = BufferUtils.createFloatBuffer(MAX_ENTITIES * 20);
		for (int i = 0; i < MAX_ENTITIES; i++)
		{
			this.instanceBuffer.put(Mat4.identity().getComponents());
			this.instanceBuffer.put(Color4f.YELLOW.toArray());
		}
		this.instanceBuffer.flip();

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
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		glEnableVertexAttribArray(4);
		glEnableVertexAttribArray(5);

		glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0L);

		glBindBuffer(GL_ARRAY_BUFFER, this.vio);
		glBufferData(GL_ARRAY_BUFFER, this.instanceBuffer, GL_DYNAMIC_DRAW);
		glVertexAttribPointer(1, 4, GL_FLOAT, false, 20 * 4, 64L);
		glVertexAttribPointer(2, 4, GL_FLOAT, false, 20 * 4, 0L);
		glVertexAttribPointer(3, 4, GL_FLOAT, false, 20 * 4, 16L);
		glVertexAttribPointer(4, 4, GL_FLOAT, false, 20 * 4, 32L);
		glVertexAttribPointer(5, 4, GL_FLOAT, false, 20 * 4, 48L);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

		glVertexAttribDivisor(0, 0);
		glVertexAttribDivisor(1, 1);
		glVertexAttribDivisor(2, 1);
		glVertexAttribDivisor(3, 1);
		glVertexAttribDivisor(4, 1);
		glVertexAttribDivisor(5, 1);

		glBindVertexArray(0);
	}

	public void updateInstances(Map<Integer, Entity> entities, List<Integer> indices)
	{
		this.renderCount = 0;
		this.instanceBuffer.clear();
		for (int i = 0; i < indices.size(); i++)
		{
			Entity e = entities.get(indices.get(i));
			if (e == null)
				continue;
			if (e instanceof Player)
				continue;
			if (e instanceof ParticleSystem)
				continue;
			if (!e.contains(EComponent.RENDER))
				continue;
			if (e.contains(EComponent.MODEL))
				continue;

			this.renderCount++;

			Transform transform = ((ECRender) e.get(EComponent.RENDER)).getTransform();

			this.instanceBuffer.put(transform.toMatrix().getComponents());
			this.instanceBuffer.put(Color4f.YELLOW.toArray());
		}
		this.instanceBuffer.flip();
		glBindBuffer(GL_ARRAY_BUFFER, this.vio);
		glBufferData(GL_ARRAY_BUFFER, this.instanceBuffer, GL_DYNAMIC_DRAW);
	}

	public void render(Shader shader, Map<Integer, Entity> entities, List<Integer> indices)
	{
		glBindVertexArray(this.vao);
		glDrawElementsInstanced(GL_TRIANGLES, this.cubeIndices().length, GL_UNSIGNED_INT, 0L, this.renderCount);
		glBindVertexArray(0);
	}

	private float[] cubeVertices()
	{
		return new float[]
		{
			-1, -1, -1,
			1, -1, -1,
			1, -1, 1,
			-1, -1, 1,

			-1, 1, -1,
			1, 1, -1,
			1, 1, 1,
			-1, 1, 1
		};
	}

	private int[] cubeIndices()
	{
		return new int[]
		{
			0, 1, 2, 0, 2, 3,
			1, 5, 6, 1, 6, 2,

			5, 4, 7, 5, 7, 6,
			4, 0, 3, 4, 3, 7,

			1, 0, 4, 1, 4, 5,
			3, 2, 6, 3, 6, 7
		};
	}
}
