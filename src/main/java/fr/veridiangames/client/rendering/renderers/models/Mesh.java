package fr.veridiangames.client.rendering.renderers.models;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import fr.veridiangames.client.rendering.buffers.Buffers;
import org.lwjgl.BufferUtils;

public class Mesh {
	
	private int vbo, ibo;
	
	private int verticesSize;
	private int indicesSize;

	private FloatBuffer vertices;
	private IntBuffer indices;
	
	public Mesh(Vertex[] verts, int[] indices) {
		this.indicesSize = indices.length;
		this.verticesSize = verts.length;

		createBuffers(verts, indices);
		
		vbo = Buffers.createVertexBuffer();
		ibo = Buffers.createVertexBuffer();
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, this.vertices, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.indices, GL_STATIC_DRAW);
	}
	
	public void createBuffers(Vertex[] verts, int[] indices) {
		vertices = BufferUtils.createFloatBuffer(verts.length * Vertex.BUFFER_SIZE);

		for (int i = 0; i < verts.length; i++) {
			vertices.put(verts[i].getPosition().x);
			vertices.put(verts[i].getPosition().y);
			vertices.put(verts[i].getPosition().z);
			
			vertices.put(verts[i].getNormal().x);
			vertices.put(verts[i].getNormal().y);
			vertices.put(verts[i].getNormal().z);
		}
		
		vertices.flip();

		this.indices = BufferUtils.createIntBuffer(indices.length);
		this.indices.put(indices);
		this.indices.flip();
	}
	
	public void render() {
		glEnableVertexAttribArray(0); // position
		glEnableVertexAttribArray(2); // normal

		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.BUFFER_SIZE * 4, 0);
		glVertexAttribPointer(2, 3, GL_FLOAT, true, Vertex.BUFFER_SIZE * 4, 12);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glDrawElements(GL_TRIANGLES, indicesSize, GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(2);
	}

	public int getVerticesSize() {
		return verticesSize;
	}
	
	public int getIndicesSize() {
		return indicesSize;
	}

	public FloatBuffer getVertices() {
		return vertices;
	}

	public IntBuffer getIndices() {
		return indices;
	}
}