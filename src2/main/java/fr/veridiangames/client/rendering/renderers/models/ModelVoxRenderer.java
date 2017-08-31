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

package fr.veridiangames.client.rendering.renderers.models;

import static fr.veridiangames.client.FileManager.getResource;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import fr.veridiangames.client.rendering.buffers.Buffers;
import fr.veridiangames.core.game.data.models.ModelVox;
import fr.veridiangames.core.loaders.ModelLoader;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;

public class ModelVoxRenderer
{
	public static final ModelVoxRenderer AK47_RENDERER = new ModelVoxRenderer(ModelLoader.loadVox(getResource("weapons/AK47.vox")), new Vec3(0.5f, 1f, 1f));
	public static final ModelVoxRenderer AWP_RENDERER = new ModelVoxRenderer(ModelLoader.loadVox(getResource("weapons/AWP.vox")));
	public static final ModelVoxRenderer SHOVEL_RENDERER = new ModelVoxRenderer(ModelLoader.loadVox(getResource("weapons/SHOVEL.vox")));
	public static final ModelVoxRenderer GRENADE_RENDERER = new ModelVoxRenderer(ModelLoader.loadVox(getResource("weapons/Grenade.vox")));

	public final static int FACE_SIZE = 4 * (3 + 4 + 3);
	private int vao, vbo;
	private FloatBuffer vertexBuffer;
	private int bufferSize;

	private ModelVox model;
	private Vec3 origin;

	public ModelVoxRenderer(ModelVox model)
	{
		this.model = model;
		this.origin = new Vec3(0);
		this.vertexBuffer = this.initBuffer(this.initFaces());
		this.initVAO();
	}

	public ModelVoxRenderer(ModelVox model, Vec3 origin)
	{
		this.model = model;
		this.origin = origin;
		this.vertexBuffer = this.initBuffer(this.initFaces());
		this.initVAO();
	}

	public void render()
	{
		glBindVertexArray(this.vao);
		glDrawArrays(GL_QUADS, 0, this.bufferSize * 4);
		glBindVertexArray(0);
	}

	private void initVAO()
	{
		this.vao = Buffers.createVertexArray();
		this.vbo = Buffers.createVertexBuffer();

		glBindVertexArray(this.vao);

		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);

		glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
		glBufferData(GL_ARRAY_BUFFER, this.vertexBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 10 * 4, 0L);
		glVertexAttribPointer(1, 4, GL_FLOAT, false, 10 * 4, 12L);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 10 * 4, 28L);

		glBindVertexArray(0);
	}

	private FloatBuffer initBuffer(int faceNum)
	{
		this.bufferSize = faceNum;
		FloatBuffer rb = BufferUtils.createFloatBuffer(faceNum * FACE_SIZE);

		int xSize = this.model.getXSize();
		int ySize = this.model.getYSize();
		int zSize = this.model.getZSize();
		int[][][] data = this.model.getData();
		for (int x = 0; x < xSize; x++)
			for (int y = 0; y < ySize; y++)
				for (int z = 0; z < zSize; z++)
				{
					if (data[x][y][z] == 0)
						continue;
					boolean left = this.model.getBlock(x - 1, y, z) == 0;
					boolean right = this.model.getBlock(x + 1, y, z) == 0;
					boolean front = this.model.getBlock(x, y, z - 1) == 0;
					boolean back = this.model.getBlock(x, y, z + 1) == 0;
					boolean top = this.model.getBlock(x, y + 1, z) == 0;
					boolean bottom = this.model.getBlock(x, y - 1, z) == 0;
					Color4f color = new Color4f(data[x][y][z]);

					float xx = x - this.origin.x * xSize;
					float yy = y - this.origin.y * ySize;
					float zz = z - this.origin.z * zSize;

					if (left)
						rb.put(this.blockDataLeft(xx, yy, zz, 1, color));

					if (right)
						rb.put(this.blockDataRight(xx, yy, zz, 1, color));

					if (front)
						rb.put(this.blockDataFront(xx, yy, zz, 1, color));

					if (back)
						rb.put(this.blockDataBack(xx, yy, zz, 1, color));

					if (top)
						rb.put(this.blockDataTop(xx, yy, zz, 1, color));

					if (bottom)
						rb.put(this.blockDataBottom(xx, yy, zz, 1, color));
				}
		rb.flip();
		return rb;
	}

	private int initFaces()
	{
		int xSize = this.model.getXSize();
		int ySize = this.model.getYSize();
		int zSize = this.model.getZSize();
		int[][][] data = this.model.getData();

		int facesSize = 0;
		for (int x = 0; x < xSize; x++)
			for (int y = 0; y < ySize; y++)
				for (int z = 0; z < zSize; z++)
				{
					if (data[x][y][z] == 0)
						continue;
					boolean left = this.model.getBlock(x - 1, y, z) == 0;
					boolean right = this.model.getBlock(x + 1, y, z) == 0;
					boolean front = this.model.getBlock(x, y, z - 1) == 0;
					boolean back = this.model.getBlock(x, y, z + 1) == 0;
					boolean top = this.model.getBlock(x, y + 1, z) == 0;
					boolean bottom = this.model.getBlock(x, y - 1, z) == 0;

					if (left)
						facesSize++;
					if (right)
						facesSize++;
					if (front)
						facesSize++;
					if (back)
						facesSize++;
					if (top)
						facesSize++;
					if (bottom)
						facesSize++;
				}
		return facesSize;
	}

	public float[] blockDataFront(float x, float y, float z, float scale, Color4f color) {
		return new float[] {
			x + scale, y, z,			color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a,	0, 0, 1,
			x, y, z,					color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a,	0, 0, 1,
			x, y + scale, z,			color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a,	0, 0, 1,
			x + scale, y + scale, z,	color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a,	0, 0, 1
		};
	}

	public float[] blockDataBack(float x, float y, float z, float scale, Color4f color) {
		return new float[] {
			x, y, z + scale,					color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a,	0, 0, -1,
			x + scale, y, z + scale,			color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a,	0, 0, -1,
			x + scale, y + scale, z + scale,	color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a,	0, 0, -1,
			x, y + scale, z + scale,			color.r * 0.9f, color.g * 0.9f, color.b * 0.9f, color.a,	0, 0, -1
		};
	}

	public float[] blockDataLeft(float x, float y, float z, float scale, Color4f color) {
		return new float[] {
			x, y + scale, z,			color.r * 0.8f, color.g * 0.8f, color.b * 0.8f, color.a,	1, 0, 0,
			x, y, z,					color.r * 0.8f, color.g * 0.8f, color.b * 0.8f, color.a,	1, 0, 0,
			x, y, z + scale,			color.r * 0.8f, color.g * 0.8f, color.b * 0.8f, color.a,	1, 0, 0,
			x, y + scale, z + scale,	color.r * 0.8f, color.g * 0.8f, color.b * 0.8f, color.a,	1, 0, 0
		};
	}

	public float[] blockDataRight(float x, float y, float z, float scale, Color4f color) {
		return new float[] {
			x + scale, y, z,					color.r * 0.8f, color.g * 0.8f, color.b * 0.8f, color.a,	-1, 0, 0,
			x + scale, y + scale, z,			color.r * 0.8f, color.g * 0.8f, color.b * 0.8f, color.a,	-1, 0, 0,
			x + scale, y + scale, z + scale,	color.r * 0.8f, color.g * 0.8f, color.b * 0.8f, color.a,	-1, 0, 0,
			x + scale, y, z + scale,			color.r * 0.8f, color.g * 0.8f, color.b * 0.8f, color.a,	-1, 0, 0
		};
	}

	public float[] blockDataBottom(float x, float y, float z, float scale, Color4f color) {
		return new float[] {
			x, y, z,					color.r * 0.7f, color.g * 0.7f, color.b * 0.7f, color.a,	0, -1, 0,
			x + scale, y, z,			color.r * 0.7f, color.g * 0.7f, color.b * 0.7f, color.a,	0, -1, 0,
			x + scale, y, z + scale,	color.r * 0.7f, color.g * 0.7f, color.b * 0.7f, color.a,	0, -1, 0,
			x, y, z + scale,			color.r * 0.7f, color.g * 0.7f, color.b * 0.7f, color.a,	0, -1, 0
		};
	}

	public float[] blockDataTop(float x, float y, float z, float scale, Color4f color) {
		return new float[] {
			x + scale, y + scale, z,			color.r * 1f, color.g * 1f, color.b * 1f, color.a,	0, 1, 0,
			x, y + scale, z,					color.r * 1f, color.g * 1f, color.b * 1f, color.a,	0, 1, 0,
			x, y + scale, z + scale, 			color.r * 1f, color.g * 1f, color.b * 1f, color.a,	0, 1, 0,
			x + scale, y + scale, z + scale,	color.r * 1f, color.g * 1f, color.b * 1f, color.a,	0, 1, 0
		};
	}

	public int getVertexCount() {
		return this.bufferSize;
	}
}
