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

package fr.veridiangames.client.rendering.renderers.game.entities.particles;

import fr.veridiangames.client.rendering.Camera;
import fr.veridiangames.client.rendering.buffers.Buffers;
import fr.veridiangames.client.rendering.shaders.Shader;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.components.ECRender;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.game.entities.particles.Particle;
import fr.veridiangames.core.game.entities.particles.ParticleSystem;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.utils.Color4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

/**
 * Created by Marc on 07/06/2016.
 */
public class ParticleRenderer
{
    public static final int MAX_PARTICLES = 10000;

    private FloatBuffer     instanceBuffer;
    private int             vao, vbo, vio, ibo;
    private int             renderCount;

    public ParticleRenderer()
    {
        this.instanceBuffer = BufferUtils.createFloatBuffer(MAX_PARTICLES * 20);
        for (int i = 0; i < MAX_PARTICLES; i++)
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

    public void updateInstances(Camera camera, Map<Integer, Entity> entities, List<Integer> indices)
    {
        renderCount = 0;
        instanceBuffer.clear();
        for (int i = 0; i < indices.size(); i++)
        {
            Entity e = entities.get(indices.get(i));
            if (e instanceof ParticleSystem)
            {
                List<Particle> particles = ((ParticleSystem) e).getParticles();

                for (int j = 0; j < particles.size(); j++)
                {
                    Particle p = particles.get(j);
                    if (!camera.isInViewFrustum(p.getTransform().getPosition().copy(), 0))
                        continue;
                    renderCount++;
                    if (renderCount >= 10000)
                        break;

                    instanceBuffer.put(p.getTransform().toMatrix().getComponents());
                    instanceBuffer.put(p.getColor().toArray());
                }
            }
        }
        instanceBuffer.flip();
        glBindBuffer(GL_ARRAY_BUFFER, vio);
        glBufferData(GL_ARRAY_BUFFER, instanceBuffer, GL_DYNAMIC_DRAW);
    }

    public void render(Shader shader, Map<Integer, Entity> entities, List<Integer> indices)
    {
        glBindVertexArray(vao);
        glDrawElementsInstanced(GL_TRIANGLES, cubeIndices().length, GL_UNSIGNED_INT, 0L, renderCount);
        glBindVertexArray(0);
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
