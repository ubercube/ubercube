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
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import fr.veridiangames.core.maths.Vec3;

/**
 * Created by Aiko on 20/07/2016.
 */
public class CurvePrimitive {
    private int vao, vbo, ibo;
    private FloatBuffer buffer;
    private IntBuffer indices;
    private int size;

    public CurvePrimitive(ArrayList<Vec3> points) {
        this.buffer = BufferUtils.createFloatBuffer(3 * points.size());
        this.indices = BufferUtils.createIntBuffer(2 * points.size());

        for (int i = 0; i < points.size() - (points.size() % 2); i++)
        {
            Vec3 p = points.get(i);

            this.buffer.put(new float[]{
                    p.x, p.y, p.z,
            });

            this.indices.put(new int[] {
                    i, i+1
            });
        }

        this.size = points.size();

        this.buffer.flip();
        this.indices.flip();

        this.vao = glGenVertexArrays();
        this.vbo = glGenBuffers();
        this.ibo = glGenBuffers();

        glBindVertexArray(this.vao);

        glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
        glBufferData(GL_ARRAY_BUFFER, this.buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.indices, GL_STATIC_DRAW);

        glBindVertexArray(0);
    }

    public void render() {
        glBindVertexArray(this.vao);
        glEnableVertexAttribArray(0);

        glDrawElements(GL_LINES, this.size, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    public void delete()
    {
        this.buffer.clear();
        this.indices.clear();

        glDeleteVertexArrays(this.vao);
        glDeleteBuffers(this.vbo);
        glDeleteBuffers(this.ibo);
    }
}
