package fr.veridiangames.client.rendering.renderers.game.minimap;

import fr.veridiangames.client.Ubercube;
import fr.veridiangames.client.main.minimap.MinimapHandler;
import fr.veridiangames.client.rendering.buffers.Buffers;
import fr.veridiangames.client.rendering.shaders.MinimapShader;
import fr.veridiangames.core.game.world.Chunk;
import fr.veridiangames.core.game.world.World;
import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.maths.Vec2;
import fr.veridiangames.core.utils.Color4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

import static fr.veridiangames.core.maths.Mathf.atan2;
import static fr.veridiangames.core.maths.Mathf.toDegrees;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class MinimapChunkRenderer
{
	private int x, z;
	private World world;

	private FloatBuffer positionsBuffer;
	private FloatBuffer colorsBuffer;
	private int vao, vbo, cbo;


	public MinimapChunkRenderer(int x, int z)
	{
		this.x = x;
		this.z = z;
		this.world = Ubercube.getInstance().getGameCore().getGame().getWorld();
		createBuffer();
	}

	public void createBuffer()
	{
		positionsBuffer = BufferUtils.createFloatBuffer(Chunk.SIZE * Chunk.SIZE * 4 * 3);
		colorsBuffer = BufferUtils.createFloatBuffer(Chunk.SIZE * Chunk.SIZE * 4 * 4);

		for (int x = 0; x < Chunk.SIZE; x++)
		{
			for (int z = 0; z < Chunk.SIZE; z++)
			{
				int xx = x + this.x * Chunk.SIZE;
				int zz = z + this.z * Chunk.SIZE;
				int block = world.getHeighestBlockAt(xx, zz);

				Color4f c = new Color4f(block);

				positionsBuffer.put(xx).put(zz).put(0);
				colorsBuffer.put(c.r).put(c.g).put(c.b).put(1f);

				positionsBuffer.put(xx + 1).put(zz).put(0);
				colorsBuffer.put(c.r).put(c.g).put(c.b).put(1f);

				positionsBuffer.put(xx + 1).put(zz + 1).put(0);
				colorsBuffer.put(c.r).put(c.g).put(c.b).put(1f);

				positionsBuffer.put(xx).put(zz + 1).put(0);
				colorsBuffer.put(c.r).put(c.g).put(c.b).put(1f);
			}
		}

		positionsBuffer.flip();
		colorsBuffer.flip();

		vao = Buffers.createVertexArray();
		vbo = Buffers.createVertexBuffer();
		cbo = Buffers.createVertexBuffer();

		glBindVertexArray(vao);

		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0L);

		glBindBuffer(GL_ARRAY_BUFFER, cbo);
		glBufferData(GL_ARRAY_BUFFER, colorsBuffer, GL_DYNAMIC_DRAW);
		glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0L);

		glBindVertexArray(0);

		positionsBuffer.clear();
		colorsBuffer.clear();
		positionsBuffer = null;
		colorsBuffer = null;
	}

	public void update()
	{
		System.out.println("Updating");
		colorsBuffer = BufferUtils.createFloatBuffer(Chunk.SIZE * Chunk.SIZE * 4 * 4);

		for (int x = 0; x < Chunk.SIZE; x++)
		{
			for (int z = 0; z < Chunk.SIZE; z++)
			{
				int xx = x + this.x * Chunk.SIZE;
				int zz = z + this.z * Chunk.SIZE;
				int block = world.getHeighestBlockAt(xx, zz);

				Color4f c = new Color4f(block);

				colorsBuffer.put(c.r).put(c.g).put(c.b).put(1f);
				colorsBuffer.put(c.r).put(c.g).put(c.b).put(1f);
				colorsBuffer.put(c.r).put(c.g).put(c.b).put(1f);
				colorsBuffer.put(c.r).put(c.g).put(c.b).put(1f);
			}
		}

		colorsBuffer.flip();

		glBindBuffer(GL_ARRAY_BUFFER, cbo);
		glBufferData(GL_ARRAY_BUFFER, colorsBuffer, GL_DYNAMIC_DRAW);

		colorsBuffer.clear();
		colorsBuffer = null;
	}

	public void render(MinimapShader shader, float scale)
	{
		glBindVertexArray(vao);
		glDrawArrays(GL_QUADS, 0, Chunk.SIZE * Chunk.SIZE * 4);
		glBindVertexArray(0);
	}
}
