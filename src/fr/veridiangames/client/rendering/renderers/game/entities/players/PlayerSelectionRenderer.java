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

package fr.veridiangames.client.rendering.renderers.game.entities.players;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.client.main.player.PlayerSelection;
import fr.veridiangames.client.rendering.buffers.Buffers;
import fr.veridiangames.client.rendering.shaders.WorldShader;

public class PlayerSelectionRenderer
{
	private PlayerSelection selection;
	
	private int vao, vbo;
	private FloatBuffer vertexBuffer;
	
	public PlayerSelectionRenderer(PlayerSelection selection)
	{
		this.selection = selection;
		
		createBuffer();
	}
	
	private void createBuffer()
	{
		vertexBuffer = BufferUtils.createFloatBuffer(3 * 4 * 6);
		
		float size = 1f;
		vertexBuffer.put(new float[] {
			size, size, size,
			-size, size, size,
			-size, -size, size,
			size, -size, size,

			-size, size, -size,
			size, size, -size,
			size, -size, -size,
			-size, -size, -size,
			
			-size, size, size,
			size, size, size,
			size, size, -size,
			-size, size, -size,
		
			size, -size, size,
			-size, -size, size,
			-size, -size, -size,
			size, -size, -size,
			
			size, size, size,
			size, -size, size,
			size, -size, -size,
			size, size, -size,
			
			-size, -size, size,
			-size, size, size,
			-size, size, -size,
			-size, -size, -size
		});

		vertexBuffer.flip();
		
		vao = Buffers.createVertexArray();
		vbo = Buffers.createVertexBuffer();
		
		glBindVertexArray(vao);
		
		glEnableVertexAttribArray(0);
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0L);
		
		glBindVertexArray(0);
	}
	
	public void render(WorldShader shader)
	{
		if (selection.hasSelection())
		{
			shader.setModelViewMatrix(Mat4.translate(selection.getPos()).mul(Mat4.scale(selection.getSize())));
			shader.setColor(1, 1, 1, 0.2f);
			glBindVertexArray(vao);
			glDrawArrays(GL_QUADS, 0, 4 * 6);
			glBindVertexArray(0);
			
			shader.setColor(0, 0, 0, 0.5f);
			glLineWidth(2);
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
			glBindVertexArray(vao);
			glDrawArrays(GL_QUADS, 0, 4 * 6);
			glBindVertexArray(0);
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		}
	}
	
	public PlayerSelection getSelection()
	{
		return selection;
	}
}
