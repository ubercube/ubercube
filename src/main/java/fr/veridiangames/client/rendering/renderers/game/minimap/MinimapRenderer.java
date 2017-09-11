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
import fr.veridiangames.core.utils.Indexer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static fr.veridiangames.core.maths.Mathf.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class MinimapRenderer
{
	private int width, height;
	private World world;

	private FloatBuffer positionsBuffer;
	private FloatBuffer colorsBuffer;
	private int vao, vbo, cbo;

	private HashMap<Integer, MinimapChunkRenderer> chunks;

	private MinimapHandler minimap;

	public MinimapRenderer(int width, int height)
	{
		this.world = Ubercube.getInstance().getGameCore().getGame().getWorld();
		this.width = width;
		this.height = height;
		this.minimap = Ubercube.getInstance().getMinimapHandler();
		this.chunks = new HashMap<>();
		fillChunks();
	}

	private void fillChunks()
	{
		for (int x = 0; x < world.getWorldSize(); x++)
		{
			for (int z = 0; z < world.getWorldSize(); z++)
			{
				int y = world.getHeighestPopulatedChunkIndexAt(x, z);
				MinimapChunkRenderer c = new MinimapChunkRenderer(x, y, z);
				chunks.put(Indexer.index3i(x, y, z), c);
			}
		}
	}

	public void update()
	{
		for (int index : world.getUpdateRequests())
		{
			MinimapChunkRenderer c = chunks.get(index);
			if (c == null)
				continue;
			c.update();
		}
	}

	public void render(MinimapShader shader, float scale)
	{
		Vec2 p = Ubercube.getInstance().getGameCore().getGame().getPlayer().getPosition().xz();
		Vec2 dir = Ubercube.getInstance().getGameCore().getGame().getPlayer().getRotation().getForward().xz().normalize();
		float yRot = toDegrees(atan2(dir.y, dir.x));

		shader.setModelViewMatrix(Mat4.translate(width / 2, height / 2, 0).mul(Mat4.rotate(0, 0, -yRot - 90).mul(Mat4.translate(-p.x * scale, -p.y * scale, 0).mul(Mat4.scale(scale, scale, scale)))));
		for (MinimapChunkRenderer c : chunks.values())
		{
			c.render(shader, scale);
		}
	}
}
