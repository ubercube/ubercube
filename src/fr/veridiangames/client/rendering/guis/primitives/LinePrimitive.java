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
import org.lwjgl.BufferUtils;

import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.maths.Vec3;

public class LinePrimitive {
	
	private int vao, vbo, ibo;
	private FloatBuffer buffer;
	private IntBuffer indices;
	
	public LinePrimitive(Vec3 a, Vec3 b) {
		buffer = BufferUtils.createFloatBuffer(3 * 2);
		indices = BufferUtils.createIntBuffer(2);
		
		buffer.put(new float[]{
			a.x, a.y, a.z,
			b.x, b.y, b.z
		});

		indices.put(new int[] {
			0, 1
		});
		
		buffer.flip();
		indices.flip();
		
		vao = glGenVertexArrays();
		vbo = glGenBuffers();
		ibo = glGenBuffers();
		
		glBindVertexArray(vao);
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		
		glBindVertexArray(0);
	}
	
	public void render(GuiShader shader) {
		shader.setModelViewMatrix(Mat4.identity());
		
		glBindVertexArray(vao);
		glEnableVertexAttribArray(0);
		
		glDrawElements(GL_LINES, 2, GL_UNSIGNED_INT, 0);			
				
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);		
	}
}
