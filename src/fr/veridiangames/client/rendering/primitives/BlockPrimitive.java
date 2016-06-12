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

package fr.veridiangames.client.rendering.primitives;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import fr.veridiangames.client.rendering.buffers.Buffers;

/**
 * Created by Marccspro on 8 mai 2016.
 */
public class BlockPrimitive
{
	private int vao, vbo;
	private static final FloatBuffer blockData = 
		(FloatBuffer) 
		BufferUtils.createFloatBuffer(4 * 4 * 6)
		.put(new float[] {
				1, 1, 1,	0.8f,
				-1, 1, 1,	0.8f,
				-1, -1, 1,	0.8f,
				1, -1, 1,	0.8f,

				-1, 1, -1,	0.8f,
				1, 1, -1,	0.8f,
				1, -1, -1,	0.8f,
				-1, -1, -1,	0.8f,
				
				-1, 1, 1,	0.9f,
				1, 1, 1,	0.9f,
				1, 1, -1,	0.9f,
				-1, 1, -1,	0.9f,
			
				1, -1, 1,	0.9f,
				-1, -1, 1,	0.9f,
				-1, -1, -1,	0.9f,
				1, -1, -1,	0.9f,
				
				1, 1, 1,	1f,
				1, -1, 1,	1f,
				1, -1, -1,	1f,
				1, 1, -1,	1f,
				
				-1, -1, 1,	1f,
				-1, 1, 1,	1f,
				-1, 1, -1,	1f,
				-1, -1, -1,	1f
			})
		.flip();
	
	public BlockPrimitive()
	{
		vao = Buffers.createVertexArray();
		vbo = Buffers.createVertexBuffer();
		
		glBindVertexArray(vao);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, blockData, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 4 * 4, 0L);
		glVertexAttribPointer(1, 1, GL_FLOAT, false, 4 * 4, 12L);
		
		glBindVertexArray(0);
	}
	
	public void render()
	{
		glBindVertexArray(vao);
		glDrawArrays(GL_QUADS, 0, 4 * 6);
		glBindVertexArray(0);
	}
}
