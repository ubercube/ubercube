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

package fr.veridiangames.client.rendering.guis.primitives;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import fr.veridiangames.client.rendering.shaders.GuiShader;
import fr.veridiangames.client.rendering.shaders.Shader;
import org.lwjgl.BufferUtils;

import fr.veridiangames.core.maths.Mat4;

public class QuadPrimitive {
	
	private static int vao, vbo, ibo;
	private static FloatBuffer buffer;
	private static IntBuffer indices;
	
	public QuadPrimitive() {
		buffer = BufferUtils.createFloatBuffer(4 * 5);
		indices = BufferUtils.createIntBuffer(3 * 2);
		
		buffer.put(new float[]{
			-1, -1, 0, 	0, 0,
			1, -1, 0, 	1, 0,
			1, 1, 0, 	1, 1,
			-1, 1, 0, 	0, 1
		});

		indices.put(new int[] {
			0, 1, 2,
			0, 2, 3
		});
		
		buffer.flip();
		indices.flip();

		vao = glGenVertexArrays();
		vbo = glGenBuffers();
		ibo = glGenBuffers();
		
		glBindVertexArray(vao);
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * 4, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * 4, 12);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		
		glBindVertexArray(0);
	}
	
	public void render(Shader shader, float x, float y, float z, float xScale, float yScale, float zScale) {
		shader.setModelViewMatrix(Mat4.translate((int)x, (int)y, (int)z).mul(Mat4.scale((int)xScale, (int)yScale, (int)zScale)));
		
		glBindVertexArray(vao);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);			
				
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		
		shader.setModelViewMatrix(Mat4.identity());
	}
}
