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

package fr.veridiangames.client.rendering.primitives;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.client.rendering.buffers.Buffers;

public class SpherePrimitive
{
	private int vao, vbo, ibo;
	
	private List<Vertex> 	vertices;
	private List<Triangle> 	triangles;
	
	public SpherePrimitive(int iteration)
	{
		this.vertices = new ArrayList<Vertex>();
		this.triangles = new ArrayList<Triangle>();
		
		generate(iteration);
		createBuffer();
	}
	
	private void generate(int iterations)
	{
		float t = (float) ((1.0 + Math.sqrt(5.0)) / 2.0);
		
		addVertex(-1, t, 0);
		addVertex(0, 1, -t);
		addVertex(-t, 0, -1);
		addVertex(0, -1, -t);
		addVertex(-1, -t, 0);
		addVertex(1, -t, 0);
		addVertex(0, -1, t);
		addVertex(t, 0, 1);
		addVertex(0, 1, t);
		addVertex(1, t, 0);
		addVertex(t, 0, -1);
		addVertex(-t, 0, 1);
		
		addTriangle(0, 1, 2);
		addTriangle(0, 2, 11);
		addTriangle(0, 11, 8);
		addTriangle(0, 8, 9);
		addTriangle(0, 9, 1);
		addTriangle(2, 4, 11);
		addTriangle(11, 4, 6);
		addTriangle(8, 11, 6);
		addTriangle(8, 6, 7);
		addTriangle(8, 7, 9);
		addTriangle(7, 10, 9);
		addTriangle(7, 5, 10);
		addTriangle(10, 5, 3);
		addTriangle(1, 10, 3);
		addTriangle(2, 3, 4);
		addTriangle(3, 5, 4);
		addTriangle(6, 5, 7);
		addTriangle(6, 4, 5);
		addTriangle(9, 10, 1);
		addTriangle(2, 1, 3);
		
		List<Triangle> oldTriangles = new ArrayList<Triangle>();
		List<Integer> addedVertices = new ArrayList<Integer>();
		
		for (int i = 0; i < vertices.size(); i++)
			addedVertices.add(i);
		
		for (int i = 0; i < iterations; i++)
		{
			oldTriangles.clear();
			oldTriangles.addAll(triangles);
			triangles.clear();
			
			for (int j = 0; j < oldTriangles.size(); j++)
			{
				subdivide(oldTriangles.get(j), addedVertices);
			}
		}
		
		for (int i = 0; i < vertices.size(); i++)
		{
			Vertex v = vertices.get(i);
			Vec3 c = new Vec3(0, 0, 0);
			
			Vec3 normal = v.getPosition().copy().sub(c).normalize();
			v.setNormal(normal);
			v.getPosition();
		}
	}
	
	private void subdivide(Triangle t, List<Integer> addedVertices)
	{
		int v1 = getMidVertex(t.a, t.b, addedVertices);
		int v2 = getMidVertex(t.b, t.c, addedVertices);
		int v3 = getMidVertex(t.c, t.a, addedVertices);
		
		addTriangle(t.a, v1, v3);
		addTriangle(t.b, v2, v1);
		addTriangle(t.c, v3, v2);
		addTriangle(v1, v2, v3);
	}
	
	private int getMidVertex(int i1, int i2, List<Integer> addedVertices)
	{
		Vec3 v1 = vertices.get(i1).getPosition();
		Vec3 v2 = vertices.get(i2).getPosition();
		
		Vec3 middle = v1.copy().add(v2).div(2);
		
		int i;
		if ((i = vertexExists(middle, addedVertices)) != -1)
		{
			return i;
		}
		
		addVertex(middle);
		addedVertices.add(vertices.size() - 1);
		
		return vertices.size() - 1;
		
	}
	
	private int vertexExists(Vec3 vertex, List<Integer> addedVertices)
	{
		for (int i = 0; i < addedVertices.size(); i++) 
		{
			if (vertices.get(addedVertices.get(i)).getPosition().equals(vertex))
			{
				return i;
			}
		}
		return -1;
	}
	
	public void addVertex(Vec3 v)
	{
		this.addVertex(v.x, v.y, v.z);
	}
	
	public void addVertex(float x, float y, float z)
	{
		vertices.add(new Vertex(new Vec3(x, y, z).normalize()));
	}
	
	public void addTriangle(int a, int b, int c)
	{
		triangles.add(new Triangle(a, b, c));
	}
	
	public void createBuffer()
	{
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.size() * 6);
		for (Vertex v : vertices)
		{
			verticesBuffer.put(v.getPosition().x);
			verticesBuffer.put(v.getPosition().y);
			verticesBuffer.put(v.getPosition().z);
			
			verticesBuffer.put(v.getNormal().x);
			verticesBuffer.put(v.getNormal().y);
			verticesBuffer.put(v.getNormal().z);
		}
		verticesBuffer.flip();
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(triangles.size() * 3);
		for (Triangle t : triangles)
		{
			indicesBuffer.put(t.a);
			indicesBuffer.put(t.b);
			indicesBuffer.put(t.c);
		}
		indicesBuffer.flip();
		
		vao = Buffers.createVertexArray();
		vbo = Buffers.createVertexBuffer();
		ibo = Buffers.createVertexBuffer();
		
		glBindVertexArray(vao);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * 4, 0L);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * 4, 12L);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
		
		glBindVertexArray(0);
	}

	public void render()
	{
		glBindVertexArray(vao);
		glDrawElements(GL_TRIANGLES, triangles.size() * 3, GL_UNSIGNED_INT, 0);
		glBindVertexArray(0);
	}
}
