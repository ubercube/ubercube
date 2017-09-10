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

package fr.veridiangames.client.rendering.renderers.game.entities;

import fr.veridiangames.client.rendering.buffers.Buffers;
import fr.veridiangames.client.rendering.renderers.Renderer;
import fr.veridiangames.client.rendering.renderers.models.OBJModel;
import fr.veridiangames.client.rendering.shaders.Shader;
import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.Entity;
import fr.veridiangames.core.game.entities.Model;
import fr.veridiangames.core.game.entities.components.ECModel;
import fr.veridiangames.core.game.entities.components.ECRender;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.game.entities.particles.ParticleSystem;
import fr.veridiangames.core.game.entities.player.Player;
import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Map;

import static fr.veridiangames.client.rendering.renderers.models.ModelVoxRenderer.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

/**
 * Created by Marc on 11/07/2016.
 */
public class ModeledEntityRenderer
{
    public ModeledEntityRenderer()
    {

    }

    public void render(Shader shader, int cubemap, Map<Integer, Entity> entities, List<Integer> indices)
    {
        Renderer.bindTextureCube(cubemap);
        glDisable(GL11.GL_CULL_FACE);

        for (int i = 0; i < indices.size(); i++)
        {
            Entity e = entities.get(indices.get(i));
            if (e == null)
            {
                GameCore.getInstance().getGame().remove(indices.get(i));
                continue;
            }
            if (!(e.contains(EComponent.RENDER) && e.contains(EComponent.MODEL)))
                continue;

            int model = ((ECModel) e.get(EComponent.MODEL)).getModel();
            Transform transform = ((ECRender) e.get(EComponent.RENDER)).getTransform();
            shader.setModelViewMatrix(transform.toMatrix().mul(Mat4.scale(1f/16f, 1f/16f, 1f/16f)));
            renderModel(model);
        }

        Renderer.bindTextureCube(0);
        glEnable(GL11.GL_CULL_FACE);
    }

    private void renderModel(int model)
    {
        switch (model)
        {
            case Model.AK47:
				OBJModel.AK47_RENDERER.render();
                break;
			case Model.AWP:
				OBJModel.AK47_RENDERER.render();
				break;
            case Model.SHOVEL:
				OBJModel.SHOVEL_RENDERER.render();
                break;
            case Model.GRENADE:
				OBJModel.GRENADE_RENDERER.render();
                break;
        }
    }
}
