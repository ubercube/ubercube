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

package fr.veridiangames.client.guis.canvases;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.client.guis.GuiCanvas;
import fr.veridiangames.client.inputs.Input;
import fr.veridiangames.client.rendering.Display;
import fr.veridiangames.client.rendering.buffers.Buffers;
import fr.veridiangames.client.rendering.shaders.MainShader;

public class PlayerHUD extends GuiCanvas
{
	private int vao, vbo;
	private FloatBuffer vertexBuffer;
	private MainShader shader;
	
	private float width, height;
	
	private float blockSize;
	
	public PlayerHUD(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.shader = new MainShader();
		initBlockBuffer();
	}
	
	private void renderFrameBuffer()
	{
		this.renderBlock();
	}
	
	public void update(Display display)
	{
		if (display.getInput().getKey(Input.KEY_C))
		{
			blockSize -= (blockSize - 0.5f) * 0.3f;
		}
		else
		{
			blockSize -= (blockSize - 4) * 0.3f; 
		}
	}
	
	public void setDisplaySize(int w, int h)
	{
		if (w != width || h != height)
		{
			width = w;
			height = h;
		}
	}

	private void initBlockBuffer()
	{
		vertexBuffer = BufferUtils.createFloatBuffer(4 * 4 * 6);
		
		float size = 1f;
		vertexBuffer.put(new float[] {
			size, size, size,	0.8f,
			-size, size, size,	0.8f,
			-size, -size, size,	0.8f,
			size, -size, size,	0.8f,

			-size, size, -size,	0.8f,
			size, size, -size,	0.8f,
			size, -size, -size,	0.8f,
			-size, -size, -size,	0.8f,
			
			-size, size, size,	0.9f,
			size, size, size,	0.9f,
			size, size, -size,	0.9f,
			-size, size, -size,	0.9f,
		
			size, -size, size,	0.9f,
			-size, -size, size,	0.9f,
			-size, -size, -size,	0.9f,
			size, -size, -size,	0.9f,
			
			size, size, size,	1f,
			size, -size, size,	1f,
			size, -size, -size,	1f,
			size, size, -size,	1f,
			
			-size, -size, size,	1f,
			-size, size, size,	1f,
			-size, size, -size,	1f,
			-size, -size, -size,	1f
		});

		vertexBuffer.flip();
		
		vao = Buffers.createVertexArray();
		vbo = Buffers.createVertexBuffer();
		
		glBindVertexArray(vao);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 4 * 4, 0L);
		glVertexAttribPointer(1, 1, GL_FLOAT, false, 4 * 4, 12L);
		
		glBindVertexArray(0);
	}
	
	public void renderBlock()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		shader.bind();
		shader.setProjectionMatrix(Mat4.perspective(70.0f, width / height, 0.1f, 10.0f));
		shader.setModelViewMatrix(Mat4.translate(0, 0, 5f).mul(Mat4.rotate(45, 45, -90).mul(Mat4.scale(blockSize, blockSize, blockSize))));
		shader.setColor(1, 0, 1, 1);

		glBindVertexArray(vao);
		glDrawArrays(GL_QUADS, 0, 4 * 6);
		glBindVertexArray(0);
	}
}
