package fr.veridiangames.client.rendering.renderers.game.physics;

import fr.veridiangames.client.rendering.buffers.Buffers;
import fr.veridiangames.client.rendering.shaders.Shader;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.components.ECRender;
import fr.veridiangames.core.game.entities.components.ECRigidbody;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.game.entities.particles.ParticleSystem;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.physics.Rigidbody;
import fr.veridiangames.core.physics.colliders.AABoxCollider;
import fr.veridiangames.core.utils.Color4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

public class ColliderRenderer
{
	public static final int MAX_ENTITIES = 2000;

	private FloatBuffer instanceBuffer;
	private int vao, vbo, vio, ibo;
	private int renderCount;

	public ColliderRenderer()
	{
		this.instanceBuffer = BufferUtils.createFloatBuffer(MAX_ENTITIES * 20);
		for (int i = 0; i < MAX_ENTITIES; i++)
		{
			instanceBuffer.put(Mat4.identity().getComponents());
			instanceBuffer.put(Color4f.YELLOW.toArray());
		}
		instanceBuffer.flip();

		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(cubeVertices().length);
		verticesBuffer.put(cubeVertices());
		verticesBuffer.flip();

		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(cubeIndices().length);
		indicesBuffer.put(cubeIndices());
		indicesBuffer.flip();

		vao = Buffers.createVertexArray();
		vbo = Buffers.createVertexBuffer();
		vio = Buffers.createVertexBuffer();
		ibo = Buffers.createVertexBuffer();

		glBindVertexArray(vao);

		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		glEnableVertexAttribArray(4);
		glEnableVertexAttribArray(5);

		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0L);

		glBindBuffer(GL_ARRAY_BUFFER, vio);
		glBufferData(GL_ARRAY_BUFFER, instanceBuffer, GL_DYNAMIC_DRAW);
		glVertexAttribPointer(1, 4, GL_FLOAT, false, 20 * 4, 64L);
		glVertexAttribPointer(2, 4, GL_FLOAT, false, 20 * 4, 0L);
		glVertexAttribPointer(3, 4, GL_FLOAT, false, 20 * 4, 16L);
		glVertexAttribPointer(4, 4, GL_FLOAT, false, 20 * 4, 32L);
		glVertexAttribPointer(5, 4, GL_FLOAT, false, 20 * 4, 48L);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

		glVertexAttribDivisor(0, 0);
		glVertexAttribDivisor(1, 1);
		glVertexAttribDivisor(2, 1);
		glVertexAttribDivisor(3, 1);
		glVertexAttribDivisor(4, 1);
		glVertexAttribDivisor(5, 1);

		glBindVertexArray(0);
	}

	public void updateInstances(Map<Integer, Entity> entities, List<Integer> indices)
	{
		renderCount = 0;
		instanceBuffer.clear();
		for (int i = 0; i < indices.size(); i++)
		{
			Entity e = entities.get(indices.get(i));
			if (e == null)
				continue;
			if (!e.contains(EComponent.RIGIDBODY))
				continue;

			renderCount++;

			Rigidbody body = ((ECRigidbody) e.get(EComponent.RIGIDBODY)).getBody();
			if (body == null)
				continue;
			if (!(body.getCollider() instanceof AABoxCollider))
				continue;
			AABoxCollider collider = (AABoxCollider) body.getCollider();
			Mat4 matrix = Mat4.translate(collider.getPosition()).mul(Mat4.scale(collider.getSize()));

			instanceBuffer.put(matrix.getComponents());
			if (body.isEnabled())
				instanceBuffer.put(Color4f.GREEN.toArray());
			else
				instanceBuffer.put(Color4f.RED.toArray());
		}
		instanceBuffer.flip();
		glBindBuffer(GL_ARRAY_BUFFER, vio);
		glBufferData(GL_ARRAY_BUFFER, instanceBuffer, GL_DYNAMIC_DRAW);
	}

	public void render(Shader shader, Map<Integer, Entity> entities, List<Integer> indices)
	{
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glBindVertexArray(vao);
		glDrawElementsInstanced(GL_TRIANGLES, cubeIndices().length, GL_UNSIGNED_INT, 0L, renderCount);
		glBindVertexArray(0);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	}

	private float[] cubeVertices()
	{
		return new float[]
			{
				-1, -1, -1,
				1, -1, -1,
				1, -1, 1,
				-1, -1, 1,

				-1, 1, -1,
				1, 1, -1,
				1, 1, 1,
				-1, 1, 1
			};
	}

	private int[] cubeIndices()
	{
		return new int[]
			{
				0, 1, 2, 0, 2, 3,
				1, 5, 6, 1, 6, 2,

				5, 4, 7, 5, 7, 6,
				4, 0, 3, 4, 3, 7,

				1, 0, 4, 1, 4, 5,
				3, 2, 6, 3, 6, 7
			};
	}
}
