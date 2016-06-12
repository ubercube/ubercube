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

package fr.veridiangames.client.rendering.renderers.guis;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;

import fr.veridiangames.core.maths.Vec2i;
import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.client.guis.components.GuiComponent;
import fr.veridiangames.client.rendering.buffers.Buffers;

public class GuiStaticRenderer
{
	private int currentCanvas;
	private int vao, vbo, tbo, cbo, ibo;
	private FloatBuffer vertexBuffer;
	private FloatBuffer coordsBuffer;
	private FloatBuffer colorBuffer;
	private IntBuffer indexBuffer;
	private int size;
	
	public GuiStaticRenderer()
	{
		currentCanvas = -1;
		int maxsize = 20;
		vertexBuffer = BufferUtils.createFloatBuffer(4 * 3 * maxsize);
		coordsBuffer = BufferUtils.createFloatBuffer(4 * 4 * maxsize);
		colorBuffer = BufferUtils.createFloatBuffer(4 * 4 * maxsize);
		indexBuffer = BufferUtils.createIntBuffer(6 * maxsize);
	}
	
	public void set(List<GuiComponent> staticDraws, int canvas)
	{
		if (currentCanvas == canvas)
			return;
		currentCanvas = canvas;
		
		this.size = 0;
		
		vao = Buffers.createVertexArray();
		vbo = Buffers.createVertexBuffer();
		tbo = Buffers.createVertexBuffer();
		cbo = Buffers.createVertexBuffer();
		ibo = Buffers.createVertexBuffer();
		
		vertexBuffer.clear();
		coordsBuffer.clear();
		colorBuffer.clear();
		indexBuffer.clear();
		int ioffs = 0;
		for (int i = 0; i < staticDraws.size(); i++)
		{
			GuiComponent gui = staticDraws.get(i);
			Vec2i pos = gui.getPosition();
			Vec2i size = gui.getSize();
			
			if (gui.getBorder() != 0)
			{
				Vec2i borderSize = new Vec2i(size).add(gui.getBorder() * 2);
				Vec2i borderPos = new Vec2i(pos).sub(borderSize.copy().div(2)).add(size.copy().div(2));
				addGuiToBuffer(borderPos, borderSize, gui.getBorderColor(), i + ioffs);
				ioffs++;
			}
			addGuiToBuffer(pos, size, gui.getColor(), i + ioffs);
		}
		
		vertexBuffer.flip();
		coordsBuffer.flip();
		colorBuffer.flip();
		indexBuffer.flip();
		
		glBindVertexArray(vao);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0L);

		glBindBuffer(GL_ARRAY_BUFFER, tbo);
		glBufferData(GL_ARRAY_BUFFER, coordsBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0L);
		
		glBindBuffer(GL_ARRAY_BUFFER, cbo);
		glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(2, 4, GL_FLOAT, false, 0, 0L);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

		glBindVertexArray(0);
	}
	
	private void addGuiToBuffer(Vec2i pos, Vec2i size, Color4f color, int i)
	{
		vertexBuffer.put(pos.x).put(pos.y).put(0);
		vertexBuffer.put(pos.x + size.x).put(pos.y).put(0);
		vertexBuffer.put(pos.x + size.x).put(pos.y + size.y).put(0);
		vertexBuffer.put(pos.x).put(pos.y + size.y).put(0);
		
		coordsBuffer.put(0).put(0);
		coordsBuffer.put(1).put(0);
		coordsBuffer.put(1).put(1);
		coordsBuffer.put(0).put(1);

		colorBuffer.put(color.r).put(color.g).put(color.b).put(color.a);
		colorBuffer.put(color.r).put(color.g).put(color.b).put(color.a);
		colorBuffer.put(color.r).put(color.g).put(color.b).put(color.a);
		colorBuffer.put(color.r).put(color.g).put(color.b).put(color.a);

		int index = i * 4;
		indexBuffer.put(index + 0).put(index + 1).put(index + 2);
		indexBuffer.put(index + 0).put(index + 2).put(index + 3);
		
		this.size++;
	}
	
	public void render()
	{
		if (currentCanvas == -1)
			return;
		if (size == 0)
			return;
		
		glBindVertexArray(vao);
		glDrawElements(GL_TRIANGLES, size * 6, GL_UNSIGNED_INT, 0L);
		glBindVertexArray(0);
	}
}
