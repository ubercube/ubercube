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

import static javafx.scene.input.KeyCode.L;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL33.*;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import fr.veridiangames.client.rendering.renderers.game.entities.players.skeleton.PlayerSkeleton;
import fr.veridiangames.client.rendering.renderers.game.world.BlockData;
import fr.veridiangames.core.game.entities.player.NetworkedPlayer;
import org.lwjgl.BufferUtils;

import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.components.ECRender;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.game.entities.player.ClientPlayer;
import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.utils.Color4f;
import fr.veridiangames.client.rendering.buffers.Buffers;

/**
 * Created by Marccspro on 8 f�vr. 2016.
 */
public class PlayerRenderer
{
	public static final int ENTITY_COMPLEXITY = 10;
	public static final int MAX_ENTITIES = 2000;
	
	private FloatBuffer instanceBuffer;
	private int vao, vbo, vio;
	
	private int renderCount;

	private PlayerSkeleton skeleton;

	public PlayerRenderer()
	{
		this.skeleton = new PlayerSkeleton();
		this.instanceBuffer = BufferUtils.createFloatBuffer(MAX_ENTITIES * 20 * ENTITY_COMPLEXITY);
		for (int i = 0; i < MAX_ENTITIES; i++)
		{
			for (int j = 0; j < ENTITY_COMPLEXITY; j++)
			{
				instanceBuffer.put(Mat4.identity().getComponents());
				instanceBuffer.put(Color4f.BLACK.toArray());
			}
		}
		instanceBuffer.flip();
		
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(cubeVertices().length);
		verticesBuffer.put(cubeVertices());
		verticesBuffer.flip();

		vao = Buffers.createVertexArray();
		vbo = Buffers.createVertexBuffer();
		vio = Buffers.createVertexBuffer();

		glBindVertexArray(vao);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		glEnableVertexAttribArray(4);
		glEnableVertexAttribArray(5);
		glEnableVertexAttribArray(6);
		glEnableVertexAttribArray(1);
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 4 * 4, 0L);
		glVertexAttribPointer(6, 1, GL_FLOAT, false, 4 * 4, 12L);
		
		glBindBuffer(GL_ARRAY_BUFFER, vio);
		glBufferData(GL_ARRAY_BUFFER, instanceBuffer, GL_DYNAMIC_DRAW);
		glVertexAttribPointer(2, 4, GL_FLOAT, false, 20 * 4, 0L);
		glVertexAttribPointer(3, 4, GL_FLOAT, false, 20 * 4, 16L);
		glVertexAttribPointer(4, 4, GL_FLOAT, false, 20 * 4, 32L);
		glVertexAttribPointer(5, 4, GL_FLOAT, false, 20 * 4, 48L);
		glVertexAttribPointer(1, 4, GL_FLOAT, false, 20 * 4, 64L);

		glVertexAttribDivisor(0, 0);
		glVertexAttribDivisor(6, 0);
		glVertexAttribDivisor(2, 1);
		glVertexAttribDivisor(3, 1);
		glVertexAttribDivisor(4, 1);
		glVertexAttribDivisor(5, 1);
		glVertexAttribDivisor(1, 1);
		
		glBindVertexArray(0);
	}
	
	public void updateInstances(Map<Integer, Entity> entities, List<Integer> indices)
	{
		renderCount = 0;
		instanceBuffer.clear();
		for (int i = 0; i < indices.size(); i++)
		{
			Entity e = entities.get(indices.get(i));
			if (e instanceof ClientPlayer) 
				continue;
			if (!((ECRender)e.get(EComponent.RENDER)).isRendered())
				continue;
			renderCount++;
			Transform transform = ((ECRender) e.get(EComponent.RENDER)).getTransform();

			skeleton.updateChilds((NetworkedPlayer) e);
			skeleton.setParentTransform(transform);
			skeleton.setBufferData(instanceBuffer);
		}
		instanceBuffer.flip();
		glBindBuffer(GL_ARRAY_BUFFER, vio);
		glBufferData(GL_ARRAY_BUFFER, instanceBuffer, GL_DYNAMIC_DRAW);
	}
	
	public void render()
	{
		glBindVertexArray(vao);
		glDrawArraysInstanced(GL_TRIANGLES, 0, 36, renderCount * ENTITY_COMPLEXITY);
		glBindVertexArray(0);
	}

	private float[] cubeVertices() 
	{
		return new float[] 
		{
			-0.5f, 0, -0.5f, BlockData.BOTTOM_SHADING,
			0.5f, 0, -0.5f, BlockData.BOTTOM_SHADING,
			-0.5f, 0, 0.5f, BlockData.BOTTOM_SHADING,
			0.5f, 0, -0.5f, BlockData.BOTTOM_SHADING,
			0.5f, 0, 0.5f, BlockData.BOTTOM_SHADING,
			-0.5f, 0, 0.5f, BlockData.BOTTOM_SHADING,

			-0.5f, 1, -0.5f, BlockData.UP_SHADING,
			-0.5f, 1, 0.5f, BlockData.UP_SHADING,
			0.5f, 1, -0.5f, BlockData.UP_SHADING,
			0.5f, 1, -0.5f, BlockData.UP_SHADING,
			-0.5f, 1, 0.5f, BlockData.UP_SHADING,
			0.5f, 1, 0.5f, BlockData.UP_SHADING,

			0.5f, 0, -0.5f, BlockData.X_SHADING,
			0.5f, 1, -0.5f, BlockData.X_SHADING,
			0.5f, 0, 0.5f, BlockData.X_SHADING,
			0.5f, 1, -0.5f, BlockData.X_SHADING,
			0.5f, 1, 0.5f, BlockData.X_SHADING,
			0.5f, 0, 0.5f, BlockData.X_SHADING,

			-0.5f, 0, -0.5f, BlockData.X_SHADING,
			-0.5f, 0, 0.5f, BlockData.X_SHADING,
			-0.5f, 1, -0.5f, BlockData.X_SHADING,
			-0.5f, 1, -0.5f, BlockData.X_SHADING,
			-0.5f, 0, 0.5f, BlockData.X_SHADING,
			-0.5f, 1, 0.5f, BlockData.X_SHADING,

			-0.5f, 0, 0.5f, BlockData.Z_SHADING,
			0.5f, 0, 0.5f, BlockData.Z_SHADING,
			-0.5f, 1, 0.5f, BlockData.Z_SHADING,
			0.5f, 0, 0.5f, BlockData.Z_SHADING,
			0.5f, 1, 0.5f, BlockData.Z_SHADING,
			-0.5f, 1, 0.5f, BlockData.Z_SHADING,

			-0.5f, 0, -0.5f, BlockData.Z_SHADING,
			-0.5f, 1, -0.5f, BlockData.Z_SHADING,
			0.5f, 0, -0.5f, BlockData.Z_SHADING,
			0.5f, 0, -0.5f, BlockData.Z_SHADING,
			-0.5f, 1, -0.5f, BlockData.Z_SHADING,
			0.5f, 1, -0.5f, BlockData.Z_SHADING
		};
	}
}
